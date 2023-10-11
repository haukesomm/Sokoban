package de.haukesomm.sokoban.web.components.icons

import dev.fritz2.core.RenderContext
import dev.fritz2.core.fill
import dev.fritz2.core.viewBox

data class IconDefinition(
    val viewBoxWidth: Int,
    val viewBoxHeight: Int,
    val content: String
)

fun RenderContext.icon(
    classes: String? = null,
    id: String? = null,
    definition: IconDefinition
) =
    definition.run { ->
        svg(classes, id) {
            content(content)
            viewBox("0 0 $viewBoxWidth $viewBoxHeight")
            fill("none")
        }
    }