package ru.spbau.gbarto.commands;

import org.junit.jupiter.api.Test;
import ru.spbau.gbarto.exception.CommandException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class CommandWordCountTest {
    @Test
    void testInputStream() throws CommandException, IOException {
        String data = "8" + System.lineSeparator() + "800" + System.lineSeparator() + "555 35 35";
        CommandWordCount wc = new CommandWordCount();
        ByteArrayInputStream input = new ByteArrayInputStream(data.getBytes());
        OutputStream output = wc.execute(null, input);
        output.close();

        assertEquals("3 5 15" + System.lineSeparator(), output.toString());
    }

    @Test
    void testFile() throws CommandException, IOException {
        CommandWordCount wc = new CommandWordCount();
        OutputStream output = wc.execute(new String[]{Paths.get("src", "test", "resources", "file.txt").toString()}, null);
        output.close();

        assertEquals("3 5 15" + System.lineSeparator(), output.toString());
    }
}