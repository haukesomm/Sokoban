package de.haukesomm.sokoban.web.components

import dev.fritz2.core.RenderContext
import dev.fritz2.core.Tag
import dev.fritz2.core.tabIndex
import dev.fritz2.headless.components.switchWithLabel
import dev.fritz2.headless.foundation.Aria
import dev.fritz2.headless.foundation.DatabindingProperty
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLDivElement

class Switch {
    val data: DatabindingProperty<Boolean> = DatabindingProperty()

    var label: String? = null

    fun RenderContext.render() {
        div("max-w-sm") {
            switchWithLabel("flex items-center gap-2") {
                this@Switch.data.value?.let { this@switchWithLabel.value.use(it) }

                switchToggle(
                    """relative inline-flex flex-shrink-0 h-4 w-8
                        | cursor-pointer rounded-full
                        | border-2 border-transparent ring-2
                        | transition-colors ease-in-out duration-200 
                        | focus-visible:outline-none
                        | focus-visible:ring-offset-2 
                        | focus-visible:ring-offset-gray-100 focus-visible:dark:ring-offset-darkgray-500 
                        | focus-visible:ring-primary-500""".trimMargin()
                ) {
                    className(enabled.map {
                        if (it) "bg-primary-400 ring-primary-600 dark:bg-primary-700 dark:ring-primary-600"
                        else "bg-primary-300 ring-primary-500 dark:bg-primary-500 dark:ring-primary-400"
                    })
                    span(
                        """inline-block h-3 w-3 
                            | rounded-full bg-white shadow pointer-events-none 
                            | ring-0 
                            | transform transition ease-in-out duration-200
                            """.trimMargin()
                    ) {
                        className(enabled.map { if (it) "translate-x-4" else "translate-x-0" })
                        attr(Aria.hidden, "true")
                    }
                }

                label?.let {
                    switchLabel(
                        "block text-sm font-medium text-gray-700 dark:text-gray-200",
                        tag = RenderContext::span
                    ) {
                        +it
                    }
                }
            }
        }
    }
}

fun RenderContext.switch(
    id: String? = null,
    classes: String? = null,
    initialize: Switch.() -> Unit = {}
): Tag<HTMLDivElement> =
    div(id, classes) {
        Switch().run {
            initialize()
            render()
        }
    }