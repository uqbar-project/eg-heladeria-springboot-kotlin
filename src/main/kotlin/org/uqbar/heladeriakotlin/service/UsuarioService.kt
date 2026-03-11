package org.uqbar.heladeriakotlin.service

import org.springframework.transaction.annotation.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.uqbar.heladeriakotlin.dao.RepoRefreshToken
import org.uqbar.heladeriakotlin.dao.RepoUsuarios
import org.uqbar.heladeriakotlin.dto.CredencialesDTO
import org.uqbar.heladeriakotlin.dto.TokenResponseDTO
import org.uqbar.heladeriakotlin.errorHandling.CredencialesInvalidasException
import org.uqbar.heladeriakotlin.model.RefreshToken
import org.uqbar.heladeriakotlin.model.Usuario
import org.uqbar.heladeriakotlin.security.TokenUtils
import java.time.LocalDateTime

@Service
@Transactional
class UsuarioService : UserDetailsService {

   @Autowired
   lateinit var usuarioRepository: RepoUsuarios

   @Autowired
   lateinit var tokenUtils: TokenUtils

   @Autowired
   lateinit var refreshTokenRepository: RepoRefreshToken

   @Transactional
   fun login(credencialesDTO: CredencialesDTO): TokenResponseDTO {
      val usuario = validarUsuario(credencialesDTO.usuario)
      usuario.validarCredenciales(credencialesDTO.password)
      return issueToken(credencialesDTO.usuario, usuario.roles.map { it.name })
   }

   @Transactional
   fun issueToken(username: String, roles: List<String>): TokenResponseDTO {
      val accessToken = tokenUtils.createToken(username, roles)
         ?: throw CredencialesInvalidasException("Error generando access token")
      val refreshToken = createRefreshToken(username)
      return TokenResponseDTO(accessToken, refreshToken)
   }

   fun refreshAccessToken(refreshTokenString: String): TokenResponseDTO {
      val tokenHash = tokenUtils.hashToken(refreshTokenString)
      val refreshToken = refreshTokenRepository.findByTokenHash(tokenHash)
         .orElseThrow { CredencialesInvalidasException("Refresh token no encontrado") }

      if (!refreshToken.isValid()) {
         throw CredencialesInvalidasException("Refresh token expirado o revocado")
      }

      // Revocamos el refresh token viejo por seguridad
      refreshToken.revoked = true
      refreshTokenRepository.save(refreshToken)

      // Generamos el nuevo refresh token
      val usuario = validarUsuario(refreshToken.username)
      return issueToken(refreshToken.username, usuario.roles.map { it.name })
   }

   private fun createRefreshToken(username: String): String {
      val tokenString = tokenUtils.generateRefreshToken()
      val expirationDays = tokenUtils.getRefreshTokenExpirationDays()

      val refreshToken = RefreshToken().apply {
         tokenHash = tokenUtils.hashToken(tokenString)
         this.username = username
         expirationDate = LocalDateTime.now().plusDays(expirationDays.toLong())
      }

      refreshTokenRepository.save(refreshToken)
      return tokenString
   }

   fun validarUsuario(nombreUsuario: String): Usuario = usuarioRepository.findByUsername(nombreUsuario).orElseThrow { CredencialesInvalidasException() }

   override fun loadUserByUsername(username: String?): UserDetails {
      if (username == null) throw CredencialesInvalidasException()
      val user = validarUsuario(username)
      return user.buildUser()
   }

}
