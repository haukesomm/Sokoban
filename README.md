# Sokoban

This project is a clone of the popular puzzle game [Sokoban](https://de.wikipedia.org/wiki/Sokoban).
It consists of a `core` library written in Kotlin and different UI implementations built on top of it.

## Modules

| Module                  | Description                                                                                                                                      |
|-------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------|
| `core`                  | Core library written in Kotlin containing the actual game logic                                                                                  |
| `web-app` (coming soon) | Modern web-based app written in [fritz2](https://fritz2.dev).                                                                                    |
| `desktop-app`           | Legacy Java App using Swing. The original version of this was my first-ever programming project. Has been migrated to use the new `core` module. |