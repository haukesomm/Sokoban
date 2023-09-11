package de.haukesomm.sokoban.web.components

import de.haukesomm.sokoban.web.components.icons.HeroIcons
import de.haukesomm.sokoban.web.components.icons.icon
import dev.fritz2.core.RenderContext
import dev.fritz2.core.storeOf
import dev.fritz2.core.transition
import dev.fritz2.headless.components.disclosure
import dev.fritz2.headless.foundation.hook

class Disclosure {

    val title = TextProperty()

    val content = ContentHook()

    fun RenderContext.render() {
        val openState = storeOf(false)
        disclosure {
            openState(openState)
            disclosureButton {
                div("flex flex-row gap-2 items-center") {
                    opened.render {
                        if (it) icon("w-4 h-4", definition = HeroIcons.chevron_up)
                        else icon("w-4 h-4", definition = HeroIcons.chevron_down)
                    }
                    title.value?.render {
                        span { +it }
                    }
                }
            }
            disclosurePanel {
                div("mt-4") {
                    hook(content)
                }
            }
        }
    }
}

fun RenderContext.disclosure(initialize: Disclosure.() -> Unit) =
    Disclosure().apply(initialize).run { render() }