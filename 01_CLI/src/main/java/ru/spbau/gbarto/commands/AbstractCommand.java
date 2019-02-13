package ru.spbau.gbarto.commands;

import ru.spbau.gbarto.exception.CommandException;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * AbstractCommand is an abstract class for commands that can be executed by CLI.
 */
public abstract class AbstractCommand {
    public abstract ByteArrayOutputStream execute(String[] args, InputStream input) throws CommandException;
}
