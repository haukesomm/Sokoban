package de.haukesomm.sokoban.web.components.game

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.web.components.icons.HeroIcons
import de.haukesomm.sokoban.web.components.icons.icon
import dev.fritz2.core.RenderContext
import dev.fritz2.core.Tag
import dev.fritz2.core.joinClasses
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLElement

class MoveButtons {

    companion object {
        private val moveEventToIcon = mapOf(
            Direction.Top to HeroIcons.arrow_up,
            Direction.Bottom to HeroIcons.arrow_down,
            Direction.Left to HeroIcons.arrow_left,
            Direction.Right to HeroIcons.arrow_right
        )
    }

    private val mutableMoveEventsFlow: MutableSharedFlow<Direction> = MutableSharedFlow()
    val moveEvents: Flow<Direction> = mutableMoveEventsFlow.asSharedFlow()

    private fun RenderContext.renderButton(direction: Direction) {
        button(
            joinClasses(
                "w-12 h-12 flex justify-center items-center",
                "bg-background border border-background-accent rounded-md shadow-sm",
                "touch-manipulation"
            )
        ) {
            moveEventToIcon[direction]?.let {
                icon("w-6 h-6", definition = it)
            }
        }.clicks.map { direction } handledBy {
            mutableMoveEventsFlow.emit(it)
        }
    }

    fun RenderContext.render() {
        div("grid grid-cols-3 gap-2") {
            div {}
            renderButton(Direction.Top)
            div {}
            renderButton(Direction.Left)
            div {}
            renderButton(Direction.Right)
            div {}
            renderButton(Direction.Bottom)
            div {}
        }
    }
}

fun RenderContext.moveButtons(): Flow<Direction> {
    return with(MoveButtons()) {
        render()
        moveEvents
    }
}