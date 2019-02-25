package ru.spbau.gbarto.commands;

import org.junit.jupiter.api.Test;
import ru.spbau.gbarto.exception.CommandException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class CommandLsTest {

    @Test
    void testLsCurrentDir() throws IOException, CommandException {
        Path root = Paths.get("src", "test", "resources", "dir1").toAbsolutePath();
        CommandLs echo = new CommandLs(new Environment(root));
        OutputStream output = echo.execute(new String[]{}, null);
        output.close();

        assertEquals(String.join(
                System.lineSeparator(),
                root.resolve("dir2").toString(),
                root.resolve("dir4").toString(),
                root.resolve("file.extension").toString()
        ) + System.lineSeparator(), output.toString());
    }

    @Test
    void testLsArg() throws IOException, CommandException {
        Path root = Paths.get("src", "test", "resources", "dir1").toAbsolutePath();
        CommandLs echo = new CommandLs(new Environment(root));
        OutputStream output = echo.execute(new String[]{"dir2"}, null);
        output.close();

        assertEquals(String.join(
                System.lineSeparator(),
                root.resolve("dir2").resolve("dir3").toString()
        ) + System.lineSeparator(), output.toString());
    }
}