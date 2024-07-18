package io.github.xisabla.back;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = BackApplication.class, properties = {
    "git.build.version=1.0.0",
    "git.commit.id=0000000000000000000000000000000000000000",
    "git.build.time=2024-00-00T00:00:00+00:00"
})
@AutoConfigureMockMvc
class BackApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnVersionInformation() throws Exception {
        mockMvc.perform(get("/info/version"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.version").isString())
            .andExpect(jsonPath("$.version").value("1.0.0"))
            .andExpect(jsonPath("$.build").isString())
            .andExpect(jsonPath("$.build").value("0000000000000000000000000000000000000000"))
            .andExpect(jsonPath("$.timestamp").isString())
            .andExpect(jsonPath("$.timestamp").value("2024-00-00T00:00:00+00:00"));
    }
}
