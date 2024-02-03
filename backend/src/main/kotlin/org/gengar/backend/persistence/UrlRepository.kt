package org.gengar.backend.persistence

import org.gengar.backend.model.TinyResponse
import org.springframework.data.jpa.repository.JpaRepository

interface UrlRepository : JpaRepository<TinyResponse, Long> {
    fun findByShortUrl(shortUrl: String): TinyResponse?
}