package com.haubir.nativeimage.cli;

import picocli.CommandLine;

public class Main {
    public static void main(String[] args) {
        final var commandLine = new CommandLine(new OmegapointCLI());

        if (args.length < 1) {
            commandLine.usage(System.out);
            return;
        }

        int exitCode = commandLine.execute(args);
        System.exit(exitCode);
    }
}
