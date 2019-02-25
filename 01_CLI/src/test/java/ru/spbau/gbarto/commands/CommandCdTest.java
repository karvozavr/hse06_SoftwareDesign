package ru.spbau.gbarto.commands;

import org.junit.jupiter.api.Test;
import ru.spbau.gbarto.exception.CommandException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class CommandCdTest {

    @Test
    void testCdHome() throws IOException, CommandException {
        Path root = Paths.get("src", "test", "resources", "dir1").toAbsolutePath();
        Environment env = new Environment(root);
        CommandCd echo = new CommandCd(env);
        OutputStream output = echo.execute(new String[]{}, null);
        output.close();

        assertEquals(System.getProperty("user.dir"), env.getCurrentDirectory().toString());
    }

    @Test
    void testCdArg() throws IOException, CommandException {
        Path root = Paths.get("src", "test", "resources").toAbsolutePath();
        Environment env = new Environment(root);
        CommandCd echo = new CommandCd(env);
        OutputStream output = echo.execute(new String[]{"dir1"}, null);
        output.close();

        assertEquals(root.resolve("dir1").toString(), env.getCurrentDirectory().toString());
    }
}