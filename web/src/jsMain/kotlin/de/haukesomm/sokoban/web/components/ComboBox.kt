package de.haukesomm.sokoban.web.components

import de.haukesomm.sokoban.web.components.icons.HeroIcons
import de.haukesomm.sokoban.web.components.icons.icon
import dev.fritz2.core.RenderContext
import dev.fritz2.core.joinClasses
import dev.fritz2.headless.components.Combobox
import dev.fritz2.headless.components.combobox
import dev.fritz2.headless.foundation.DatabindingProperty
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlin.math.truncate

class ComboBox<T> {

    var options: List<T> = emptyList()

    var optionsFormat: (T) -> String = { it.toString() }
    
    var filterBy: T.() -> String = { optionsFormat(this) }

    val value: DatabindingProperty<T?> = DatabindingProperty()


    fun RenderContext.render() {
        combobox<T> headlessCombobox@{
            this@ComboBox.value.value?.let {
                this@headlessCombobox.value.use(it)
            }

            items(options)
            itemFormat = optionsFormat

            filterBy(this@ComboBox.filterBy)

            selectionStrategy.autoSelectMatch()
            maximumDisplayedItems = 100

            comboboxPanelReference(
                joinClasses(
                    "w-full px-1.5 flex flex-row items-center gap-1",
                    "rounded-md bg-background-accent hover:bg-background-contrast",
                    "border-invisible focus-within:ring-1 focus-within:ring-primary-400",
                    "dark:focus-within:ring-primary-800"
                )
            ) {
                comboboxInput(
                    joinClasses(
                        "w-full h-min p-1.5 border-0 outline-0 focus:ring-transparent focus:shadow-none bg-transparent",
                        "text-sm"
                    )
                ) {
                }
                opened.render { isOpened ->
                    button {
                        icon(
                            "w-4 h-4 pointer-events-none",
                            definition = if (isOpened) HeroIcons.chevron_up else HeroIcons.chevron_down
                        )
                    }.clicks { stopPropagation() } handledBy toggle
                }
            }

            comboboxItems(
                joinClasses(
                    "mt-2 py-1.5 flex flex-col max-h-80 overflow-y-auto",
                    "bg-background rounded shadow-sm border border-background-accent"
                )
            ) {
                results.render(into = this) { (_, items, _) ->
                    items.forEach { item ->
                        comboboxItem(item, "px-3 py-1.5 flex flex-row items-center gap-2 cursor-pointer") {
                            className(
                                combine(active, selected) { active, selected ->
                                    if (selected) {
                                        if (active) "bg-primary-300 dark:bg-primary-700"
                                        else "bg-primary-200 dark:bg-primary-800"
                                    } else {
                                        if (active) "bg-secondary-200 dark:bg-secondary-800"
                                        else ""
                                    }
                                }
                            )

                            span("grow text-left") {
                                +optionsFormat(item.value)
                            }

                            selected.render { isSelected ->
                                if (isSelected) {
                                    icon("w-4 h-4", definition = HeroIcons.check)
                                }
                            }
                        }
                    }
                    if (items.isEmpty()) {
                        div("px-3 py-1.5 flex flex-row items-center opacity-50") {
                            +"No level found"
                        }
                    }
                }
            }
        }
    }
}

fun <T> RenderContext.comboBox(
    initialize: ComboBox<T>.() -> Unit
) {
    with(ComboBox<T>().apply(initialize)) {
        render()
    }
}