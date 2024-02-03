package org.gengar.backend.service

import org.gengar.backend.model.TinyRequest
import org.gengar.backend.model.TinyResponse
import org.gengar.backend.persistence.UrlRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.security.MessageDigest
import java.util.Base64

@Service
class TinyService(
    val urlRepository: UrlRepository
) {
    fun shortenUrl(tinyRequest: TinyRequest): TinyResponse {
        val tinyResponse = TinyResponse(
            longUrl = tinyRequest.longUrl,
            shortUrl = generateUniqueString(tinyRequest.longUrl),
            userId = tinyRequest.userId
        )
        return urlRepository.save(tinyResponse)
    }

    fun lookUp(url: String): String {
        try {
            val longUrl = urlRepository.findByShortUrl(url)?.longUrl
            return longUrl ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Tiny Url not listed")
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error during lookup", e)
        }
    }

    private fun generateUniqueString(longUrl: String): String {
        val hashedBytes = sha256Hash(longUrl)
        val base64 = Base64.getUrlEncoder().encodeToString(hashedBytes)
        return base64.substring(0, 7)


    }

    private fun sha256Hash(input: String): ByteArray {
        val messageDigest = MessageDigest.getInstance("SHA-256")
        return messageDigest.digest(input.toByteArray())
    }


}