package de.haukesomm.sokoban.web.theme

import dev.fritz2.core.RootStore
import kotlinx.browser.window
import org.w3c.dom.Element

/**
 * A Store that reflects the current dark mode setting of the browser.
 *
 * Additionally, it updates the CSS classes of the `:root` element to reflect the current dark mode setting
 * so that Tailwind CSS can apply the correct color scheme.
 */
object DarkModeStore : RootStore<Boolean>(DarkModeStrategies.getCurrentStrategy().initiallyEnabled) {

    private const val DARK_MODE_CLASS = "dark"

    /**
     * Initializes the store, so it can react to changes in the browser's dark mode setting.
     *
     * Additionally, a light and dark background color can be specified. These will be added and removed
     * from the `:root` element's class list, as it does not support Tailwind's `:dark` pseudo-class.
     * This is due to the fact that the class marking the dark mode as active needs to be higher in the
     * cascade than a class using the `:dark` selector. Since the pages background color is applied
     * to the `:root` element, it is necessary to add and remove the respective styles individually.
     */
    fun init(
        bgLight: String? = null,
        bgDark: String? = null
    ) {
        var activeStrategy: DarkModeStrategy? = null

        ThemePreferences.selectedPreference handledBy { preference ->
            activeStrategy?.run { onDeactivate() }

            val newStrategy = DarkModeStrategies.getStrategyByPreference(preference)
            newStrategy.run { onActivate() }
            activeStrategy = newStrategy
        }

        window.document.querySelector(":root")?.let(Element::classList)?.run {
            data handledBy { darkModeEnabled ->
                if (darkModeEnabled) {
                    add(DARK_MODE_CLASS)
                    bgLight?.let { remove(it) }
                    bgDark?.let { add(it) }
                } else {
                    remove(DARK_MODE_CLASS)
                    bgLight?.let { add(it) }
                    bgDark?.let { remove(it) }
                }
            }
        }
    }
}