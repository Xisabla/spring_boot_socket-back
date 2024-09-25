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
    /**
     * Version of the application.
     */
    @Value("${git.build.version}")
    private String version;

    /**
     * Commit ID of the application.
     */
    @Value("${git.commit.id}")
    private String commitId;

    /**
     * Timestamp of the build.
     */
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
    public static class Version {
        /**
         * Version of the application.
         */
        private String version;
        /**
         * Commit ID of the application.
         */
        private String build;
        /**
         * Timestamp of the build.
         */
        private String timestamp;
    }
}
