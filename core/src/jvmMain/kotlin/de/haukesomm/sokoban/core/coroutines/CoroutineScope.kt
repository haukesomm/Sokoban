package de.haukesomm.sokoban.core.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.ForkJoinPool

actual val SokobanMainScope: CoroutineScope
    get() = CoroutineScope(ForkJoinPool.commonPool().asCoroutineDispatcher())