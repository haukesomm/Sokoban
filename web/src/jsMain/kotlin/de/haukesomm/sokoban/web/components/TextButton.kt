package de.haukesomm.sokoban.web.components

import de.haukesomm.sokoban.web.components.icons.IconDefinition
import de.haukesomm.sokoban.web.components.icons.icon
import dev.fritz2.core.Listener
import dev.fritz2.core.RenderContext
import dev.fritz2.core.disabled
import dev.fritz2.core.joinClasses
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.events.MouseEvent

fun RenderContext.textButton(
    label: String,
    icon: IconDefinition? = null,
    disabled: Flow<Boolean> = flowOf(false)
): Listener<MouseEvent, HTMLButtonElement> = button(
    joinClasses(
        "p-1.5 flex items-center gap-2",
        "bg-background-accent hover:bg-background-contrast rounded-md shadow-sm",
        "disabled:opacity-50 disabled:pointer-events-none"
    )
) {
    disabled(disabled)

    icon?.let {
        icon("w-4 h-4", definition = it)
    }
    +label
}.clicks