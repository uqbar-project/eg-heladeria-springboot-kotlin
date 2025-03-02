package org.uqbar.heladeriakotlin.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.uqbar.heladeriakotlin.service.UsuarioService

@Component
class JWTAuthorizationFilter : OncePerRequestFilter() {

   @Autowired
   lateinit var tokenUtils: TokenUtils

   @Autowired
   lateinit var usuarioService: UsuarioService

   override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
      val bearerToken = request.getHeader("Authorization")
      if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
         val token = bearerToken.substringAfter("Bearer ")
         val usernamePAT = tokenUtils.getAuthentication(token)
         usuarioService.validarUsuario(usernamePAT.name)
         SecurityContextHolder.getContext().authentication = usernamePAT
         logger.info("username PAT: $usernamePAT")
      }
      filterChain.doFilter(request, response)
   }

}