package de.haukesomm.sokoban.web.components

import dev.fritz2.headless.foundation.Property
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FlowProperty<T> : Property<Flow<T>>() {

    operator fun invoke(value: Flow<T>) {
        this.value = value
    }

    operator fun invoke(value: T) {
        this.value = flowOf(value)
    }
}