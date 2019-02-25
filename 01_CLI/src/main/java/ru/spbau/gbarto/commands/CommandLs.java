package ru.spbau.gbarto.commands;

import ru.spbau.gbarto.exception.CommandException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * Returns current path.
 */
public class CommandLs extends AbstractCommand {

    private Environment environment;

    public CommandLs(Environment environment) {
        this.environment = environment;
    }

    /**
     * Executes command.
     *
     * @param args  is a String array of arguments
     * @param input is an input stream of data (ignored)
     * @return output stream
     * @throws CommandException if something goes wrong
     */
    @Override
    public ByteArrayOutputStream execute(String[] args, InputStream input) throws CommandException {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            final Path directory = environment.getCurrentDirectory().toAbsolutePath();

            if (args.length == 0) {
                for (Path p : Files.list(directory).collect(Collectors.toList())) {
                    String file = p.toString() + System.lineSeparator();
                    output.write(file.getBytes());
                }
            } else if (args.length == 1 && environment.changeDirectory(Paths.get(args[0]))) {
                for (Path p : Files.list(environment.getCurrentDirectory()).collect(Collectors.toList())) {
                    String file = p.toString() + System.lineSeparator();
                    output.write(file.getBytes());
                }
                environment.changeDirectory(directory);
            } else {
                String error = "Error: invalid arguments." + System.lineSeparator();
                output.write(error.getBytes());
            }

            return output;
        } catch (IOException e) {
            throw new CommandException("Problem reading or writing data to stream");
        }
    }
}
