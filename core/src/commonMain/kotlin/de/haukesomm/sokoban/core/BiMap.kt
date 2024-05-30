package de.haukesomm.sokoban.core

/**
 * This interface represents a bijective map.
 *
 * The inverse map can be accessed via the [inverse] property.
 */
interface BiMap<K, V> : Map<K, V> {

    /**
     * The inverse of the [map].
     */
    val inverse: BiMap<V, K>
}

/**
 * This class implements a [BiMap].
 *
 * The provided [map] _should not_ contain duplicate keys as they will get lost when the map's inverse is computed.
 * In case a key is provided multiple times, the last one will be kept.
 */
internal class BiMapImpl<K, V>(val map: Map<K, V>) : Map<K, V> by map, BiMap<K, V> {

    override val inverse: BiMap<V, K>
        get() = map.entries
            .associate { (k, v) -> v to k }
            .let(::BiMapImpl)
}

/**
 * Creates an _immutable_ [BiMap] from the given [entries].
 *
 * The provided [map] _should not_ contain duplicate keys as they will get lost when the map's inverse is computed.
 * In case a key is provided multiple times, the last one will be kept.
 */
fun biMapOf(vararg entries: Pair<Char, TileProperties>): BiMap<Char, TileProperties> =
    BiMapImpl(
        mapOf(*entries)
    )