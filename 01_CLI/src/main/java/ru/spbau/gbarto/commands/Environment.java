package ru.spbau.gbarto.commands;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * The shell environment.
 */
public class Environment {

    private Path currentDirectory;

    /**
     * Return current directory.
     *
     * @return current directory
     */
    public Path getCurrentDirectory() {
        return currentDirectory;
    }

    public Environment(Path directory) {
        currentDirectory = directory;
    }

    /**
     * Change directory to given
     *
     * @param newDirectory new directory
     * @return success
     */
    public boolean changeDirectory(Path newDirectory) {
        if (newDirectory.isAbsolute() && Files.exists(newDirectory) && Files.isDirectory(newDirectory)) {
            currentDirectory = newDirectory;
            return true;
        } else {
            Path dir = currentDirectory.resolve(newDirectory).normalize();
            if (Files.exists(dir) && Files.isDirectory(dir)) {
                currentDirectory = dir;
                return true;
            }
        }

        return false;
    }
}
