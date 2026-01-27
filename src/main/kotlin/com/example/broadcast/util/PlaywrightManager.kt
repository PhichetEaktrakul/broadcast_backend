package com.example.broadcast.util

import com.microsoft.playwright.*
import jakarta.annotation.PreDestroy
import org.springframework.stereotype.Component

@Component
class PlaywrightManager {

    val playwright: Playwright = Playwright.create()
    val browser: Browser = playwright.chromium().launch(BrowserType.LaunchOptions().setHeadless(true))

    @PreDestroy
    fun close() {
        browser.close()
        playwright.close()
    }
}
