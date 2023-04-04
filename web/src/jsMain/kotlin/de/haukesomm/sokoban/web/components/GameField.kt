package de.haukesomm.sokoban.web.components

import de.haukesomm.sokoban.core.*
import de.haukesomm.sokoban.web.components.icons.HeroIcons
import de.haukesomm.sokoban.web.components.icons.IconDefinition
import de.haukesomm.sokoban.web.components.icons.icon
import dev.fritz2.core.RenderContext
import kotlinx.coroutines.flow.Flow

private enum class TileStyling(
    val classes: String? = null,
    val icon: IconDefinition? = null
) {
    Wall(classes = "bg-primary-500 rounded-lg border border-primary-600"),
    Target(icon = HeroIcons.cube_transparent),
    Default(classes = "bg-secondary-500");

    companion object {
        fun forType(type: TileType): TileStyling? =
            when(type) {
                TileType.WALL -> Wall
                TileType.TARGET -> Target
                else -> null
            }
    }
}

private enum class EntityStyling(
    val icon: IconDefinition? = null
) {
    Player(HeroIcons.user),
    Box(HeroIcons.cube);

    companion object {
        fun forType(type: EntityType): EntityStyling? =
            when(type) {
                EntityType.PLAYER -> Player
                EntityType.BOX -> Box
                else -> null
            }
    }
}

class GameField(private val states: Flow<GameState>) {

    fun RenderContext.render() {
        div(TileStyling.Default.classes) {
            states.render(into = this) { state ->
                for (y in 0 until state.height) {
                    div("flex flex-row") {
                        for (x in 0 until state.width) {
                            val tile = state.tileAt(x, y)
                            val entity = state.entityAt(x, y)
                            renderTile(tile, entity)
                        }
                    }
                }
            }
        }
    }

    private fun RenderContext.renderTile(tile: Tile, entity: Entity?) {
        div("w-8 h-8 flex flex-shrink-0 justify-center items-center text-black") {
            val entityStyling = entity?.let { EntityStyling.forType(it.type) }

            TileStyling.forType(tile.type)?.let { styling ->
                styling.classes?.let { className(it) }
                if (entityStyling == null && styling.icon != null) {
                    icon("w-4 h-4", definition = styling.icon)
                }
            }

            entityStyling?.let { styling ->
                styling.icon?.let {
                    icon("w-5 h-5", definition = it)
                }
            }
        }
    }
}

fun RenderContext.gameField(states: Flow<GameState>, ) {
    GameField(states).run {
        render()
    }
}