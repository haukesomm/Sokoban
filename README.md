# Sokoban

![Build Workflow](https://github.com/haukesomm/Sokoban/actions/workflows/build-and-deploy.yml/badge.svg)
[![Awesome Kotlin Badge](https://kotlin.link/awesome-kotlin.svg)](https://github.com/KotlinBy/awesome-kotlin)

This project is a clone of the popular puzzle game [Sokoban](https://de.wikipedia.org/wiki/Sokoban).  
It consists of a `core` library written in Kotlin and a [fritz2](https://fritz2.dev) based web app.  
Additionally, a legacy version based on Java and Swing is included for the nostalgia 🦖

The latest web version can be played [here](https://sokoban.haukesomm.de).

<img src="./assets/screenshot.png" width="50%">

## Modules

| Module   | Description                                                                                                                                                 |
|----------|-------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `core`   | Core library written in Kotlin containing the actual game logic                                                                                             |
| `web`    | Modern web-based app written in Kotlin using the [fritz2](https://fritz2.dev) framework                                                                     |
| `legacy` | Legacy Java app using Swing. The original version of this was my first-ever project when I learned to code. Has been migrated to use the new `core` module. |