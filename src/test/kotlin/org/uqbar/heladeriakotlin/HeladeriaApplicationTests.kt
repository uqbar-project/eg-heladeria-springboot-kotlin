package org.uqbar.heladeriakotlin

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.transaction.Transactional
import org.junit.jupiter.api.BeforeEach
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
import org.uqbar.heladeriakotlin.dao.RepoUsuarios
import org.uqbar.heladeriakotlin.dto.CredencialesDTO
import org.uqbar.heladeriakotlin.model.*
import org.uqbar.heladeriakotlin.security.TokenUtils
import org.uqbar.utils.getHeladeriaBase
import org.uqbar.utils.toJSON

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Dado un controlador")
class HeladeriaApplicationTests {

    private val mapper = jacksonObjectMapper().apply {
        registerModule(JavaTimeModule())
    }
    
    @Autowired
    lateinit var repoUsuarios: RepoUsuarios
    
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var tokenUtils: TokenUtils

    lateinit var tokenUsuarioOk: String

    lateinit var tokenAdminOk: String

    @BeforeEach
    fun crearUsuarios() {
        repoUsuarios.deleteAll()
        tokenUsuarioOk = crearUsuario("elComun1", "password1", ROLES.READONLY.name)
        tokenAdminOk = crearUsuario("admin", "123456", ROLES.ADMIN.name)
    }

