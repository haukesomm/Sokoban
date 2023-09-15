package de.haukesomm.sokoban.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

internal actual val SokobanMainScope: CoroutineScope
    get() = MainScope()