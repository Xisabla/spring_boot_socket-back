package io.github.xisabla.tavern.back.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service for the info about the application.
 */
@Service
public class InfoService {
    @Value("${git.build.version}")
    private String version;
    @Value("${git.commit.id}")
    private String commitId;
    @Value("${git.build.time}")
    private String appBuildTime;

    /**
     * Get the version of the application.
     *
     * @return The data structure providing the version of the application, its build and the timestamp of the build.
     */
    public Version getVersion() {
        return new Version(version, commitId, appBuildTime);
    }

    @Data
    @AllArgsConstructor
    public class Version {
        private String version;
        private String build;
        private String timestamp;
    }
}
