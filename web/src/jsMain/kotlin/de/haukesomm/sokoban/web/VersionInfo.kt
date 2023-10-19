package de.haukesomm.sokoban.web

import kotlinx.browser.document

object VersionInfo {

    val sokobanVersion: String by lazy {
        document.asDynamic().sokobanVersion.toString()
    }

    val sokobanBuildTime: String by lazy {
        document.asDynamic().sokobanBuildTime.toString()
    }

    val fritz2Version: String by lazy {
        document.asDynamic().fritz2Version.toString()
    }
}