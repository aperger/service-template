package hu.ps.templates.serviceapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/message")
@Tag(name = "Messages API REST endpoints")
public class OAuth2ResourceServerController {

    @GetMapping("/welcome")
    @Operation(summary = "Welcome message to authenticated users")
    public String index(@AuthenticationPrincipal Jwt jwt) {
        String name = jwt.getClaim("name");
        String t = ZonedDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
        return String.format("Hello authenticated user -> %s (%s)!", name != null ? name : jwt.getSubject(), t);
    }

    @GetMapping("/secret")
    @Operation(summary = "Get secret message")
    public String getSecureMessage() {
        return "secret message";
    }

    @PostMapping("/secret")
    @Operation(summary = "Save secret message")
    public String createSecureMessage(@RequestBody String message) {
        return String.format("Message was created. Content: %s", message);
    }

}

