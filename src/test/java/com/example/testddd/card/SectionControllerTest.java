package com.example.testddd.card;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnServicesForExistingSectionKey() throws Exception {
        mockMvc.perform(get("/seccion/servicios"))
                .andExpect(status().isOk())
                .andExpect(view().name("service/list"))
                .andExpect(model().attributeExists("services"))
                .andExpect(model().attribute("services", not(empty())))
                .andExpect(model().attribute("sectionTitle", "Servicios"));
    }

    @Test
    void shouldShowEmptyMessageForUnknownSectionKey() throws Exception {
        mockMvc.perform(get("/seccion/ofertas"))
                .andExpect(status().isOk())
                .andExpect(view().name("service/list"))
                .andExpect(model().attribute("services", empty()))
                .andExpect(content().string(Matchers.containsString("Esta sección aún no tiene servicios.")));
    }
}
