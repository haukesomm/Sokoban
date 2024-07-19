package de.haukesomm.sokoban.web.components

import dev.fritz2.core.RenderContext
import dev.fritz2.core.storeOf
import dev.fritz2.headless.components.modal
import dev.fritz2.headless.foundation.Hook
import dev.fritz2.headless.foundation.hook
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.plus

class Dialog<P, R>(context: RenderContext, payloads: Flow<P>) : RenderContext by context {

    private val payloads = payloads.shareIn(MainScope() + job, SharingStarted.Lazily, replay = 1)

    private val results = MutableSharedFlow<R>(replay = 1)


    inner class ContentContext(
        context: RenderContext,
        val payload: P,
        val close: (R) -> Unit
    ) : RenderContext by context

    inner class ContentHook : Hook<ContentContext, Unit, Unit>() {
        operator fun invoke(content: ContentContext.() -> Unit) {
            value = { _, _ ->
                content()
            }
        }
    }

    val content: ContentHook = ContentHook()


    fun render(): Flow<R> {
        modal {
            openState(storeOf(false))
            payloads.map { } handledBy open
            results.map { } handledBy close

            modalPanel {
                modalOverlay("fixed inset-0 bg-background opacity-80") {  }

                div("fixed inset-0 z-10 flex justify-center items-center") {
                    div("p-4 bg-background text-foreground border border-background-accent rounded-md shadow-md") {
                        payloads.render { payload ->
                            with(ContentContext(context = this, payload, close = results::tryEmit)) {
                                hook(content)
                            }
                        }
                    }
                }
            }
        }

        return results.asSharedFlow()
    }
}

fun <P, R> RenderContext.dialog(
    payloads: Flow<P>,
    initialize: Dialog<P, R>.() -> Unit
): Flow<R> =
    with(Dialog<P, R>(this, payloads)) {
        initialize()
        render()
    }