package org.uqbar.heladeriakotlin

import jakarta.transaction.Transactional
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
import org.uqbar.heladeriakotlin.model.Duenio
import org.uqbar.heladeriakotlin.model.Heladeria
import org.uqbar.heladeriakotlin.model.TipoHeladeria
import org.uqbar.utils.getHeladeriaBase
import org.uqbar.utils.toJSON

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Dado un controlador")
class HeladeriaApplicationTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `Buscar una heladeria indicando parte del nombre`() {
        mockMvc.perform(get("/heladerias/buscar").param("nombre", "tuc")).andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].nombre").value("Tucán"))
    }

    @Test
    fun `Listar todas las heladerias si no se ingresa texto de busqueda`() {
        mockMvc.perform(get("/heladerias/buscar")).andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.length()").value(3))
    }

    @Test
    fun `Buscar una heladeria por nombre no encuentra ninguna que coincida`() {
        mockMvc.perform(get("/heladerias/buscar").param("nombre", "inexistente")).andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.length()").value(0))
    }

    @Test
    fun `Buscar una heladeria por id`() {
        mockMvc.perform(get("/heladerias/id/{id}", "1")).andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.nombre").value("Tucán"))
    }

    @Test
    fun `Buscar una heladeria por id inexistente devuelve not found`() {
        mockMvc.perform(get("/heladerias/id/{id}", "999")).andExpect(status().isNotFound)
    }

    @Test
    fun `Listar todos los duenios`() {
        mockMvc.perform(get("/duenios")).andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.length()").value(3))
    }

    @Test
    @Transactional
    fun `Crear un nuevo duenio con un payload valido`() {
        val body = mapOf("nombreCompleto" to "Fernando Dodino").toJSON()

        mockMvc.perform(
            post("/duenios").contentType(MediaType.APPLICATION_JSON).content(body)
        ).andExpect(status().isOk).andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.nombreCompleto").value("Fernando Dodino"))
    }

    @Test
    fun `No se puede crear un duenio con nombre vacio`() {
        val body = mapOf("nombreCompleto" to "").toJSON()

        mockMvc.perform(
            post("/duenios").contentType(MediaType.APPLICATION_JSON).content(body)
        ).andExpect(status().isBadRequest)
    }

    @Test
    @Transactional
    fun `Al actualizar una heladeria el id en el body es obligatorio`() {
        val body = Heladeria("Test", TipoHeladeria.ECONOMICA, Duenio("Carlos", 1L)).apply {
            this.gustos = mutableMapOf("Chocolate" to 5)
        }.toJSON()

        mockMvc.perform(
            put("/heladerias/{heladeriaId}", "1").contentType(MediaType.APPLICATION_JSON).content(body)
        ).andExpect(status().isBadRequest)
    }

    @Test
    @Transactional
    fun `Al actualizar una heladeria el id en el body debe coincidir con el id en la URL`() {
        val idBody = 1L
        val idURL = "2"
        val body = Heladeria("Test", TipoHeladeria.ECONOMICA, Duenio("Carlos", idBody), 1L).apply {
            this.gustos = mutableMapOf("Chocolate" to 5)
        }.toJSON()

        mockMvc.perform(
            put("/heladerias/{heladeriaId}", idURL).contentType(MediaType.APPLICATION_JSON).content(body)
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun `Para actualizar una heladeria el nombre no puede estar vacio`() {
        val body = getHeladeriaBase().apply {
            this.nombre = ""
        }.toJSON()

        mockMvc.perform(
            put("/heladerias/{heladeriaId}", "1").contentType(MediaType.APPLICATION_JSON).content(body)
        ).andExpect(status().isBadRequest)
    }

    @Test
    @Transactional
    fun `Actualizar el nombre de una heladeria`() {
        val body = getHeladeriaBase().apply {
            this.nombre = "nuevoNombre"
        }.toJSON()

        mockMvc.perform(
            put("/heladerias/{heladeriaId}", "1").contentType(MediaType.APPLICATION_JSON).content(body)
        ).andExpect(status().isOk).andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.nombre").value("nuevoNombre"))
    }

    @Test
    @Transactional
    fun `Actualizar el duenio de una heladeria`() {
        val body = getHeladeriaBase().apply {
            this.duenio = Duenio("Alejandro Dini", 2L) // el nombre es irrelevante, solo importa el id
        }.toJSON()

        mockMvc.perform(
            put("/heladerias/{heladeriaId}", "1").contentType(MediaType.APPLICATION_JSON).content(body)
        ).andExpect(status().isOk).andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.duenio.id").value(2L))
    }

    @Test
    @Transactional
    fun `Para actualizar el duenio de una heladeria el id del duenio es obligatorio`() {
        val body = getHeladeriaBase().apply {
            this.duenio = Duenio("Alejandro Dini")
        }.toJSON()

        mockMvc.perform(
            put("/heladerias/{heladeriaId}", "1").contentType(MediaType.APPLICATION_JSON).content(body)
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun `No se puede actualizar el duenio de una heladeria a uno inexistente`() {
        val body = getHeladeriaBase().apply {
            this.duenio = Duenio("Carlos Martinelli", 9999L)
        }.toJSON()


        mockMvc.perform(
            put("/heladerias/{heladeriaId}/", "1").contentType(MediaType.APPLICATION_JSON).content(body)
        ).andExpect(status().isNotFound)
    }

    @Test
    @Transactional
    fun `Actualizar el tipo de una heladeria`() {
        val body = getHeladeriaBase().apply {
            this.tipoHeladeria = TipoHeladeria.INDUSTRIAL
        }.toJSON()

        mockMvc.perform(
            put("/heladerias/{heladeriaId}", "1").contentType(MediaType.APPLICATION_JSON).content(body)
        ).andExpect(status().isOk).andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.tipoHeladeria").value("INDUSTRIAL"))
    }


    @Test
    @Transactional
    fun `Modificar los gustos de una heladeria`() {
        val body = getHeladeriaBase().apply {
            this.gustos["nuevo"] = 7
        }.toJSON()

        mockMvc.perform(
            put("/heladerias/{heladeriaId}", "1").contentType(MediaType.APPLICATION_JSON).content(body)
        ).andExpect(status().isOk).andExpect(jsonPath("$.gustos.nuevo").value(7))
    }

    @Test
    fun `Un gusto no puede tener una dificultad mayor a 10`() {
        val body = getHeladeriaBase().apply {
            this.gustos["nuevo"] = 11
        }.toJSON()

        mockMvc.perform(
            put("/heladerias/{heladeriaId}", "1").contentType(MediaType.APPLICATION_JSON).content(body)
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun `Un gusto no puede tener una dificultad menor a 1`() {
        val body = getHeladeriaBase().apply {
            this.gustos["nuevo"] = 0
        }.toJSON()

        mockMvc.perform(
            put("/heladerias/{heladeriaId}", "1").contentType(MediaType.APPLICATION_JSON).content(body)
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun `Una heladeria debe tener al menos un gusto`() {
        val body = getHeladeriaBase().apply {
            this.gustos = mutableMapOf()
        }.toJSON()

        mockMvc.perform(
            put("/heladerias/{heladeriaId}", "1").contentType(MediaType.APPLICATION_JSON).content(body)
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun `Los nombres de los gustos de una heladeria no pueden ser string vacios`() {
        val body = getHeladeriaBase().apply {
            this.gustos[""] = 5
        }.toJSON()
        mockMvc.perform(
            put("/heladerias/{heladeriaId}", "1").contentType(MediaType.APPLICATION_JSON).content(body)
        ).andExpect(status().isBadRequest)
    }

}
