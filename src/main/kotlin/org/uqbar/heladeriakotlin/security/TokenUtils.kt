package org.uqbar.heladeriakotlin.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import org.uqbar.heladeriakotlin.errorHandling.CredencialesInvalidasException
import java.util.*

@Component
class TokenUtils {
   @Value("\${security.secret-key}")
   lateinit var secretKey: String

   @Value("\${security.access-token-minutes}")
   var accessTokenMinutes: Int = 60

   val logger = LoggerFactory.getLogger(TokenUtils::class.java)

   fun createToken(nombre: String, password: String, roles: List<String>): String? {
      val longExpirationTime = accessTokenMinutes * 60 * 1000

      val now = Date()

      return Jwts.builder()
         .subject(nombre)
         .issuedAt(now)
         .expiration(Date(now.time + longExpirationTime))
         .claim("roles", roles)
         .signWith(Keys.hmacShaKeyFor(secretKey.toByteArray()))
         .compact()
   }

   fun getAuthentication(token: String): UsernamePasswordAuthenticationToken {
      val secret = Keys.hmacShaKeyFor(secretKey.toByteArray())
      val claims = Jwts.parser()
         .verifyWith(secret)
         .build()
         .parseSignedClaims(token)
         .payload

      // Token no tiene usuario
      if (claims.subject == null || claims.subject.isBlank()) {
         throw CredencialesInvalidasException()
      }

      // Token vencido
      if (claims.expiration.before(Date())) {
         throw CredencialesInvalidasException()
      }

      logger.info("Token decoded, user: " + claims.subject + " - roles: " + claims["roles"])

      val roles = (claims["roles"] as List<*>).map { SimpleGrantedAuthority(it.toString()) }
      return UsernamePasswordAuthenticationToken(claims.subject, null, roles)
   }
}