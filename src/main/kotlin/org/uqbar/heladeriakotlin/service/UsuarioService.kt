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
import org.uqbar.heladeriakotlin.security.TokenUtils

@Service
@Transactional
class UsuarioService : UserDetailsService {

   val logger = LoggerFactory.getLogger(UsuarioService::class.java)

   @Autowired
   lateinit var usuarioRepository: RepoUsuarios

   @Autowired
   lateinit var tokenUtils: TokenUtils

   @Transactional(Transactional.TxType.REQUIRED)
   fun login(credencialesDTO: CredencialesDTO): String {
      val usuario = validarUsuario(credencialesDTO.usuario)
      usuario.validarCredenciales(credencialesDTO.password)
      return tokenUtils.createToken(credencialesDTO.usuario, credencialesDTO.password, usuario.roles.map { it.name })!!
   }

   fun validarUsuario(nombreUsuario: String) = usuarioRepository.findByUsername(nombreUsuario).orElseThrow { CredencialesInvalidasException() }

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

}
