package de.haukesomm.sokoban.web

import dev.fritz2.core.Lens
import dev.fritz2.core.Store

internal fun <T> mapToNullableLens(placeholder: T): Lens<T, T?> = object : Lens<T, T?> {
    override val id: String = ""
    override fun get(parent: T): T? = parent.takeUnless { parent == placeholder }
    override fun set(parent: T, value: T?): T = value ?: placeholder
}

/**
 * Backported function from fritz2 1.0-SNAPSHOT that maps a store to a nullable store.
 *
 * TODO Remove this function when fritz2-1.0-RC20 is released.
 */
fun <T> Store<T>.mapNullableHelper(placeholder: T): Store<T?> =
    map(mapToNullableLens(placeholder))