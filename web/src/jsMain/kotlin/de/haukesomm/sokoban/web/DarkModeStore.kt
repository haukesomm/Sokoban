package de.haukesomm.sokoban.web

import dev.fritz2.core.RootStore
import kotlinx.browser.window
import org.w3c.dom.events.EventListener

private val darkModeQueryList = window.matchMedia("(prefers-color-scheme: dark)")

object DarkModeStore : RootStore<Boolean>(darkModeQueryList.matches) {

    private const val DARK_MODE_CLASS = "dark"

    init {
        darkModeQueryList.addEventListener("change", EventListener { event ->
            (event.asDynamic().matches as? Boolean)?.let {
                update(it)
            } ?: run {
                console.warn("Sokoban: Unable to update dark mode based on browser event")
            }
        })

        window.document.querySelector(":root")?.let { element ->
            data handledBy  {
                if (it) element.classList.add(DARK_MODE_CLASS)
                else element.classList.remove(DARK_MODE_CLASS)
            }
        }
    }
}