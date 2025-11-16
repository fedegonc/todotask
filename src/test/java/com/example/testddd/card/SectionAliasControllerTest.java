package com.example.testddd.card;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SectionAliasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void aliasShouldForwardToSection() throws Exception {
        mockMvc.perform(get("/servicios"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/asd"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/seccion/asd"));
    }

    @Test
    void shouldNotOverrideKnownRoutes() throws Exception {
        mockMvc.perform(get("/servicios"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/bitacora"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/seccion/servicios"))
                .andExpect(status().isOk());
    }
}
