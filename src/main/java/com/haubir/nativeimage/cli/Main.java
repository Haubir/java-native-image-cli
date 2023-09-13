package com.haubir.nativeimage.cli;

import com.haubir.nativeimage.cli.commands.Status;
import com.haubir.nativeimage.cli.gum.CommandExecutor;
import com.haubir.nativeimage.cli.gum.Gum;
import com.haubir.nativeimage.cli.gum.ProcessTerminatedAbnormallyException;
import com.haubir.nativeimage.cli.gum.Spinner;
import java.io.IOException;
import org.jline.terminal.TerminalBuilder;
import picocli.CommandLine;

public class Main {
    public static void main(String[] args) throws IOException {
        final var commandExecutor = new CommandExecutor();
        final var terminal = TerminalBuilder.builder().dumb(true).build();
        final var spinner = new Spinner(terminal);
        final var gum = new Gum(commandExecutor, spinner);

        final var commandLine = new CommandLine(new OmegapointCLI())
                .addSubcommand(new Status(gum));

        if (args.length < 1) {
            commandLine.usage(System.out);
            return;
        }
        try {
            int exitCode = commandLine.execute(args);
            System.exit(exitCode);
        } catch (ProcessTerminatedAbnormallyException e) {
            System.out.println(e.getMessage() + ": Sudden exit");
        }
    }
}
