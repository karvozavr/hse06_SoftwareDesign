package ru.spbau.gbarto.commands;

import ru.spbau.gbarto.exception.CommandException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Returns three parameters: the number of lines, the number of words and the number of bytes in the files or input stream.
 */
public class CommandWordCount extends AbstractCommand {
    private int getWordsCount(List<String> lines) {
        return lines.stream()
                .map((line) -> line.trim().split("\\s+"))
                .filter((strings) -> strings.length > 1 || !strings[0].equals(""))
                .mapToInt((strings) -> strings.length)
                .sum();
    }

    private void writeToOutput(int lines, int words, int bytes, OutputStream output) throws IOException {
        String str = lines + " " + words + " " + bytes + System.lineSeparator();
        output.write(str.getBytes());
        output.flush();
    }

    private void executeForFile(String path, OutputStream output) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(path));
        int words = getWordsCount(lines);
        int bytes = Files.readAllBytes(Paths.get(path)).length;

        writeToOutput(lines.size(), words, bytes, output);
    }

    private void executeForStream(InputStream input, OutputStream output) throws CommandException, IOException {
        if (input == null) {
            throw new CommandException("No input to read");
        }

        ByteArrayOutputStream tmpStream = new ByteArrayOutputStream();
        int bytes = 0;
        byte[] buffer = new byte[1024];
        int read;
        while ((read = input.read(buffer)) > 0) {
            tmpStream.write(buffer, 0, read);
            bytes += read;
        }

        List<String> lines = Arrays.asList(tmpStream.toString().split(System.lineSeparator()));
        int words = getWordsCount(lines);

        writeToOutput(lines.size(), words, bytes, output);
    }

    /**
     * Executes command.
     *
     * @param args is a String array of arguments
     * @param input is an input stream of data (ignored)
     * @return output stream
     * @throws CommandException if something goes wrong
     */
    @Override
    public ByteArrayOutputStream execute(String[] args, InputStream input) throws CommandException {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            if (args != null) {
                for (String arg : args) {
                    executeForFile(arg, output);
                }
            } else {
                executeForStream(input, output);
            }

            return output;
        } catch (IOException e) {
            throw new CommandException("Problem reading or writing data to stream");
        }
    }
}
