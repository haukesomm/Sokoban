package de.haukesomm.sokoban.core

/**
 * This class represents a bijective map.
 *
 * The provided [map] _must be bijective_, meaning that every key must be mapped to exactly one value and vice versa.
 * Otherwise, an [IllegalStateException] will be thrown when trying to access the [inverse] map.
 */
class BiMap<K, V>(val map: Map<K, V>) : Map<K, V> by map {

    /**
     * The inverse of the [map].
     *
     * @throws IllegalStateException if the [map] is not bijective.
     */
    val inverse: BiMap<V, K>
        get() = map.entries.associate { (k, v) -> v to k }.let { inverse ->
            if (inverse.size == map.size) BiMap(inverse)
            else throw IllegalStateException("Map is not bijective!")
        }
}