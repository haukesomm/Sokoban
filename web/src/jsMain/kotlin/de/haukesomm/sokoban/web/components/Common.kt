package de.haukesomm.sokoban.web.components

import de.haukesomm.sokoban.web.components.icons.IconDefinition
import de.haukesomm.sokoban.web.components.icons.icon
import dev.fritz2.core.RenderContext
import dev.fritz2.core.Tag
import dev.fritz2.core.href
import dev.fritz2.core.target
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLDivElement

fun RenderContext.withTitle(text: String, content: RenderContext.() -> Unit): Tag<HTMLDivElement> =
    div("space-y-4") {
        span("font-semibold") {
            +text
        }
        content()
    }

// FIXME: Anchor not focusable
fun RenderContext.iconLink(
    icon: IconDefinition,
    text: String,
    description: String? = null,
    href: String
): Tag<HTMLAnchorElement> =
    a("flex items-center gap-2") {
        href(href)
        target("_blank")

        icon("w-5 h-5", definition = icon)

        div("flex flex-col") {
            span("text-sm font-medium") {
                +text
            }
            description?.let {
                span("text-xs text-neutral-dark-secondary dark:text-neutral-light-secondary") {
                    +it
                }
            }
        }
    }