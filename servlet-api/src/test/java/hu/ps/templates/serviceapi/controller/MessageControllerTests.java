package hu.ps.templates.serviceapi.controller;

import hu.ps.templates.serviceapi.config.OAuth2ResourceServerSecurityConfiguration;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MessageController.class)
@Import(OAuth2ResourceServerSecurityConfiguration.class)
class MessageControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void greetsAuthenticatedUser() throws Exception {
        // @formatter:off
        this.mockMvc.perform(get("/api/message/welcome").with(jwt().jwt((jwt) -> jwt.subject("ch4mpy"))))
                .andExpect(content().string(startsWith("Hello authenticated user -> ch4mpy (")));
        // @formatter:on
    }

    @Test
    void messageCanBeReadWithScopeMessageReadAuthority() throws Exception {
        // @formatter:off
        this.mockMvc.perform(get("/api/message/secret").with(jwt().jwt((jwt) -> jwt.claim("scope", "message:read"))))
                .andExpect(content().string(is("secret message")));

        this.mockMvc.perform(get("/api/message/secret").with(jwt().authorities(new SimpleGrantedAuthority(("SCOPE_message:read")))))
                .andExpect(content().string(is("secret message")));
        // @formatter:on
    }

    @Test
    void messageCanNotBeReadWithoutScopeMessageReadAuthority() throws Exception {
        // @formatter:off
        this.mockMvc.perform(get("/api/message/secret").with(jwt()))
                .andExpect(status().isForbidden());
        // @formatter:on
    }

    @Test
    void messageCanNotBeCreatedWithoutAnyScope() throws Exception {
        // @formatter:off
        this.mockMvc.perform(post("/api/message/secret")
                        .content("Hello message")
                        .with(jwt()))
                .andExpect(status().isForbidden());
        // @formatter:on
    }

    @Test
    void messageCanNotBeCreatedWithScopeMessageReadAuthority() throws Exception {
        // @formatter:off
        this.mockMvc.perform(post("/api/message/secret")
                        .content("Hello message")
                        .with(jwt().jwt((jwt) -> jwt.claim("scope", "message:read"))))
                .andExpect(status().isForbidden());
        // @formatter:on
    }

    @Test
    void messageCanBeCreatedWithScopeMessageWriteAuthority() throws Exception {
        // @formatter:off
        this.mockMvc.perform(post("/api/message/secret")
                        .content("Hello message")
                        .with(jwt().jwt((jwt) -> jwt.claim("scope", "message:write"))))
                .andExpect(status().isOk())
                .andExpect(content().string(is("Message was created. Content: Hello message")));
        // @formatter:on
    }

}
