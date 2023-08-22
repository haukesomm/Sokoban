package de.haukesomm.sokoban.web.components

import de.haukesomm.sokoban.web.components.icons.IconDefinition
import de.haukesomm.sokoban.web.components.icons.icon
import dev.fritz2.core.*
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLDivElement

fun RenderContext.withTitle(text: String, content: RenderContext.() -> Unit): Tag<HTMLDivElement> =
    div("space-y-4 dark:text-gray-300") {
        span("font-semibold dark:text-gray-300") {
            +text
        }
        content()
    }

// FIXME: Anchor not focusable
fun RenderContext.iconLink(icon: IconDefinition, text: String, href: String): Tag<HTMLAnchorElement> =
    a("flex items-center gap-2 text-gray-800 text-gray-700 dark:text-gray-200") {
        href(href)
        target("_blank")

        icon("w-6 h-6", definition = icon)

        span("text-sm font-medium") {
            +text
        }
    }