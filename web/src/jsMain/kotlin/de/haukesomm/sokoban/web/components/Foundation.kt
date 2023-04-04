package de.haukesomm.sokoban.web.components

import dev.fritz2.core.RenderContext
import dev.fritz2.headless.foundation.Hook

class ContentHook : Hook<RenderContext, Unit, Unit>() {
    operator fun invoke(content: RenderContext.() -> Unit) {
        value = { _, _ ->
            content()
        }
    }
}