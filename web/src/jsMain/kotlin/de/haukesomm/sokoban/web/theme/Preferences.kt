package de.haukesomm.sokoban.web.theme

import kotlinx.browser.window
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Represents the user's preference for the theme.
 *
 * The current preference is saved in the browser's local storage.
 *
 * @see ThemePreferences
 */
enum class ThemePreference {

    /**
     * Always use the light theme.
     */
    AlwaysLight,

    /**
     * Always use the dark theme.
     */
    AlwaysDark,

    /**
     * Use the theme that is currently set in the browser.
     */
    FollowSystem;

    companion object {
        /**
         * Returns the [ThemePreference] with the given name or [FollowSystem] if no such preference exists.
         */
        fun getByNameOrDefault(name: String): ThemePreference? =
            values().find { it.name == name }
    }
}

/**
 * Object used to read and write the user's theme preference.
 *
 * The preference is saved in the browser's local storage.
 *
 * The current preference can either be read from the [selectedPreference] flow or by calling
 * [getCurrentDarkModePreference].
 */
object ThemePreferences {

    private const val LOCAL_STORAGE_DARK_MODE_KEY = "dark_mode_preference"

    private val internalState = MutableStateFlow(getCurrentDarkModePreference())

    /**
     * A flow that emits the user's current theme preference.
     */
    val selectedPreference: Flow<ThemePreference> = internalState

    /**
     * Returns the user's current theme preference.
     */
    fun getCurrentDarkModePreference(): ThemePreference =
        window.localStorage.getItem(LOCAL_STORAGE_DARK_MODE_KEY)?.let {
            ThemePreference.getByNameOrDefault(it)
        } ?: ThemePreference.FollowSystem

    /**
     * Sets the user's theme preference.
     *
     * If called, the [selectedPreference] flow will emit the new preference.
     */
    fun setDarkModePreference(preference: ThemePreference) {
        window.localStorage.setItem(LOCAL_STORAGE_DARK_MODE_KEY, preference.name)
        internalState.value = preference
    }
}