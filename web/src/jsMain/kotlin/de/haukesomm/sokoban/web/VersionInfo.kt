package de.haukesomm.sokoban.web

import kotlinx.browser.document

object VersionInfo {
    val version: String by lazy {
        document.asDynamic().sokobanVersion.toString()
    }
    val commitHash: String by lazy {
        document.asDynamic().sokobanCommitHash.toString()
    }
}