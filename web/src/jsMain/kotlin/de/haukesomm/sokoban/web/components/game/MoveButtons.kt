package de.haukesomm.sokoban.web.components.game

import de.haukesomm.sokoban.web.components.icons.HeroIcons
import de.haukesomm.sokoban.web.components.icons.icon
import dev.fritz2.core.RenderContext
import dev.fritz2.core.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

enum class MoveEvent {
    Up, Down, Left, Right
}

class MoveButtons<E : HTMLElement>(tag: Tag<E>) : Tag<E> by tag {

    companion object {
        private val moveEventToIcon = mapOf(
            MoveEvent.Up to HeroIcons.arrow_up,
            MoveEvent.Down to HeroIcons.arrow_down,
            MoveEvent.Left to HeroIcons.arrow_left,
            MoveEvent.Right to HeroIcons.arrow_right
        )
    }

    private val mutableMoveEventsFlow: MutableSharedFlow<MoveEvent> = MutableSharedFlow()
    val moveEvents: Flow<MoveEvent> = mutableMoveEventsFlow.asSharedFlow()

    private fun RenderContext.renderButton(moveEvent: MoveEvent) {
        button(
            """w-10 h-10 flex justify-center items-center border border-neutral-dark-secondary 
                | dark:border-neutral-light-secondary rounded-md text-neutral-dark-secondary
                | dark:text-neutral-light-secondary""".trimMargin()
        ) {
            moveEventToIcon[moveEvent]?.let {
                icon("w-6 h-6", definition = it)
            }
        }.clicks.map { moveEvent } handledBy {
            mutableMoveEventsFlow.emit(it)
        }
    }

    fun render() {
        className("flex gap-4")
        renderButton(MoveEvent.Left)
        renderButton(MoveEvent.Up)
        renderButton(MoveEvent.Down)
        renderButton(MoveEvent.Right)
    }
}

fun RenderContext.moveButtons(init: MoveButtons<HTMLDivElement>.() -> Unit = {}) {
    div {
        MoveButtons(this).apply(init).run {
            render()
        }
    }
}