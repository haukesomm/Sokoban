package de.haukesomm.sokoban.legacy.level;

import de.haukesomm.sokoban.core.level.Level;
import de.haukesomm.sokoban.core.level.LevelDescription;
import de.haukesomm.sokoban.core.level.LevelRepository;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class JarResourceLevelRepository implements LevelRepository {

    private static final String LEVEL_FILES_RESOURCE_PATH = "de/haukesomm/sokoban/legacy/level";
    private static final String BUILTIN_LEVEL_FILE_EXTENSION = ".sokoban";

    private final int levelWidth;
    private final int levelHeight;

    // TODO: Calculate width and height from file
    public JarResourceLevelRepository(int levelWidth, int levelHeight) {
        this.levelWidth = levelWidth;
        this.levelHeight = levelHeight;
    }

    @Override
    @NotNull
    public List<LevelDescription> getAvailableLevels() {
        var levelDescriptions = new ArrayList<LevelDescription>();

        var levelResourceInputStream = getClass().getClassLoader().getResourceAsStream(LEVEL_FILES_RESOURCE_PATH);

        if (levelResourceInputStream == null) {
            System.err.println("Unable to open level resource directory: " + LEVEL_FILES_RESOURCE_PATH);
            return levelDescriptions;
        }

        try(var fileNameReader = new BufferedReader(new InputStreamReader(levelResourceInputStream))) {
            String fileName;
            while ((fileName = fileNameReader.readLine()) != null) {
                if (fileName.endsWith(BUILTIN_LEVEL_FILE_EXTENSION)) {
                    var displayName = fileName.replaceAll(BUILTIN_LEVEL_FILE_EXTENSION + "$", "");
                    levelDescriptions.add(new LevelDescription(fileName, displayName));
                }
            }
        } catch (IOException ex) {
            System.err.println("Unable to read level resource directory:");
            ex.printStackTrace();
        }

        return levelDescriptions;
    }

    @Override
    public Level getLevelOrNull(String id) {
        var layoutStringBuilder = new StringBuilder();

        var levelResource = getClass().getClassLoader().getResourceAsStream(LEVEL_FILES_RESOURCE_PATH + "/" + id);

        if (levelResource == null) {
            System.err.println("Unable to open builtin level resource: " + id);
            return null;
        }

        try(var levelDataReader = new BufferedReader(new InputStreamReader(levelResource))) {
            String row;
            while ((row = levelDataReader.readLine()) != null) {
                layoutStringBuilder.append(row);
            }
        } catch (IOException ex) {
            System.err.println("Unable to read level resource '" + id +"':");
            ex.printStackTrace();
            return null;
        }

        return new Level(id, id, levelWidth, levelHeight, layoutStringBuilder.toString());
    }
}
