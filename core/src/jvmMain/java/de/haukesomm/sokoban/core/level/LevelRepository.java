package de.haukesomm.sokoban.core.level;

import java.util.List;

public interface LevelRepository {

    List<LevelDescription> getAvailableLevels();

    Level getLevelOrNull(String id);
}
