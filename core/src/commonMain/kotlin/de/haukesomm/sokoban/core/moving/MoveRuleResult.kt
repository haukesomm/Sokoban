package de.haukesomm.sokoban.core.moving

/**
 * Represents the result of a [MoveRule].
 *
 * The result contains a [status] and an optional [message]. The [message] can be used to provide additional
 * information about the result for debugging purposes.
 *
 * Instances of this class should primarily be created using the [possible], [boxAheadNeedsToMove] and [impossible]
 * factory methods defined in the companion object.
 */
data class MoveRuleResult(
    val status: Status,
    val message: String? = null
) {
    enum class Status {
        Possible,
        BoxAheadNeedsToMove,
        Impossible
    }

    companion object {

        /**
         * Creates a new [MoveRuleResult] with the status [Status.Possible].
         */
        fun possible(): MoveRuleResult =
            MoveRuleResult(Status.Possible)

        /**
         * Creates a new [MoveRuleResult] with the status [Status.BoxAheadNeedsToMove].
         */
        fun boxAheadNeedsToMove(): MoveRuleResult =
            MoveRuleResult(Status.BoxAheadNeedsToMove)

        /**
         * Creates a new [MoveRuleResult] with the status [Status.Impossible] and an optional [message].
         */
        fun impossible(message: String? = null): MoveRuleResult =
            MoveRuleResult(Status.Impossible, message)
    }
}