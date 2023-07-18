package de.haukesomm.sokoban.web.components

import de.haukesomm.sokoban.web.components.icons.IconDefinition
import de.haukesomm.sokoban.web.components.icons.icon
import dev.fritz2.core.RenderContext

fun RenderContext.alert(
    title: String,
    message: String? = null,
    icon: IconDefinition? = null
) =
    div(
        """w-full p-4 flex flex-col items-center gap-4
            | text-primary-500 bg-primary-100 text-sm
            | dark:text-primary-800 dark:bg-primary-400
            | border border-primary-500 rounded-lg
            | dark:border-primary-800
        """.trimMargin()
    ) {
        icon?.let {
            icon("w-6 h-6", definition = it)
        }
        p("font-semibold") {
            +title
        }
        message?.let {
            p {
                +it
            }
        }
    }