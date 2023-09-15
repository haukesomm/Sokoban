package de.haukesomm.sokoban.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.ForkJoinPool

internal actual val SokobanMainScope: CoroutineScope
    get() = CoroutineScope(ForkJoinPool.commonPool().asCoroutineDispatcher())