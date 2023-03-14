package hu.ps.templates.reactiveservicetemplate.controller;

import hu.ps.templates.reactiveservicetemplate.config.OAuth2ResourceServerSecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;


@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@Import(OAuth2ResourceServerSecurityConfiguration.class)
class MessageControllerTests {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void greetsAuthenticatedUser() throws Exception {
        // @formatter:off
        this.webTestClient.mutateWith(mockJwt().jwt((jwt) -> jwt.subject("subject"))).get().uri("/api/message/welcome")
                .exchange()
                .expectStatus().isOk()
                .expectBody().toString().startsWith("Hello authenticated user -> subject (");

        var response = this.webTestClient.mutateWith(mockJwt().jwt((jwt) -> jwt.subject("subject").claim("name", "attila"))).get().uri("/api/message/welcome")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).returnResult().getResponseBody();

        assertTrue(response.startsWith("Hello authenticated user -> attila ("));
        // @formatter:on
    }

    @Test
    void messageCanBeReadWithScopeMessageReadAuthority() throws Exception {
        // @formatter:off
        var response = this.webTestClient.mutateWith(mockJwt().jwt((jwt) -> jwt.claim("scope", "message:read")))
                .get().uri("/api/message/secret")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("secret message");
        // @formatter:on
    }

    @Test
    void messageCanNotBeReadWithoutScopeMessageReadAuthority() throws Exception {
        // @formatter:off
        this.webTestClient.mutateWith(mockJwt().jwt((jwt) -> jwt.subject("subject")))
                .get().uri("/api/message/secret")
                .exchange()
                .expectStatus().isForbidden();
        // @formatter:on
    }

    @Test
    void messageCanNotBeCreatedWithoutAnyScope() throws Exception {
        // @formatter:off
        this.webTestClient.mutateWith(mockJwt().jwt((jwt) -> jwt.claim("name", "attila")))
                .post().uri("/api/message/secret")
                .bodyValue("Hello message")
                .exchange()
                .expectStatus().isForbidden();
        // @formatter:on
    }

    @Test
    void messageCanNotBeCreatedWithScopeMessageReadAuthority() throws Exception {
        // @formatter:off
        this.webTestClient.mutateWith(mockJwt().jwt((jwt) -> jwt.claim("scope", "message:read")))
                .post().uri("/api/message/secret")
                .bodyValue("Hello message")
                .exchange()
                .expectStatus().isForbidden();
        // @formatter:on
    }

    @Test
    void messageCanBeCreatedWithScopeMessageWriteAuthority() throws Exception {
        // @formatter:off
        this.webTestClient.mutateWith(mockJwt().jwt((jwt) -> jwt.claim("scope", "message:write")))
                .post().uri("/api/message/secret")
                .bodyValue("Hello message")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Message was created. Content: Hello message");
        // @formatter:on
    }

}
