package de.haukesomm.sokoban.web.components

import de.haukesomm.sokoban.web.components.icons.HeroIcons
import de.haukesomm.sokoban.web.components.icons.IconDefinition
import de.haukesomm.sokoban.web.components.icons.icon
import dev.fritz2.core.RenderContext
import dev.fritz2.core.href
import dev.fritz2.core.target
import dev.fritz2.core.title

// FIXME: Anchor not focusable
fun RenderContext.iconLink(
    icon: IconDefinition,
    text: String,
    description: String? = null,
    hint: String? = null,
    href: String
) {
    a("flex items-center gap-3") {
        href(href)
        target("_blank")

        icon("w-5 h-5", definition = icon)

        div("grow flex flex-col") {
            +text
            description?.let {
                span("text-xs") {
                    +it
                    hint?.let {
                        title(it)
                    }
                }
            }
        }

        icon("w-4 h-4", definition = HeroIcons.link)
    }
}