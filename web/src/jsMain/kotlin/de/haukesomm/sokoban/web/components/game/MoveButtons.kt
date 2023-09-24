package de.haukesomm.sokoban.web.components.game

import de.haukesomm.sokoban.web.components.icons.HeroIcons
import de.haukesomm.sokoban.web.components.icons.icon
import dev.fritz2.core.RenderContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map

enum class MoveEvent {
    Up, Down, Left, Right
}

class MoveButtons {

    companion object {
        private val moveEventToIcon = mapOf(
            MoveEvent.Up to HeroIcons.arrow_up,
            MoveEvent.Down to HeroIcons.arrow_down,
            MoveEvent.Left to HeroIcons.arrow_left,
            MoveEvent.Right to HeroIcons.arrow_right
        )
    }

    private val _moveEvents: MutableSharedFlow<MoveEvent> = MutableSharedFlow()
    val moveEvents: Flow<MoveEvent> = _moveEvents.asSharedFlow()

    private fun RenderContext.renderButton(moveEvent: MoveEvent) {
        button(
            """w-10 h-10 flex justify-center items-center border border-neutral-dark-secondary 
                | dark:border-neutral-light-secondary rounded-md text-neutral-dark-secondary
                | dark:text-neutral-light-secondary""".trimMargin()
        ) {
            moveEventToIcon[moveEvent]?.let {
                icon("w-6 h-6", definition = it)
            }
        }.clicks.map { moveEvent } handledBy { _moveEvents.emit(it) }
    }

    fun RenderContext.render() {
        div("flex gap-4") {
            renderButton(MoveEvent.Left)
            renderButton(MoveEvent.Up)
            renderButton(MoveEvent.Down)
            renderButton(MoveEvent.Right)
        }
    }
}

fun RenderContext.moveButtons(init: MoveButtons.() -> Unit = {}) {
    MoveButtons().apply(init).run { render() }
}