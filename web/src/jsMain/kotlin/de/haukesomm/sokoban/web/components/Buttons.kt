package de.haukesomm.sokoban.web.components

import de.haukesomm.sokoban.web.components.icons.IconDefinition
import de.haukesomm.sokoban.web.components.icons.icon
import dev.fritz2.core.RenderContext
import dev.fritz2.core.Tag
import dev.fritz2.core.disabled
import dev.fritz2.core.type
import org.w3c.dom.HTMLButtonElement

class PlainButton {

    enum class IconSize(internal val sizeClasses: String) {
        Small("w-4 h-4"),
        Medium("w-6 h-6"),
        Large("w-8 h-8")
    }

    var iconDefinition: FlowProperty<IconDefinition> = FlowProperty()
    var iconSize: IconSize = IconSize.Small

    val text: FlowProperty<String> = FlowProperty<String>()

    val disabled: FlowProperty<Boolean> = FlowProperty()


    fun RenderContext.render(): Tag<HTMLButtonElement> =
        button(
            """p-1 flex flex-row items-center gap-2 rounded-md focus:outline-none
                | focus-visible:bg-primary-100 focus-visible:dark:bg-primary-800
                | disabled:text-neutral-dark-disabled
                | disabled:dark:text-neutral-light-disabled""".trimMargin()
        ) {
            type("button")

            disabled.value?.let { disabled(it) }

            iconDefinition.value?.let { iconFlow ->
                iconFlow.render {
                    icon(iconSize.sizeClasses, definition = it)
                }
            }

            text.value?.let { textFlow ->
                textFlow.render {
                    span("min-w-max text-sm") {
                        +it
                    }
                }
            }
        }
}

fun RenderContext.plainButton(initialize: PlainButton.() -> Unit) =
    PlainButton().run {
        initialize()
        render()
    }