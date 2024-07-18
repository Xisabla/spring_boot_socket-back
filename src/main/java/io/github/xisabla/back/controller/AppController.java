package io.github.xisabla.back.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.xisabla.back.service.InfoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AppController {
    private final InfoService appService;

    @GetMapping("/test/endpoint")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("It just works !");
    }

    @GetMapping("/info/version")
    public ResponseEntity<InfoService.Version> getVersion() {
        return ResponseEntity.ok(appService.getAppVersion());
    }
}
