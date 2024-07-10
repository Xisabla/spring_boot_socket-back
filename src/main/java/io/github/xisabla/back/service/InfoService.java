package io.github.xisabla.back.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Service for application information.
 */
@Service
public class InfoService {
    @Data
    @AllArgsConstructor
    public class Version {
        private String version;
        private String build;
        private String time;
    }

    @Value("${git.build.version}")
    private String appVersion;

    @Value("${git.commit.id}")
    private String appBuild;

    @Value("${git.build.time}")
    private String appTime;

    public Version getAppVersion() {
        return new Version(
                appVersion.replace("-SNAPSHOT", ""),
                appBuild,
                appTime);
    }
}
