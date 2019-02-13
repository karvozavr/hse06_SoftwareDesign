package ru.spbau.gbarto.commands;

import org.junit.jupiter.api.Test;
import ru.spbau.gbarto.exception.CommandException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class CommandGrepTest {
    @Test
    void testFile() throws CommandException, IOException {
        CommandGrep grep = new CommandGrep();
        OutputStream output = grep.execute(new String[]{"8", Paths.get("src", "test", "resources", "file.txt").toString()}, null);
        output.close();

        assertEquals("8\n800\n", output.toString());
    }
}