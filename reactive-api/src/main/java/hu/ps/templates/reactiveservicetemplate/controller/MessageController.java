package hu.ps.templates.reactiveservicetemplate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@CrossOrigin
@RestController
@RequestMapping("/api/message")
@Tag(name = "Messages API REST endpoints")
public class MessageController {

    @GetMapping("/welcome")
    @Operation(summary = "Welcome message to authenticated users")
    public Mono<String> index(@AuthenticationPrincipal Jwt jwt) {
        String name = jwt.getClaim("name");
        String t = ZonedDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
        return Mono.just(String.format("Hello authenticated user -> %s (%s)!", name != null ? name : jwt.getSubject(), t));
    }

    @GetMapping("/secret")
    @Operation(summary = "Get secret message")
    public Mono<String> getSecureMessage() {
        return Mono.just("secret message");
    }

    @PostMapping("/secret")
    @Operation(summary = "Save secret message")
    public Mono<String> createSecureMessage(@RequestBody String message) {
        return Mono.just(String.format("Message was created. Content: %s", message));
    }

}

