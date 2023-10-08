package de.haukesomm.sokoban.web.components

import dev.fritz2.core.*
import dev.fritz2.headless.components.modal as headlessModal
import dev.fritz2.headless.foundation.Hook
import dev.fritz2.headless.foundation.hook
import kotlinx.coroutines.flow.*

class ModalContentContext<T, R>(
    context: RenderContext,
    val payload: T,
    val close: Handler<R>,
) : RenderContext by context

class ModalContentHook<T, R> : Hook<ModalContentContext<T, R>, Unit, Unit>() {

    operator fun invoke(content: ModalContentContext<T, R>.() -> Unit) {
        value = { _, _ ->
            content()
        }
    }
}

class Modal<T, R>(private val payloads: Flow<T>) {

    private val openStore = object : RootStore<Boolean>(false) {

        val close = handleAndEmit<R, R> { _, result ->
            emit(result)
            false
        }
    }


    val content: ModalContentHook<T, R> = ModalContentHook()

    internal val results: Flow<R> = openStore.close


    init {
        payloads.map { true } handledBy openStore.update
    }


    fun render() {
        headlessModal {
            openState(openStore)
            modalPanel("fixed inset-0 w-full z-20") {
                modalOverlay("fixed inset-0 bg-gray-300 dark:bg-gray-800 opacity-75") {
                }
                div("fixed inset-0 flex flex-row justify-center items-center") {
                    div(
                        """absolute m-4 rounded-lg overflow-hidden 
                            | shadow-lg text-sm text-neutral-dark dark:text-neutral-light
                            | bg-background-light dark:bg-background-dark
                        """.trimMargin()
                    ) {
                        payloads.render { payload ->
                            ModalContentContext(
                                context = this,
                                payload = payload,
                                close = openStore.close,
                            ).run {
                                hook(content)
                            }
                        }
                    }
                }
            }
        }
    }
}

fun <T, R> modal(
    payloads: Flow<T>,
    initialize: Modal<T, R>.() -> Unit
): Flow<R> =
    flow {
        Modal<T, R>(payloads).run {
            initialize()
            render()
            results.collect { emit(it) }
        }
    }
