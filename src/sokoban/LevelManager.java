package sokoban;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import sokoban.gui.Game;

public class LevelManager {

    public static final String EMPTY_LEVEL = "00";
    public static final String EMPTY_LEVEL_ALIAS = "Levels: ";
    public static final String CUSTOM_EXTENSION = ".custom";
    public static final String BUILTIN_EXTENSION = ".builtin";
    public static final String BUILTIN_PATH = "/sokoban/resources/levels/";
    
    private final URL url = getClass().getResource(BUILTIN_PATH);

    public String[] getFileList() throws Exception {
        try {
            List<String> levelNamesList = new ArrayList<>();

            if (url.getProtocol().equals("jar")) {
                try (ZipInputStream zip = new ZipInputStream(new FileInputStream(Game.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()))) {
                    for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
                        if (!entry.isDirectory() && entry.getName().endsWith(BUILTIN_EXTENSION)) {
                            System.out.println("Found level '" + entry.getName() + "'");
                            levelNamesList.add(entry.getName());
                        }
                    }
                }
            } else if (url != null && url.getProtocol().equals("file")) {
                levelNamesList.addAll(Arrays.asList(new File(url.toURI()).list()));
            }

            for (int i = 0; i < levelNamesList.size(); i++) {
                levelNamesList.set(i, levelNamesList.get(i).replaceAll(BUILTIN_EXTENSION, ""));
                String[] tmp = levelNamesList.get(i).split("/");
                levelNamesList.set(i, tmp[tmp.length - 1]);
            }

            Collections.sort(levelNamesList);

            for (int i = 0; i < levelNamesList.size(); i++) {
                if (levelNamesList.get(i).contains(EMPTY_LEVEL)) {
                    levelNamesList.set(i, EMPTY_LEVEL_ALIAS);
                }
            }

            return Arrays.copyOf(levelNamesList.toArray(), levelNamesList.toArray().length, String[].class);
        } catch (IOException | URISyntaxException e) {
            throw new Exception("Could not get file list!");
        }
    }

    public String getLevelData(String level) throws UnsupportedOperationException, FileNotFoundException {
        try {
            InputStream is;
            String levelString;

            if (level.endsWith(CUSTOM_EXTENSION)) {
                is = new FileInputStream(level);
            } else if (level.endsWith(BUILTIN_EXTENSION) && !level.contains(EMPTY_LEVEL_ALIAS)) {
                is = getClass().getResourceAsStream(BUILTIN_PATH + level);
            } else {
                throw new UnsupportedOperationException("The selcted file is no level file!");
            }

            levelString = new Scanner(is).useDelimiter("\\Z").next();

            if (!levelString.equals("") && levelString.contains("\r\n")) {
                levelString = levelString.replaceAll("\r\n", "");
            }

            return levelString;
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException("File not found.");
        }
    }

}
