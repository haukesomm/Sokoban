package de.haukesomm.sokoban.web

import de.haukesomm.sokoban.web.components.alert
import de.haukesomm.sokoban.web.components.icons.HeroIcons
import de.haukesomm.sokoban.web.theme.DarkModeStore
import dev.fritz2.core.RenderContext
import kotlinx.browser.document

fun main() {
    renderWithPortalRoot {
        initTheme()

        div("hidden md:block") {
            gameFrame()
        }
        div("block md:hidden") {
            mobileDeviceNotSupportedBanner()
        }
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

private fun RenderContext.mobileDeviceNotSupportedBanner() =
    div("m-4") {
        alert(
            title = "Mobile devices are not yet supported",
            message = """It appears you are accessing this app from a mobile device.
                | Unfortunately, Sokoban is not playable on mobile devices yet.
                | Please try again on a desktop computer.
            """.trimMargin(),
            icon = HeroIcons.device_mobile
        )
    }