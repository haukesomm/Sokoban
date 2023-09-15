package de.haukesomm.sokoban.core

import kotlinx.coroutines.CoroutineScope

/**
 * This is the main [CoroutineScope] of the application.
 *
 * Its implementation depends on the platform the application is running on.
 */
internal expect val SokobanMainScope: CoroutineScope