package de.haukesomm.sokoban.web.theme

import dev.fritz2.core.Store
import kotlinx.browser.window
import org.w3c.dom.events.EventListener

/**
 * A strategy that can be used to determine the initial value of the dark mode setting and to react to changes
 * in the browser's dark mode setting.
 *
 * A [Store] can react to changes in the dark mode setting by calling [onActivate] and [onDeactivate] when
 * the dark mode setting is enabled or disabled, respectively.
 *
 * The current strategy can be accessed via [DarkModeStrategies.getCurrentStrategy].
 */
sealed interface DarkModeStrategy {

    /**
     * The initial value of the dark mode setting.
     *
     * `true`, if the dark mode is enabled, `false` otherwise.
     */
    val initiallyEnabled: Boolean

    /**
     * Called when the strategy is activated.
     */
    fun Store<Boolean>.onActivate()

    /**
     * Called when the strategy is deactivated.
     */
    fun Store<Boolean>.onDeactivate()
}

/**
 * Strategy that statically activates or deactivates the dark mode, depending on the value of [initiallyEnabled].
 */
class StaticDarkModeStrategy(
    override val initiallyEnabled: Boolean
) : DarkModeStrategy {

    override fun Store<Boolean>.onActivate() {
        update(initiallyEnabled)
    }

    override fun Store<Boolean>.onDeactivate() {
        // Nothing to do here
    }
}

/**
 * Strategy that reflects the browser's dark mode setting.
 */
class SystemReflectingDarkModeStrategy : DarkModeStrategy {

    private var listener: EventListener? = null

    private val darkModeQueryList =
        window.matchMedia("(prefers-color-scheme: dark)")

    override val initiallyEnabled: Boolean
        get() = darkModeQueryList.matches

    override fun Store<Boolean>.onActivate() {
        update(initiallyEnabled)

        darkModeQueryList.addEventListener("change", EventListener { event ->
            (event.asDynamic().matches as? Boolean)?.let {
                update(it)
            } ?: run {
                console.warn("Sokoban: Unable to update dark mode based on browser event")
            }
        }.also { listener = it })
    }

    override fun Store<Boolean>.onDeactivate() {
        darkModeQueryList.removeEventListener("change", listener)
        listener = null
    }
}

/**
 * Object used to access the current [DarkModeStrategy].
 */
object DarkModeStrategies {

    /**
     * Returns the appropriate [DarkModeStrategy] for the given [themePreference].
     */
    fun getStrategyByPreference(themePreference: ThemePreference): DarkModeStrategy =
        when (themePreference) {
            ThemePreference.AlwaysDark -> StaticDarkModeStrategy(true)
            ThemePreference.AlwaysLight -> StaticDarkModeStrategy(false)
            ThemePreference.FollowSystem -> SystemReflectingDarkModeStrategy()
        }

    /**
     * Returns the current [DarkModeStrategy].
     */
    fun getCurrentStrategy(): DarkModeStrategy =
        getStrategyByPreference(ThemePreferences.getCurrentDarkModePreference())
}