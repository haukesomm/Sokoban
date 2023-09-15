package de.haukesomm.sokoban.core

/**
 * This interface represents a bijective map.
 *
 * The inverse map can be accessed via the [inverse] property.
 */
interface BiMap<K, V> : Map<K, V> {

    /**
     * The inverse of the [map].
     *
     * @throws IllegalStateException if the [map] is not bijective.
     */
    val inverse: BiMap<V, K>
}

/**
 * This class implements a [BiMap].
 *
 * The provided [map] _must be bijective_, meaning that every key must be mapped to exactly one value and vice versa.
 * Otherwise, an [IllegalStateException] will be thrown when trying to access the [inverse] map.
 */
class BiMapImpl<K, V>(val map: Map<K, V>) : Map<K, V> by map, BiMap<K, V> {

    override val inverse: BiMap<V, K>
        get() = map.entries
            .associate { (k, v) -> v to k }
            .let { inverse ->
                if (inverse.size == map.size) BiMapImpl(inverse)
                else throw IllegalStateException("Map is not bijective!")
            }
}