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

    // Set global default text color
    document.querySelector("body")
        ?.classList
        ?.add("text-neutral-dark", "dark:text-neutral-light")
}