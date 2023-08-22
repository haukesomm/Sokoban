package de.haukesomm.sokoban.web

import de.haukesomm.sokoban.web.components.alert
import de.haukesomm.sokoban.web.components.icons.HeroIcons
import dev.fritz2.core.RenderContext
import dev.fritz2.core.render
import kotlinx.browser.document
import org.w3c.dom.Element

fun main() {
    render {
        DarkModeStore.init(
            bgLight = "bg-gray-100",
            bgDark = "bg-darkgray-500"
        )

        div("hidden md:block") {
            gameFrame()
        }
        div("block md:hidden") {
            mobileDeviceNotSupportedBanner()
        }
    }
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