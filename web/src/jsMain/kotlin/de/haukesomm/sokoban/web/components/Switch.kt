package de.haukesomm.sokoban.web.components

import dev.fritz2.core.RenderContext
import dev.fritz2.core.Tag
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
            switchWithLabel("flex items-center justify-between") {
                this@Switch.data.value?.let { this@switchWithLabel.value.use(it) }

                span("flex-grow flex flex-col") {
                    label?.let {
                        switchLabel(
                            "block mr-2 text-sm font-medium text-gray-700 dark:text-gray-200",
                            tag = RenderContext::span
                        ) {
                            +it
                        }
                    }
                    /*switchDescription("mt-1 text-xs text-primary-700", tag = RenderContext::span) {
                        +"The web's favourite utility-first CSS framework"
                    }*/
                }

                switchToggle(
                    """relative inline-flex flex-shrink-0 h-6 w-11
                        | cursor-pointer rounded-full
                        | border-2 border-transparent ring-1 ring-primary-400  
                        | transition-colors ease-in-out duration-200 
                        | focus:outline-none focus:ring-2 focus:ring-primary-500
                    """.trimMargin()
                ) {
                    className(enabled.map { if (it) "bg-primary-700" else "bg-primary-300" })
                    span("sr-only") { +"Use setting" }
                    span(
                        """inline-block h-5 w-5 
                            | rounded-full bg-white shadow pointer-events-none 
                            | ring-0 
                            | transform transition ease-in-out duration-200
                            """.trimMargin()
                    ) {
                        className(enabled.map { if (it) "translate-x-5" else "translate-x-0" })
                        attr(Aria.hidden, "true")
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