package de.haukesomm.sokoban.web.components

import dev.fritz2.core.RenderContext
import dev.fritz2.core.Tag
import org.w3c.dom.HTMLDivElement

fun RenderContext.withTitle(text: String, content: RenderContext.() -> Unit): Tag<HTMLDivElement> =
    div("space-y-4") {
        span("font-semibold") {
            +text
        }
        content()
    }