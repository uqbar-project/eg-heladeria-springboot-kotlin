package org.uqbar.heladeriakotlin.service

import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.uqbar.heladeriakotlin.dao.RepoUsuarios
import org.uqbar.heladeriakotlin.dto.CredencialesDTO
import org.uqbar.heladeriakotlin.errorHandling.CredencialesInvalidasException

@Service
@Transactional
class UsuarioService : UserDetailsService {

   val logger = LoggerFactory.getLogger(UsuarioService::class.java)

   @Autowired
   lateinit var usuarioRepository: RepoUsuarios

//   @Autowired
//   lateinit var tokenUtils: TokenUtils

   @Transactional(Transactional.TxType.REQUIRED)
   fun login(credenciales: CredencialesDTO) {
      val usuario = validarUsuario(credenciales.usuario)
//      usuario.loguearse()
//      usuario.validarCredenciales(credenciales.password)
   }

   fun validarUsuario(nombreUsuario: String) = usuarioRepository.findByUsername(nombreUsuario).orElseThrow { CredencialesInvalidasException() }

//   @Transactional(Transactional.TxType.NEVER)
//   fun usuarios() = usuarioRepository.findAll().map { usuario ->
//      usuario.toUsuarioBaseDTO()
//   }

//   @Transactional(Transactional.TxType.REQUIRED)
//   fun crearUsuario(credencialesDTO: CredencialesDTO): Usuario {
//      if (usuarioRepository.findByNombre(credencialesDTO.usuario).isPresent) {
//         throw BusinessException("Ya existe un usuario con ese nombre")
//      }
//      val usuario = Usuario().apply {
//         nombre = credencialesDTO.usuario
//         crearPassword(credencialesDTO.password)
//         validar()
//      }
//      usuarioRepository.save(usuario)
//      return usuario
//   }
//
//   @Transactional(Transactional.TxType.REQUIRED)
//   fun eliminarUsuario(idUsuario: Long): Usuario {
//      val usuario = getUsuarioPorId(idUsuario)
//      usuarioRepository.delete(usuario)
//      return usuario
//   }

   // NOT_SUPPORTED permite que otro metodo en transacción lo llame y luego continúe con dicha transacción
   // con NEVER si intentamos llamar desde un metodo que tiene transacción a verUsuario, dispara una excepción
   @Transactional(Transactional.TxType.NOT_SUPPORTED)
   fun verUsuario(nombreUsuario: String) = usuarioRepository.findByUsername(nombreUsuario).orElseThrow { NotFoundException("No se encontró el usuario con el nombre $nombreUsuario") }

   override fun loadUserByUsername(username: String?): UserDetails {
      if (username == null) throw CredencialesInvalidasException()
      val user = verUsuario(username)
      logger.info("Usuario " + user.username + " - Roles: " + roleFor(username))
      return User(user.username, user.password, roleFor(username))
   }

   private fun roleFor(username: String) = if (username.lowercase() == "admin") listOf(SimpleGrantedAuthority("ROLE_ADMIN")) else listOf()

   private fun getUsuarioPorId(idUsuario: Long) = usuarioRepository.findById(idUsuario).orElseThrow { NotFoundException("No se encontró el usuario con el identificador $idUsuario") }

   // El efecto que tiene es simplemente devolver un ok si el filtro de JWT (JWTAuthorizationFilter) pasa
   fun validar(): String = "ok"

}
