package ru.spbau.gbarto.commands;

import org.junit.jupiter.api.Test;
import ru.spbau.gbarto.exception.CommandException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class CommandCatTest {
    @Test
    void testInputStream() throws CommandException, IOException {
        String data = "8" + System.lineSeparator() + "800" + System.lineSeparator() + "555 35 35";
        CommandCat cat = new CommandCat();
        ByteArrayInputStream input = new ByteArrayInputStream(data.getBytes());
        OutputStream output = cat.execute(null, input);
        output.close();

        assertEquals(data, output.toString());
    }

    @Test
    void testFile() throws CommandException, IOException {
        CommandCat cat = new CommandCat();
        OutputStream output = cat.execute(new String[]{Paths.get("src", "test", "resources", "file.txt").toString()}, null);
        output.close();

        assertEquals("8" + System.lineSeparator() + "800" + System.lineSeparator() + "555 35 35", output.toString());
    }
}