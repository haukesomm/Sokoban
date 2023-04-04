package de.haukesomm.sokoban.web

import dev.fritz2.core.Store
import dev.fritz2.core.WithJob
import dev.fritz2.core.storeOf
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import org.w3c.dom.events.EventListener

fun WithJob.initDarkMode(): Store<Boolean> {
    val darkModeQueryList = window.matchMedia("(prefers-color-scheme: dark)")
    val themeStateStore = storeOf(darkModeQueryList.matches)

    darkModeQueryList.addEventListener("change", EventListener { event ->
        (event.asDynamic().matches as? Boolean)?.let {
            themeStateStore.update(it)
        } ?: run {
            console.warn("Sokoban: Unable to update dark mode based on browser event")
        }
    })

    window.document.querySelector(":root")?.let { element ->
        (MainScope() + job).launch {
            themeStateStore.data.collect {
                if (it) element.classList.add("dark")
                else element.classList.remove("dark")
            }
        }
    }

    return themeStateStore
}