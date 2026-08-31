package com.agroflow.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtUtils {

    private val secretKey: SecretKey = Keys.hmacShaKeyFor("YWdyb2Zsb3ctc2VjcmV0LWtleS12ZXJ5LWxvbmctc2VjdXJlLWtleQ==".toByteArray())
    private val expirationMs: Long = 86400000 // 24 hours

    fun generateToken(id: String, rolId: String): String {
        return Jwts.builder()
            .subject(id)
            .claim("rolId", rolId)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + expirationMs))
            .signWith(secretKey)
            .compact()
    }

    fun extractAllClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
    }

    fun validateToken(token: String): Boolean {
        return try {
            val claims = extractAllClaims(token)
            !claims.expiration.before(Date())
        } catch (e: Exception) {
            false
        }
    }
}
