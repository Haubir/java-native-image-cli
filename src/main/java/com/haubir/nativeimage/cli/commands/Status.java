package com.haubir.nativeimage.cli.commands;

import com.haubir.nativeimage.cli.gum.Gum;
import io.vavr.control.Validation;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;

@Command(
        name = "status",
        description = "Status of something",
        mixinStandardHelpOptions = true
)
public class Status implements Callable<Validation<Integer, Integer>> {
    private final Gum gum;

    public Status(final Gum gum) {
        this.gum = gum;
    }

    @Override
    public Validation<Integer, Integer> call() {
        gum.message("To be implemented.");
        return Validation.valid(0);
    }
}
