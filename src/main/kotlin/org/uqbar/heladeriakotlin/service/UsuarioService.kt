package org.uqbar.heladeriakotlin.service

import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
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
      return tokenUtils.createToken(credencialesDTO.usuario, usuario.roles.map { it.name })!!
   }

   fun validarUsuario(nombreUsuario: String) = usuarioRepository.findByUsername(nombreUsuario).orElseThrow { CredencialesInvalidasException() }

   override fun loadUserByUsername(username: String?): UserDetails {
      if (username == null) throw CredencialesInvalidasException()
      val user = validarUsuario(username)
      return user.buildUser()
   }

}
