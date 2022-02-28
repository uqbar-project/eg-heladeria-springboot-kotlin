package org.uqbarprojec.heladeriakotlin

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Dado un controlador")
class HeladeriaApplicationTests(@Autowired val mockMvc: MockMvc) {

    @Test
    fun `Buscar una heladeria indicando parte del nombre`() {
        mockMvc
            .perform(get("/heladerias/buscar/tu"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].nombre").value("Tucán"))
    }

    @Test
    fun `Listar todas las heladerias si no se ingresa texto de busqueda`() {
        mockMvc
            .perform(get("/heladerias/buscar"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(3))
    }

}
