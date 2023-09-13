package com.haubir.nativeimage.cli;

import picocli.CommandLine.Command;

@Command(
        name = "omegapoint-cli",
        description = "The unofficial Omegapoint CLI of the future?",
        aliases = {"opcli"},
        mixinStandardHelpOptions = true
)
public class OmegapointCLI implements Runnable {
    @Override
    public void run() {

    }
}
