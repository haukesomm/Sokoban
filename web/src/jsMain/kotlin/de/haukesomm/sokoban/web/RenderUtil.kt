package de.haukesomm.sokoban.web

import dev.fritz2.core.RenderContext
import dev.fritz2.core.render
import dev.fritz2.headless.foundation.portalRoot

/**
 * Convenience function to render an app's ui with a portal root.
 *
 * In vanilla fritz2, the portal root has to be rendered manually.
 */
// TODO: Remove, once implemented as part of fritz2
fun renderWithPortalRoot(content: RenderContext.() -> Unit) =
    render {
        content()
        portalRoot()
    }