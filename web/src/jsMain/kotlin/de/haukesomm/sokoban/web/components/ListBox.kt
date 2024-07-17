package de.haukesomm.sokoban.web.components

import de.haukesomm.sokoban.web.components.icons.HeroIcons
import de.haukesomm.sokoban.web.components.icons.icon
import dev.fritz2.core.RenderContext
import dev.fritz2.core.joinClasses
import dev.fritz2.headless.components.listbox
import dev.fritz2.headless.foundation.DatabindingProperty
import kotlinx.coroutines.flow.map

class ListBox<T> {

    var options: List<T> = emptyList()

    var optionsFormat: (T) -> String = { it.toString() }

    val value: DatabindingProperty<T> = DatabindingProperty()


    fun RenderContext.render() {
        listbox {
            this@ListBox.value.value?.let {
                value.use(it)
            }

            listboxButton(
                joinClasses(
                    "p-1.5 w-full flex flex-row items-center gap-1",
                    "shadow-sm rounded-md bg-background-accent hover:bg-background-contrast"
                )
            ) {
                span("grow text-left") {
                    value.data.map { optionsFormat(it) }.renderText(into = this)
                }
                opened.render { isOpened ->
                    icon(
                        "w-4 h-4 pointer-events-none",
                        definition = if (isOpened) HeroIcons.chevron_up else HeroIcons.chevron_down
                    )
                }
            }

            listboxItems(
                joinClasses(
                    "mt-2 py-1.5 flex flex-col max-h-80 overflow-y-auto",
                    "bg-background rounded shadow-sm border border-background-accent"
                )
            ) {
                options.forEach { option ->
                    listboxItem(option, "px-3 py-1.5 flex flex-row items-center gap-2 cursor-pointer") {
                        className(selected.map {
                            if (it) "bg-primary-200 hover:bg-primary-300 dark:bg-primary-800 dark:hover:bg-primary-700"
                            else "hover:bg-secondary-200 dark:hover:bg-secondary-800"
                        })

                        span("grow text-left") {
                            +optionsFormat(option)
                        }
                        selected.render { isSelected ->
                            if (isSelected) {
                                icon("w-4 h-4", definition = HeroIcons.check)
                            }
                        }
                    }
                }
            }
        }
    }
}

fun <T> RenderContext.listBox(
    initialize: ListBox<T>.() -> Unit
) {
    with(ListBox<T>().apply(initialize)) {
        render()
    }
}