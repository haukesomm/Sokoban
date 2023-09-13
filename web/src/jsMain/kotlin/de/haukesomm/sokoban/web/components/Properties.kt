package de.haukesomm.sokoban.web.components

import de.haukesomm.sokoban.web.components.icons.IconDefinition
import dev.fritz2.core.RenderContext
import dev.fritz2.headless.foundation.Hook
import dev.fritz2.headless.foundation.Property
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * A property that holds a [Flow] of a certain type.
 *
 * This is a workaround for the fact that [Property] does not support [Flow]s.
 */
class FlowProperty<T> : Property<Flow<T>>() {

    operator fun invoke(value: Flow<T>) {
        this.value = value
    }

    operator fun invoke(value: T) {
        this.value = flowOf(value)
    }
}

/**
 * A property that holds a [Flow] of [String]s.
 */
typealias TextProperty = FlowProperty<String>

/**
 * A property that holds a [Flow] of [IconDefinition]s.
 */
typealias IconProperty = FlowProperty<IconDefinition>

/**
 * A property that holds a [Flow] of [Boolean]s.
 */
typealias BooleanProperty = FlowProperty<Boolean>

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