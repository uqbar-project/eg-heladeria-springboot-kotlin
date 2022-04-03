package org.uqbarprojec.heladeriakotlin

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
import org.uqbarprojec.heladeriakotlin.utils.toJSON
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(0))
    }

    @Test
    fun `Buscar una heladeria por id`() {
        mockMvc
            .perform(get("/heladerias/id/{id}", "1"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.nombre").value("Tucán"))
    }

    @Test
    fun `Buscar una heladeria por id inexistente devuelve not found`() {
        mockMvc
            .perform(get("/heladerias/id/{id}", "999"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `Listar todos los duenios`() {
        mockMvc
            .perform(get("/duenios"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(3))
    }

    @Test
    @Transactional
    fun `Crear un nuevo duenio con un payload valido`() {
        val body = mapOf("nombreCompleto" to "Fernando Dodino").toJSON()

        mockMvc
            .perform(
                post("/duenios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body)
            )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.nombreCompleto").value("Fernando Dodino"))
    }

    @Test
    fun `No se puede crear un duenio con nombre vacio`() {
        val body = mapOf("nombreCompleto" to "").toJSON()

        mockMvc
            .perform(
                post("/duenios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body)
            )
            .andExpect(status().isBadRequest)
    }

    @Test
    @Transactional
    fun `Actualizar parcialmente una heladeria`() {
        val body = mapOf("nombre" to "nuevoNombre").toJSON()

        mockMvc
            .perform(
                patch("/heladerias/{heladeriaId}/", "1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body)
            )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.nombre").value("nuevoNombre"))
    }

    @Test
    fun `No se puede actualizar una heladeria con datos invalidos`() {
        val body = mapOf("nombre" to "").toJSON()

        mockMvc
            .perform(
                patch("/heladerias/{heladeriaId}/", "1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body)
            )
            .andExpect(status().isBadRequest)
    }

    @Test
    //TODO: seria más correcto que devuelva 422 (Unprocessable Entity)
    fun `Actualizar el duenio de una heladeria a uno inexistente devuelve not found`() {
        val body = mapOf("duenio" to mapOf("id" to "9999")).toJSON()

        mockMvc
            .perform(
                patch("/heladerias/{heladeriaId}/", "1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body)
            )
            .andExpect(status().isNotFound)
    }

    @Test
    @Transactional
    fun `Agregar un gusto a una heladeria`() {
        val body = mapOf("nuevo" to "7").toJSON()

        mockMvc
            .perform(
                post("/heladerias/{heladeriaId}/gustos", "1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body)
            )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.gustos.nuevo").value(7))
    }

    @Test
    fun `Agregar un gusto con dificultad mayor a 10 devuelve bad request`() {
        val body = mapOf("nuevo" to "11").toJSON()

        mockMvc
            .perform(
                post("/heladerias/{heladeriaId}/gustos", "1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body)
            )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `Agregar un gusto con dificultad menor a 1 devuelve bad request`() {
        val body = mapOf("nuevo" to "0").toJSON()

        mockMvc
            .perform(
                post("/heladerias/{heladeriaId}/gustos", "1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body)
            )
            .andExpect(status().isBadRequest)
    }

    @Test
    @Transactional
    fun `Eliminar un gusto de una heladeria`() {
        val body = mapOf("frutilla" to "7").toJSON()

        mockMvc
            .perform(
                delete("/heladerias/{heladeriaId}/gustos", "1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body)
            )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.gustos.frutilla").doesNotExist())
    }

    @Test
    fun `No se puede eliminar un gusto que no pertenece a una heladeria`() {
        val body = mapOf("inexistente" to "7").toJSON()

        mockMvc
            .perform(
                delete("/heladerias/{heladeriaId}/gustos", "1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body)
            )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `No se puede eliminar el ultimo gusto de una heladeria`() {
        val body = mapOf("crema americana" to "2").toJSON()

        mockMvc
            .perform(
                delete("/heladerias/{heladeriaId}/gustos", "3")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body)
            )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `No se puede crear un gusto con nombre vacio`() {
        val body = mapOf("" to "7").toJSON()

        mockMvc
            .perform(
                post("/heladerias/{heladeriaId}/gustos", "1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body)
            )
            .andExpect(status().isBadRequest)
    }


}
