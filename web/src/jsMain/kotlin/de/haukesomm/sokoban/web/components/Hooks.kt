package de.haukesomm.sokoban.web.components

import dev.fritz2.core.RenderContext
import dev.fritz2.headless.foundation.Hook

/**
 * A hook that renders its content in the given [RenderContext].
 */
class ContentHook : Hook<RenderContext, Unit, Unit>() {

    operator fun invoke(content: RenderContext.() -> Unit) {
        value = { _, _ ->
            content()
        }
    }
}