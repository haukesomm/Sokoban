package de.haukesomm.sokoban.web

import de.haukesomm.sokoban.web.components.alert
import de.haukesomm.sokoban.web.components.game.gameFrame
import de.haukesomm.sokoban.web.components.icons.HeroIcons
import dev.fritz2.core.RenderContext
import dev.fritz2.core.render

fun main() {
    render {
        div("h-screen w-full hidden lg:block") {
            gameFrame()
        }
        div("h-screen w-full block lg:hidden") {
            mobileDeviceNotSupportedBanner()
        }
    }
}

private fun RenderContext.mobileDeviceNotSupportedBanner() =
    div("m-4") {
        alert(
            title = "This app is desktop-only",
            message = """Currently, this app does not support mobile devices.
                | In order to use this app, re-open it on a computer.
            """.trimMargin(),
            icon = HeroIcons.desktop_computer
        )
    }