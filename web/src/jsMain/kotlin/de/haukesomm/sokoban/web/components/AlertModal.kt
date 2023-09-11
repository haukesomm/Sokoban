package de.haukesomm.sokoban.web.components

import de.haukesomm.sokoban.web.components.icons.HeroIcons
import de.haukesomm.sokoban.web.components.icons.icon
import dev.fritz2.core.Handler
import dev.fritz2.core.RenderContext
import dev.fritz2.core.storeOf
import dev.fritz2.core.type
import dev.fritz2.headless.components.modal
import dev.fritz2.headless.foundation.hook

class AlertModal {
    private val openStore = storeOf(false)
    private val open: Handler<Unit> = openStore.handle { true }

    val content: ContentHook = ContentHook()

    fun RenderContext.render(): Handler<Unit> {
        modal {
            openState(openStore)
            modalPanel("fixed inset-0 w-full z-20") {
                modalOverlay("fixed inset-0 bg-gray-300 dark:bg-gray-800 opacity-75") {
                }
                div("fixed inset-0 flex flex-row justify-center") {
                    div(
                        """absolute p-4 mt-12 flex flex-row items-start gap-4 rounded-lg 
                            | text-sm text-neutral-dark dark:text-neutral-light
                            | bg-background-light dark:bg-background-dark""".trimMargin()
                    ) {
                        div {
                            hook(content)
                        }
                        div {
                            button("") {
                                type("icon")
                                icon("w-5 h-5", definition = HeroIcons.x)
                            }.clicks handledBy close
                        }
                    }
                }
            }
        }
        return open
    }
}

fun RenderContext.alertModal(
    initialize: AlertModal.() -> Unit
): Handler<Unit> =
    AlertModal().run {
        initialize()
        render()
    }