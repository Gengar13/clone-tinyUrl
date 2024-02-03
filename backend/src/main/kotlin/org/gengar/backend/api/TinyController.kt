package org.gengar.backend.api

import org.gengar.backend.model.TinyRequest
import org.gengar.backend.model.TinyResponse
import org.gengar.backend.service.TinyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.view.RedirectView

@RestController
class TinyController(
    @Autowired
    val tinyService: TinyService

) {
    @PostMapping("/shortenUrl")
    fun createTinyUrl(@RequestBody tinyRequest: TinyRequest): TinyResponse {
        return tinyService.shortenUrl(tinyRequest)
    }

    @GetMapping("/{tinyUrl}")
    fun redirect(@PathVariable tinyUrl: String): RedirectView {
        try {
            val url = tinyService.lookUp(tinyUrl)
            return RedirectView(url)
        } catch (e: ResponseStatusException) {
            throw e
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error during redirection", e)
        }
    }

}