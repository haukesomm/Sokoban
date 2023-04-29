package de.haukesomm.sokoban.web.components

import de.haukesomm.sokoban.web.components.icons.HeroIcons
import de.haukesomm.sokoban.web.components.icons.icon
import dev.fritz2.core.RenderContext
import dev.fritz2.headless.components.checkboxGroup
import dev.fritz2.headless.foundation.DatabindingProperty
import kotlinx.coroutines.flow.map

class CheckboxGroup<T> {
    val values: DatabindingProperty<List<T>> = DatabindingProperty()

    var title: String? = null
    var options: List<T> = emptyList()
    var optionsFormat: (T) -> String = { it.toString() }
    var optionDescriptionFormat: ((T) -> String)? = null

    fun RenderContext.render() {
        checkboxGroup {
            value.use(this@CheckboxGroup.values.value!!)

            div("flex flex-col gap-4") {
                title?.let {
                    checkboxGroupLabel("text-md font-semibold dark:text-gray-300") {
                        +it
                    }
                }
                div("space-y-4") {
                    options.forEach { option ->
                        checkboxGroupOption(option) {
                            checkboxGroupOptionToggle("grid grid-cols-[min-content_auto] gap-2") {
                                div("w-4 h-4 mt-1 flex justify-center items-center rounded-sm") {
                                    className(selected.map {
                                        if (it) "bg-primary-500"
                                        else "border border-gray-300 dark:border-gray-600"
                                    })
                                    div {
                                        className(selected.map { if (it) "" else "!hidden" })
                                        icon("w-4 h-4 text-white", definition = HeroIcons.check)
                                    }
                                }
                                div("flex flex-col dark:text-gray-300") {
                                    checkboxGroupOptionLabel("text-sm font-semibold") {
                                        +optionsFormat(option)
                                    }
                                    optionDescriptionFormat?.let { format ->
                                        checkboxGroupOptionDescription("text-xs") {
                                            +format(option)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun <T> RenderContext.checkboxGroup(initialize: CheckboxGroup<T>.() -> Unit) {
    CheckboxGroup<T>().run {
        initialize()
        render()
    }
}