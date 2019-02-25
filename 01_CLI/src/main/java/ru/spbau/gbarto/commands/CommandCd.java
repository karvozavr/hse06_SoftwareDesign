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
public class CommandCd extends AbstractCommand {

    private Environment environment;

    public CommandCd(Environment environment) {
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

            if (args.length == 0) {
                environment.changeDirectory(Paths.get(System.getProperty("user.dir")).toAbsolutePath());
            } else if (!(args.length == 1 && environment.changeDirectory(Paths.get(args[0])))) {
                String error = "Error: invalid arguments." + System.lineSeparator();
                output.write(error.getBytes());
            }

            return output;
        } catch (IOException e) {
            throw new CommandException("Problem reading or writing data to stream");
        }
    }
}

