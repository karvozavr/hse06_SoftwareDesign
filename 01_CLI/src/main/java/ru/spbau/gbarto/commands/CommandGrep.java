package ru.spbau.gbarto.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import ru.spbau.gbarto.exception.CommandException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * CommandGrep allows you to search patterns in the text.
 */
public class CommandGrep extends AbstractCommand {
    private class Arguments {
        @Parameter(names = {"-i"})
        boolean lower;
        @Parameter(names = {"-w"})
        boolean words;
        @Parameter(names = {"-A"})
        int append;

        @Parameter(required = true)
        List<String> others = new ArrayList<>();
    }

    private Arguments parseArguments(String[] args) {
        Arguments arguments = new Arguments();
        JCommander.newBuilder()
                .addObject(arguments)
                .build().parse(args);
        return arguments;
    }

    private Pattern getPattern(Arguments arguments) {
        String pattern = arguments.others.get(0);

        if (arguments.lower) {
            pattern = pattern.toLowerCase();
        }

        if (arguments.words) {
            pattern = "\\b" + pattern + "\\b";
        }

        return Pattern.compile(pattern);
    }

    private List<String> getFiles(Arguments arguments) {
        return arguments.others.subList(1, arguments.others.size());
    }

    private void redirectStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = input.read(buffer)) > 0) {
            output.write(buffer, 0, read);
        }
        output.flush();
    }

    private void writeLine(String line, String prefix, String sep, OutputStream output) throws IOException {
        if (prefix != null) {
            output.write((prefix + sep).getBytes());
        }

        output.write((line + System.lineSeparator()).getBytes());
    }

    private ByteArrayOutputStream executeForLines(Pattern pattern, Arguments arguments, List<String> lines, String prefix) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        int append = 0;
        for (String line : lines) {
            if (arguments.lower) {
                line = line.toLowerCase();
            }

            if (pattern.matcher(line).find()) {
                writeLine(line, prefix, ":", output);
                append = arguments.append;
            } else if (append > 0) {
                writeLine(line, prefix, "-", output);
                append--;
            }
        }

        return output;
    }

    /**
     * Executes command.
     *
     * @param args is a String array of arguments
     * @param input is an input stream of data
     * @return output stream
     * @throws CommandException if something goes wrong
     */
    @Override
    public ByteArrayOutputStream execute(String[] args, InputStream input) throws CommandException {
        Arguments arguments = parseArguments(args);

        Pattern pattern = getPattern(arguments);
        List<String> files = getFiles(arguments);

        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            if (files.isEmpty()) {
                if (input == null) {
                    throw new CommandException("No input to read");
                }

                ByteArrayOutputStream tmpStream = new ByteArrayOutputStream();
                redirectStream(input, tmpStream);
                String[] lines = tmpStream.toString().split(System.lineSeparator());
                ByteArrayOutputStream out = executeForLines(pattern, arguments, Arrays.asList(lines), null);
                output.write(out.toByteArray());
            } else {
                for (String file : files) {
                    String prefix = files.size() > 1 ? file : null;
                    List<String> lines = Files.readAllLines(Paths.get(file));
                    ByteArrayOutputStream out = executeForLines(pattern, arguments, lines, prefix);
                    output.write(out.toByteArray());
                }
            }
            return output;
        } catch (IOException e) {
            throw new CommandException("Problem reading or writing data to stream");
        }
    }
}
