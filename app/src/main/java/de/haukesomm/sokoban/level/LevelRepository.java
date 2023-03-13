package de.haukesomm.sokoban.level;

import java.util.List;

public interface LevelRepository {

    List<LevelDescription> getAvailableLevels();

    Level getLevelOrNull(String id);
}
