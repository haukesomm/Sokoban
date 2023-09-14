# Sokoban

![Build Workflow](https://github.com/haukesomm/Sokoban/actions/workflows/build-and-deploy.yml/badge.svg)
[![Awesome Kotlin Badge](https://kotlin.link/awesome-kotlin.svg)](https://github.com/KotlinBy/awesome-kotlin)

This project is a clone of the popular puzzle game [Sokoban](https://de.wikipedia.org/wiki/Sokoban).  
It consists of a `core` library written in Kotlin and a [fritz2](https://fritz2.dev) based web app.  
Additionally, a legacy version based on Java and Swing is included for the nostalgia 🦖

The latest snapshot version can be played [on the web](https://sokoban.haukesomm.de).

<img src="./assets/screenshot.png" width="50%">

## Modules

| Module   | Description                                                                                                                                                       |
|----------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `core`   | Core library written in Kotlin containing the actual game logic and bundled levels.                                                                               |
| `web`    | Web app written in Kotlin using the [fritz2](https://fritz2.dev) framework. It is based on the `core` library. This is the actual game that can be played online. |
| `legacy` | Legacy Java app using Swing. Back in school, the original version was one of my first coding projects. Has been rewritten to use the new `core` library as well.  |

> Please note: Even though the core module is used by the legacy Java code, it is intended to be used in Kotlin
> projects only and is not fully compatible with Java!