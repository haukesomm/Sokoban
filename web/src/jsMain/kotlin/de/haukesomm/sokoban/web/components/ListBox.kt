package de.haukesomm.sokoban.web.components

import de.haukesomm.sokoban.web.components.icons.HeroIcons
import de.haukesomm.sokoban.web.components.icons.icon
import dev.fritz2.core.RenderContext
import dev.fritz2.core.transition
import dev.fritz2.headless.components.listbox
import dev.fritz2.headless.foundation.DatabindingProperty
import dev.fritz2.headless.foundation.utils.floatingui.utils.PlacementValues
import kotlinx.coroutines.flow.map

class ListBox<T> {
    var entries: List<T> = emptyList()
    var format: (T) -> String = { it.toString() }

    val value: DatabindingProperty<T> = DatabindingProperty()

    fun RenderContext.render() {
        listbox<T> {
            this@ListBox.value.value?.let { value.use(it) }

            listboxButton(
                """flex items-center space-between w-full py-1.5 px-2 gap-2
                    | rounded cursor-default
                    | border border-primary-500 dark:border-primary-600 
                    | font-sans text-sm text-left
                    | hover:border-primary-700 dark:hover:border-primary-400
                    | focus-visible:outline-none focus-visible:ring-2 
                    | focus-visible:ring-primary-600
                    | focus-visible:border-primary-700""".trimMargin()
            ) {
                span("block truncate w-full") {
                    value.data.map { format(it) }.render { +it }
                }
                icon("w-4 h-4", definition = HeroIcons.chevron_down)
            }

            listboxItems(
                """flex flex-col min-w-max max-w-md max-h-80 py-1 overflow-auto origin-top  
                    | bg-background-lightest dark:bg-background-darkest rounded shadow-md divide-y 
                    | divide-gray-100 dark:divide-gray-700
                    | ring-1 ring-primary-600 ring-opacity-5 
                    | focus:outline-none""".trimMargin(),
                tag = RenderContext::ul
            ) {
                placement = PlacementValues.bottomStart

                transition(
                    opened,
                    "transition duration-100 ease-out",
                    "opacity-0 scale-95",
                    "opacity-100 scale-100",
                    "transition duration-100 ease-in",
                    "opacity-100 scale-100",
                    "opacity-0 scale-95"
                )

                entries.forEach { entry ->
                    listboxItem(
                        entry,
                        """w-full relative h-8 px-2 py-1.5 flex flex-row items-center gap-2
                            | cursor-default select-none disabled:opacity-50
                            | text-sm
                            | hover:bg-primary-500 hover:text-white hover:dark:bg-primary-600 hover:dark:text-white
                            | """.trimMargin(),
                        tag = RenderContext::li
                    ) {
                        className(selected.map {
                            if (it) "font-semibold"
                            else ""
                        })

                        className(active.map {
                            if (it) "bg-primary-500 text-white dark:bg-primary-600 dark:text-white"
                            else ""
                        })

                        selected.render {
                            if (it) icon("w-4 h-4", definition = HeroIcons.check)
                        }

                        span("absolute left-8") {
                            +format(entry)
                        }
                    }
                }
            }
        }
    }
}

fun <T> RenderContext.listBox(initialize: ListBox<T>.() -> Unit) {
    ListBox<T>().run {
        initialize()
        render()
    }
}