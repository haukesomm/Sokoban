package de.haukesomm.sokoban.web.components

import de.haukesomm.sokoban.web.components.icons.IconDefinition
import de.haukesomm.sokoban.web.components.icons.icon
import dev.fritz2.core.RenderContext

fun RenderContext.alert(
    title: String? = null,
    message: String? = null,
    icon: IconDefinition? = null
) =
    div(
        """w-full p-4 flex flex-col items-center gap-4
            | text-primary-500 bg-primary-100 text-sm
            | dark:text-primary-400 dark:bg-primary-800
            | border border-primary-500 rounded-lg
            | dark:border-primary-900
        """.trimMargin()
    ) {
        icon?.let {
            icon("w-6 h-6", definition = it)
        }
        title?.let {
            p("font-semibold") {
                +it
            }
        }
        message?.let {
            p {
                +it
            }
        }
    }