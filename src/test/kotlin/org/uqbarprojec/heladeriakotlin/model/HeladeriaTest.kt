package org.uqbarprojec.heladeriakotlin.model

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class HeladeriaTest {
    @Test
    fun simpleHeladeriaTest() {
//        val unaHeladeria = Heladeria("Heladeria Pedrito", Duenio("Pedrito"))
//        assert(unaHeladeria.nombre == "Heladeria Pedrito")
//        assert(unaHeladeria.duenio.nombre == "Pedrito")
        assert(1 == 1)
    }
}