    // region POST /login
    @Test
    fun `usuario inexistente no pasa el login`() {
        mockMvc.perform(
            post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyUsuarioInexistente())
        ).andExpect(status().isUnauthorized)
    }

    @Test
    fun `usuario con password incorrecta no pasa el login`() {
        mockMvc.perform(
            post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyUsuarioPasswordIncorrecta())
        ).andExpect(status().isUnauthorized)
    }

    @Test
    fun `usuario existente pasa el login y retorna JWT`() {
        mockMvc.perform(
            post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyUsuarioExistente())
        )
            .andExpect(status().isOk)
    }
    // endregion

    // region GET /heladerias/buscar
    @Test
    fun `Buscar una heladeria sin estar logueado da error de autorización`() {
        mockMvc.perform(get("/heladerias/buscar").param("nombre", "tuc"))
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `Buscar una heladeria con un token inválido da error de autorización`() {
        mockMvc.perform(get("/heladerias/buscar")
            .param("nombre", "tuc")
            .header("Authorization", tokenUsuarioInvalido())
        )
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `Buscar una heladeria indicando parte del nombre con un usuario autenticado funciona ok`() {
        mockMvc.perform(
            get("/heladerias/buscar")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", tokenUsuarioOk)
                .param("nombre", "tuc")
        ).andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].nombre").value("Tucán"))
    }

    @Test
    fun `Listar todas las heladerias si no se ingresa texto de busqueda trae todas las heladerías`() {
        mockMvc.perform(get("/heladerias/buscar")
            .header("Authorization", tokenUsuarioOk)
        ).andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.length()").value(3))
    }

    @Test
    fun `Buscar una heladeria por nombre no encuentra ninguna que coincida`() {
        mockMvc.perform(get("/heladerias/buscar")
            .param("nombre", "inexistente")
            .header("Authorization", tokenUsuarioOk)
        ).andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.length()").value(0))
    }
    // endregion

    // region GET /heladerias/buscar/{id}
    @Test
    fun `Buscar una heladeria por id sin estar autenticado da error de autorización`() {
        mockMvc.perform(
            get("/heladerias/{id}", "1")
        ).andExpect(status().isUnauthorized)
    }

    @Test
    fun `Buscar una heladeria por id con un usuario autenticado funciona correctamente`() {
        mockMvc.perform(
            get("/heladerias/{id}", "1")
                .header("Authorization", tokenUsuarioOk)
        ).andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.nombre").value("Tucán"))
    }

    @Test
    fun `Buscar una heladeria por id inexistente devuelve not found`() {
        mockMvc.perform(
            get("/heladerias/{id}", "999")
                .header("Authorization", tokenUsuarioOk)
        ).andExpect(status().isNotFound)
    }
    // endregion

    // region GET /duenios
    @Test
    fun `Listar todos los duenios sin estar autenticado devuelve un error de autorización`() {
        mockMvc.perform(get("/duenios")).andExpect(status().isUnauthorized)
    }

    @Test
    fun `Listar todos los duenios`() {
        mockMvc.perform(
            get("/duenios")
                .header("Authorization", tokenUsuarioOk)
        ).andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.length()").value(3))
    }
    // endregion

    // region POST /duenios
    @Test
    @Transactional
    fun `Crear un nuevo duenio con un payload valido pero sin estar autenticado da error de autorización`() {
        val body = mapOf("nombreCompleto" to "Fernando Dodino").toJSON()

        mockMvc.perform(
            post("/duenios")
                .contentType(MediaType.APPLICATION_JSON).content(body)
        ).andExpect(status().isUnauthorized)
    }

    @Test
    @Transactional
    fun `Crear un nuevo duenio con un payload valido pero con un usuario que no es admin da error de permisos`() {
        val body = mapOf("nombreCompleto" to "Fernando Dodino").toJSON()

        mockMvc.perform(
            post("/duenios")
                .contentType(MediaType.APPLICATION_JSON).content(body)
                .header("Authorization", tokenUsuarioOk)
        ).andExpect(status().isForbidden)
    }

    @Test
    @Transactional
    fun `Crear un nuevo duenio con un payload valido y un usuario admin funciona correctamente`() {
        val body = mapOf("nombreCompleto" to "Fernando Dodino").toJSON()

        mockMvc.perform(
            post("/duenios")
                .contentType(MediaType.APPLICATION_JSON).content(body)
                .header("Authorization", tokenAdminOk)
        ).andExpect(status().isOk).andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.nombreCompleto").value("Fernando Dodino"))
    }

    @Test
    fun `No se puede crear un duenio con nombre vacio`() {
        val body = mapOf("nombreCompleto" to "").toJSON()

        mockMvc.perform(
            post("/duenios")
                .contentType(MediaType.APPLICATION_JSON).content(body)
                .header("Authorization", tokenAdminOk)
        ).andExpect(status().isBadRequest)
    }
    // endregion

    // region PUT /heladerias/{id}
    @Test
    @Transactional
    fun `Actualizar una heladeria sin estar logueado da error de autorización`() {
        val body = getHeladeriaBase().apply {
            this.nombre = "nuevoNombre"
        }.toJSON()

        mockMvc.perform(
            put("/heladerias/{heladeriaId}", "1").contentType(MediaType.APPLICATION_JSON).content(body)
        ).andExpect(status().isUnauthorized)
    }

    @Test
    @Transactional
    fun `Actualizar una heladeria con un usuario que no es admin da error de permisos`() {
        val body = getHeladeriaBase().apply {
            this.nombre = "nuevoNombre"
        }.toJSON()

        mockMvc.perform(
            put("/heladerias/{heladeriaId}", "1")
                .contentType(MediaType.APPLICATION_JSON).content(body)
                .header("Authorization", tokenUsuarioOk)
        ).andExpect(status().isForbidden)
    }

    @Test
    @Transactional
    fun `Al actualizar una heladeria el id en el body es obligatorio`() {
        val body = Heladeria("Test", TipoHeladeria.ECONOMICA, Duenio("Carlos", 1L)).apply {
            this.gustos = mutableMapOf("Chocolate" to 5)
        }.toJSON()

        mockMvc.perform(
            put("/heladerias/{heladeriaId}", "1")
                .contentType(MediaType.APPLICATION_JSON).content(body)
                .header("Authorization", tokenAdminOk)

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
            put("/heladerias/{heladeriaId}", idURL)
                .contentType(MediaType.APPLICATION_JSON).content(body)
                .header("Authorization", tokenAdminOk)
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun `Para actualizar una heladeria el nombre no puede estar vacio`() {
        val body = getHeladeriaBase().apply {
            this.nombre = ""
        }.toJSON()

        mockMvc.perform(
            put("/heladerias/{heladeriaId}", "1")
                .contentType(MediaType.APPLICATION_JSON).content(body)
                .header("Authorization", tokenAdminOk)
        ).andExpect(status().isBadRequest)
    }

    @Test
    @Transactional
    fun `Actualizar el nombre de una heladeria correctamente`() {
        val body = getHeladeriaBase().apply {
            this.nombre = "nuevoNombre"
        }.toJSON()

        mockMvc.perform(
            put("/heladerias/{heladeriaId}", "1")
                .contentType(MediaType.APPLICATION_JSON).content(body)
                .header("Authorization", tokenAdminOk)
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
            put("/heladerias/{heladeriaId}", "1")
                .contentType(MediaType.APPLICATION_JSON).content(body)
                .header("Authorization", tokenAdminOk)
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
            put("/heladerias/{heladeriaId}", "1")
                .contentType(MediaType.APPLICATION_JSON).content(body)
                .header("Authorization", tokenAdminOk)
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun `No se puede actualizar el duenio de una heladeria a uno inexistente`() {
        val body = getHeladeriaBase().apply {
            this.duenio = Duenio("Carlos Martinelli", 9999L)
        }.toJSON()


        mockMvc.perform(
            put("/heladerias/{heladeriaId}/", "1")
                .contentType(MediaType.APPLICATION_JSON).content(body)
                .header("Authorization", tokenAdminOk)
        ).andExpect(status().isNotFound)
    }

    @Test
    @Transactional
    fun `Actualizar el tipo de una heladeria`() {
        val body = getHeladeriaBase().apply {
            this.tipoHeladeria = TipoHeladeria.INDUSTRIAL
        }.toJSON()

        mockMvc.perform(
            put("/heladerias/{heladeriaId}", "1")
                .contentType(MediaType.APPLICATION_JSON).content(body)
                .header("Authorization", tokenAdminOk)
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
            put("/heladerias/{heladeriaId}", "1")
                .contentType(MediaType.APPLICATION_JSON).content(body)
                .header("Authorization", tokenAdminOk)
        ).andExpect(status().isOk).andExpect(jsonPath("$.gustos.nuevo").value(7))
    }

    @Test
    fun `Un gusto no puede tener una dificultad mayor a 10`() {
        val body = getHeladeriaBase().apply {
            this.gustos["nuevo"] = 11
        }.toJSON()

        mockMvc.perform(
            put("/heladerias/{heladeriaId}", "1")
                .contentType(MediaType.APPLICATION_JSON).content(body)
                .header("Authorization", tokenAdminOk)
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun `Un gusto no puede tener una dificultad menor a 1`() {
        val body = getHeladeriaBase().apply {
            this.gustos["nuevo"] = 0
        }.toJSON()

        mockMvc.perform(
            put("/heladerias/{heladeriaId}", "1")
                .contentType(MediaType.APPLICATION_JSON).content(body)
                .header("Authorization", tokenAdminOk)
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun `Una heladeria debe tener al menos un gusto`() {
        val body = getHeladeriaBase().apply {
            this.gustos = mutableMapOf()
        }.toJSON()

        mockMvc.perform(
            put("/heladerias/{heladeriaId}", "1")
                .contentType(MediaType.APPLICATION_JSON).content(body)
                .header("Authorization", tokenAdminOk)
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun `Los nombres de los gustos de una heladeria no pueden ser string vacios`() {
        val body = getHeladeriaBase().apply {
            this.gustos[""] = 5
        }.toJSON()
        mockMvc.perform(
            put("/heladerias/{heladeriaId}", "1")
                .contentType(MediaType.APPLICATION_JSON).content(body)
                .header("Authorization", tokenAdminOk)
        ).andExpect(status().isBadRequest)
    }
    // endregion

    // ****************************************** HELPERS **********************************************
    private fun bodyUsuarioExistente() = mapper.writeValueAsString(CredencialesDTO("elComun1", "password1"))

    private fun bodyUsuarioPasswordIncorrecta() = mapper.writeValueAsString(CredencialesDTO("elComun1", "password2"))

    private fun bodyUsuarioInexistente() =
        mapper.writeValueAsString(CredencialesDTO("usuarioInvalido", "cualquierPassword"))

    private fun tokenUsuarioInvalido() =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsaXR1cmJlIiwiaWF0IjoxNjcwNTk0OTI5LCJleHAiOjE2NzE2NzQ5MjksInJvbGVzIjoiUk9MRV9VU0VSIn0.hSrd0sTw1OH57YlmV19xNCtide76AZa476XjPwE1uiW0wgbo7w5CarrJWCLjy0e62EZIbVjEGmIdHZ5tMHGkyg"

    private fun crearUsuario(name: String, password: String, roles: String): String {
        val usuario = Usuario().apply {
            username = name
            crearPassword(password)
        }
        repoUsuarios.save(usuario)
        return "Bearer " + tokenUtils.createToken(usuario.username, listOf(roles))!!
    }
}
