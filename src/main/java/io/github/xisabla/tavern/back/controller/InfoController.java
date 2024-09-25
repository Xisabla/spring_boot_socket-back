package io.github.xisabla.tavern.back.controller;

import io.github.xisabla.tavern.back.service.InfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for the info about the application.
 */
@RestController
@RequestMapping("/info")
@RequiredArgsConstructor
public class InfoController {
    /**
     * Service for getting the info about the application.
     */
    private final InfoService infoService;

    /**
     * Health check endpoint.
     *
     * @return 200 OK.
     */
    @GetMapping("/healthcheck")
    public ResponseEntity<Void> healthCheck() {
        return ResponseEntity.ok().build();
    }

    /**
     * Get the version of the application.
     *
     * @return The version of the application.
     */
    @GetMapping("/version")
    public ResponseEntity<InfoService.Version> getVersion() {
        return ResponseEntity.ok(infoService.getVersion());
    }
}
