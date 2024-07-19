package de.haukesomm.sokoban.web.components

import de.haukesomm.sokoban.web.components.icons.HeroIcons
import de.haukesomm.sokoban.web.components.icons.icon
import dev.fritz2.core.RenderContext
import dev.fritz2.core.joinClasses
import dev.fritz2.headless.components.popOver
import dev.fritz2.headless.foundation.Hook
import dev.fritz2.headless.foundation.hook
import dev.fritz2.headless.foundation.utils.floatingui.utils.PlacementValues

class PopOver {

    val label: FlowProperty<String> = FlowProperty()


    class ContentHook : Hook<RenderContext, Unit, Unit>() {
        operator fun invoke(content: RenderContext.() -> Unit) {
            this.value = { _, _ ->
                content()
            }
        }
    }

    val content: ContentHook = ContentHook()


    fun RenderContext.render() {
        popOver {
            popOverButton(
                joinClasses(
                    "p-1.5 w-full flex flex-row items-center gap-1",
                    "shadow-sm rounded-md bg-background-accent hover:bg-background-contrast"
                )
            ) {
                label.value?.renderText()
                opened.render { isOpened ->
                    icon(
                        "w-4 h-4 pointer-events-none",
                        definition = if (isOpened) HeroIcons.chevron_up else HeroIcons.chevron_down
                    )
                }
            }
            popOverPanel("mt-2 p-3 bg-background rounded shadow-sm border border-background-accent") {
                placement = PlacementValues.bottomStart
                div("max-w-80") {
                    hook(content)
                }
            }
        }
    }
}

fun RenderContext.popOver(initialize: PopOver.() -> Unit) {
    with(PopOver().apply(initialize)) {
        render()
    }
}