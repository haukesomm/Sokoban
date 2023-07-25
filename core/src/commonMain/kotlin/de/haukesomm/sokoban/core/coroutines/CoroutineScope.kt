package de.haukesomm.sokoban.core.coroutines

import kotlinx.coroutines.CoroutineScope

/**
 * This is the main [CoroutineScope] of the application.
 *
 * Its implementation depends on the platform the application is running on.
 */
expect val SokobanMainScope: CoroutineScope