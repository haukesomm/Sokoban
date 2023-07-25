package de.haukesomm.sokoban.core.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

actual val SokobanMainScope: CoroutineScope
    get() = MainScope()