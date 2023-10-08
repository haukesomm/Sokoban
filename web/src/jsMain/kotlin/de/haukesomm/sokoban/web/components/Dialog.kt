package de.haukesomm.sokoban.web.components

import dev.fritz2.core.Handler
import dev.fritz2.core.RenderContext
import dev.fritz2.core.classes
import dev.fritz2.core.storeOf
import dev.fritz2.headless.foundation.Hook
import dev.fritz2.headless.foundation.hook
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

enum class DialogAction {
    Close,
    Dismiss
}

data class DialogResult<R>(
    val action: DialogAction,
    val result: R?
)

class DialogContentContext<T, R>(
    context: RenderContext,
    val payload: T,
    val exportResult: Handler<R>
) : RenderContext by context

class DialogContentHook<T, R> : Hook<DialogContentContext<T, R>, Unit, Unit>() {

    operator fun invoke(content: DialogContentContext<T, R>.() -> Unit) {
        value = { _, _ ->
            content()
        }
    }
}

class DialogContext<T, R> {

    val title: TextProperty = TextProperty()

    val content: DialogContentHook<T, R> = DialogContentHook()

    val closeButtonText: TextProperty = TextProperty("Close")

    val dismissButtonText: TextProperty = TextProperty()
}


private fun RenderContext.dialogButton(text: String, primary: Boolean = false) =
    button(
        classes(
            """p-1 flex flex-row items-center gap-2 rounded-sm font-semibold focus:outline-none
                | focus-visible:ring-2 focus-visible:ring-primary-500 focus-visible:dark:ring-primary-600
                | disabled:text-neutral-dark-disabled
                | disabled:dark:text-neutral-light-disabled
            """.trimMargin(),
            "text-primary-500 dark:text-primary-600".takeIf { primary },
            "text-neutral-dark-secondary dark:text-neutral-light-secondary".takeIf { !primary }
        )
    ) {
        +text
    }

fun <T, R> dialog(
    payloads: Flow<T>,
    initialize: DialogContext<T, R>.() -> Unit
): Flow<DialogResult<R>> =
    modal(payloads) {
        content {
            val exportStore = storeOf<R?>(null)

            DialogContext<T, R>().run dialogContext@{
                initialize()

                div("flex flex-col bg-background-light dark:bg-background-dark") {
                   title.value?.render { title ->
                        div(
                            """px-4 pt-4 pb-2 bg-background-lightest dark:bg-background-darkest 
                            | border-b border-neutral-light dark:border-neutral-dark
                        """.trimMargin()
                        ) {
                            p("font-semibold") {
                                +title
                            }
                        }
                    }

                    DialogContentContext<T, R>(
                        context = this,
                        payload = payload,
                        exportResult = exportStore.handle { _, export -> export }
                    ).run {
                        hook(this@dialogContext.content)
                    }

                    div(
                        """px-3 py-2 flex flex-row-reverse gap-4
                        | bg-background-lightest dark:bg-background-darkest 
                        | border-t border-neutral-light dark:border-neutral-dark
                    """.trimMargin()
                    ) {
                        closeButtonText.value?.render {
                            dialogButton(it, primary = true).clicks
                                .flatMapLatest { exportStore.data }
                                .map { result -> DialogResult(DialogAction.Close, result) } handledBy close
                        }

                        dismissButtonText.value?.render {
                            dialogButton(it).clicks
                                .map { DialogResult<R>(DialogAction.Dismiss, null) } handledBy close
                        }
                    }
                }
            }
        }
    }