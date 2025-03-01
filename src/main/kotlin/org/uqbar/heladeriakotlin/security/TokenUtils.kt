package org.uqbar.heladeriakotlin.security

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import org.uqbar.heladeriakotlin.errorHandling.CredencialesInvalidasException
import org.uqbar.heladeriakotlin.errorHandling.TokenExpiradoException
import java.util.*
import kotlin.time.Duration.Companion.minutes

@Component
class TokenUtils {
   @Value("\${security.secret-key}")
   lateinit var secretKey: String

   @Value("\${security.access-token-minutes}")
   var accessTokenMinutes: Int = 30

   val logger = LoggerFactory.getLogger(TokenUtils::class.java)

   fun createToken(nombre: String, roles: List<String>): String? {
      val longExpirationTime = accessTokenMinutes.minutes.inWholeMilliseconds

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
      try {
         val secret = Keys.hmacShaKeyFor(secretKey.toByteArray())
         val claims = Jwts.parser()
            .verifyWith(secret)
            .build()
            // acá se valida el vencimiento del token
            .parseSignedClaims(token)
            .payload

         // Token no tiene usuario
         if (claims.subject == null || claims.subject.isBlank()) {
            throw CredencialesInvalidasException()
         }

         logger.info("Token decoded, user: " + claims.subject + " - roles: " + claims["roles"])

         val roles = (claims["roles"] as List<*>).map { SimpleGrantedAuthority(it.toString()) }
         return UsernamePasswordAuthenticationToken(claims.subject, null, roles)
      } catch (expiredJwtException: ExpiredJwtException) {
         throw TokenExpiradoException("Sesión vencida")
      }
   }
}