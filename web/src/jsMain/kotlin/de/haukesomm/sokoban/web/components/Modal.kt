package de.haukesomm.sokoban.web.components

import dev.fritz2.core.*
import dev.fritz2.headless.components.modal as headlessModal
import dev.fritz2.headless.foundation.Hook
import dev.fritz2.headless.foundation.hook
import kotlinx.coroutines.flow.*

enum class ModalAction {
    Close,
    Dismiss
}

data class ModalResult<R>(
    val action: ModalAction,
    val result: R?
)

class ModalContentContext<T, R>(
    context: RenderContext,
    val payload: T,
    val exportResult: Handler<R>,
    val close: Handler<ModalAction>,
) : RenderContext by context

class ModalContentHook<T, R> : Hook<ModalContentContext<T, R>, Unit, Unit>() {

    operator fun invoke(content: ModalContentContext<T, R>.() -> Unit) {
        value = { _, _ ->
            content()
        }
    }
}

class Modal<T, R>(context: RenderContext, payloads: Flow<T>) : RenderContext by context {

    private val payloadStore = object : RootStore<T?>(initialData = null, job = job) {

        val receive = handleAndEmit<T, T> { _, payload ->
            emit(payload)
            payload
        }
    }

    private val exportStore = storeOf<R?>(null)

    private val openStore = object : RootStore<Boolean>(initialData = false, job = job) {

        val open = handle { true }

        val close = handleAndEmit<ModalAction, ModalAction> { _, action ->
            emit(action)
            false
        }
    }

    init {
        payloads handledBy payloadStore.receive
        payloadStore.receive.map {} handledBy openStore.open
        openStore.close.map { null } handledBy payloadStore.update
    }


    val title: FlowProperty<String> = FlowProperty<String>()

    val content: ModalContentHook<T & Any, R> = ModalContentHook()

    var closeButtonText: String = "Close"

    var dismissButtonText: String? = null


    internal val results: Flow<ModalResult<R>> =
        combine(openStore.close, exportStore.data) { action, result ->
            ModalResult(action, result)
        }


    private fun RenderContext.actionButton(text: String, primary: Boolean = false) =
        button(
            classes(
                "p-1 flex flex-row items-center gap-2 rounded-md text-base font-semibold focus-visible:outline-none",
                """text-primary-500 dark:text-primary-600
                    | focus-visible:bg-primary-100 focus-visible:dark:bg-primary-900
                """.trimMargin().takeIf { primary },
                """text-neutral-dark-secondary dark:text-neutral-light-secondary
                    | focus-visible:bg-background-light focus-visible:dark:bg-background-darkest
                """.trimMargin().takeIf { !primary }
            )
        ) {
            type("button")
            +text
        }

    fun render() {
        headlessModal {
            openState(openStore)
            modalPanel("fixed inset-0 w-full z-20") {
                modalOverlay("fixed inset-0 bg-gray-300 dark:bg-gray-800 opacity-75") {
                }
                div("fixed inset-0 flex flex-row justify-center items-center") {
                    div(
                        """absolute max-h-full md:m-4 rounded-lg overflow-hidden flex flex-col
                            | shadow-lg text-sm text-neutral-dark dark:text-neutral-light
                            | bg-background-light dark:bg-background-darkest
                        """.trimMargin()
                    ) {
                        title.value?.render { title ->
                            div(
                                """px-4 py-3 bg-background-lightest dark:bg-background-dark 
                                    | border-b border-neutral-light dark:border-neutral-dark
                                """.trimMargin()
                            ) {
                                p("text-lg font-semibold") {
                                    +title
                                }
                            }
                        }

                        div("flex-shrink overflow-auto") {
                            ModalContentContext(
                                context = this,
                                payload = payloadStore.current!!,
                                exportResult = exportStore.handle<R> { _, export -> export },
                                close = openStore.close
                            ).run {
                                hook(content)
                            }
                        }

                        div(
                            """px-3 py-2 flex flex-row-reverse gap-4
                                | bg-background-lightest dark:bg-background-dark
                                | border-t border-neutral-light dark:border-neutral-dark
                            """.trimMargin()
                        ) {
                            actionButton(closeButtonText, primary = true).clicks
                                .map { ModalAction.Close } handledBy openStore.close

                            dismissButtonText?.let {
                                actionButton(it).clicks
                                    .map { ModalAction.Dismiss } handledBy openStore.close
                            }
                        }
                    }
                }
            }
        }
    }
}

fun <T, R> RenderContext.modal(
    payloads: Flow<T>,
    initialize: Modal<T, R>.() -> Unit
): Flow<ModalResult<R>> =
    flow {
        Modal<T, R>(this@modal, payloads).run {
            initialize()
            render()
            results.collect { emit(it) }
        }
    }

fun RenderContext.modal(
    payloads: Flow<Unit>,
    initialize: Modal<Unit, Unit>.() -> Unit
): Flow<ModalResult<Unit>> =
    modal<Unit, Unit>(payloads, initialize)
