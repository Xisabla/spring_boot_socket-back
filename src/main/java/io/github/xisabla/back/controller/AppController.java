package io.github.xisabla.back.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.xisabla.back.service.InfoService;
import lombok.RequiredArgsConstructor;

/**
 * Controller for general application information.
 */
@RestController
@RequiredArgsConstructor
public class AppController {
    private final InfoService appService;

    @GetMapping("/info/version")
    public ResponseEntity<InfoService.Version> getVersion() {
        return ResponseEntity.ok(appService.getAppVersion());
    }
}
