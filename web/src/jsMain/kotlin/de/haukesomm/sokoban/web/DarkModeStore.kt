package de.haukesomm.sokoban.web

import dev.fritz2.core.RootStore
import kotlinx.browser.window
import org.w3c.dom.Element
import org.w3c.dom.events.EventListener

private val darkModeQueryList
    get() = window.matchMedia("(prefers-color-scheme: dark)")

/**
 * A Store that reflects the current dark mode setting of the browser.
 *
 * Additionally, it updates the CSS classes of the `:root` element to reflect the current dark mode setting
 * so that Tailwind CSS can apply the correct color scheme.
 */
object DarkModeStore : RootStore<Boolean>(darkModeQueryList.matches) {

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
        darkModeQueryList.addEventListener("change", EventListener { event ->
            (event.asDynamic().matches as? Boolean)?.let {
                update(it)
            } ?: run {
                console.warn("Sokoban: Unable to update dark mode based on browser event")
            }
        })

        window.document.querySelector(":root")?.let(Element::classList)?.run {
            data handledBy  { darkModeEnabled ->
                if (darkModeEnabled) {
                    add(DARK_MODE_CLASS)
                    bgLight?.let { remove(it) }
                    bgDark?.let { add(it) }
                }
                else {
                    remove(DARK_MODE_CLASS)
                    bgLight?.let { add(it) }
                    bgDark?.let { remove(it) }
                }
            }
        }
    }
}