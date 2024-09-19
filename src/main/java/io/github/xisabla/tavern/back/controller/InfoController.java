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
    private final InfoService infoService;

    @GetMapping("/healthcheck")
    public ResponseEntity<Void> healthCheck() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/version")
    public ResponseEntity<InfoService.Version> getVersion() {
        return ResponseEntity.ok(infoService.getVersion());
    }
}
