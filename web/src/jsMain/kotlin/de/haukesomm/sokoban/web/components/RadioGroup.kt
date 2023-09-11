package de.haukesomm.sokoban.web.components

import de.haukesomm.sokoban.web.components.icons.HeroIcons
import de.haukesomm.sokoban.web.components.icons.icon
import dev.fritz2.core.RenderContext
import dev.fritz2.headless.components.radioGroup
import dev.fritz2.headless.foundation.DatabindingProperty
import kotlinx.coroutines.flow.map

class RadioGroup<T> {

    val value: DatabindingProperty<T> = DatabindingProperty()

    var title: String? = null
    var options: List<T> = emptyList()
    var optionsFormat: (T) -> String = { it.toString() }
    var optionDescriptionFormat: ((T) -> String?)? = null

    fun RenderContext.render() {
        radioGroup<T> {
            value.use(this@RadioGroup.value.value!!)

            div("flex flex-col gap-4") {
                title?.let {
                    radioGroupLabel("text-md font-medium dark:text-gray-300") {
                        +it
                    }
                }
                div("space-y-4") {
                    options.forEach { option ->
                        radioGroupOption(option) {
                            radioGroupOptionToggle("group grid grid-cols-[min-content_auto] gap-2 focus:outline-none") {
                                div(
                                    """w-4 h-4 mt-1 flex justify-center items-center rounded-full
                                        | group-focus-visible:ring-2 
                                        | group-focus-visible:ring-offset-2
                                        | group-focus-visible:ring-offset-background-light
                                        | group-focus-visible:dark:ring-offset-background-dark
                                        | group-focus-visible:ring-primary-500
                                        | group-focus-visible:dark:ring-primary-600""".trimMargin()
                                ) {
                                    className(selected.map {
                                        if (it) "bg-primary-500 dark:bg-primary-600"
                                        else "border border-neutral-dark-secondary dark:border-neutral-light-secondary"
                                    })
                                    div {
                                        className(selected.map { if (it) "" else "!hidden" })
                                        div("w-2 h-2 rounded-full bg-white") {}
                                    }
                                }
                                div("flex flex-col") {
                                    radioGroupOptionLabel("text-sm font-semibold") {
                                        +optionsFormat(option)
                                    }
                                    optionDescriptionFormat?.let { format ->
                                        radioGroupOptionDescription(
                                            """text-xs text-neutral-dark-secondary 
                                                | dark:text-neutral-light-secondary""".trimMargin()
                                        ) {
                                            format(option)?.let { +it }
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

fun <T> RenderContext.radioGroup(
    initialize: RadioGroup<T>.() -> Unit = {}
) = RadioGroup<T>().run {
    initialize()
    render()
}