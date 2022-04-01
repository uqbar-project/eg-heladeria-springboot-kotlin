package org.uqbarprojec.heladeriakotlin

import org.json.JSONObject
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import javax.transaction.Transactional


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

    @Test
    fun `Buscar una heladeria por nombre no encuentra ninguna que coincida`() {
        mockMvc
            .perform(get("/heladerias/buscar/{nombre}", "inexistente"))
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.length()").value(0))
    }

    @Test
    fun `Buscar una heladeria por id inexistente devuelve not found`() {
        mockMvc
            .perform(get("/heladerias/id/{id}", "999"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `Buscar heladeria pasando un id invalido devuelve bad request`() {
        mockMvc
            .perform(get("/heladerias/id/{id}", "invalido"))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `Listar todos los duenios`() {
        mockMvc
            .perform(get("/duenios"))
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.length()").value(3))
    }

    @Test
    @Transactional
    fun `Crear un nuevo duenio con un payload valido`() {
        val body = mapOf("nombreCompleto" to "Fernando Dodino")
        val json = JSONObject(body).toString()

        mockMvc
            .perform(
                post("/duenios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
            )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.nombreCompleto").value("Fernando Dodino"))
    }

    @Test
    fun `Intentar crear un duenio con payload invalido devuelve bad request`() {
        mockMvc
            .perform(
                post("/duenios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}")
            )
            .andExpect(status().isBadRequest)
    }
}
