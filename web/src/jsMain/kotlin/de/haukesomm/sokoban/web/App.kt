package de.haukesomm.sokoban.web

import de.haukesomm.sokoban.web.theme.DarkModeStore
import kotlinx.browser.document

fun main() {
    renderWithPortalRoot {
        initTheme()
        gameFrame()
    }
}

private fun initTheme() {
    // Initialize dark mode handling.
    // This is also responsible for setting the global page background color.
    DarkModeStore.init(
        bgLight = "bg-background-light",
        bgDark = "bg-background-darkest"
    )


    // Set up necessary CSS classes on the <html> and <body> elements:

    document.querySelector("html")
        ?.classList
        ?.add("h-full")

    document.querySelector("body")
        ?.classList
        ?.add("h-full", "text-neutral-dark", "dark:text-neutral-light")
}