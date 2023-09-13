package com.haubir.nativeimage.cli.gum;

import org.jline.terminal.Terminal;

import java.util.function.Supplier;

public class Spinner {
    private static final String EMPTY_ENTIRE_CURRENT_LINE = "\u001B[A";
    private static final char[] SPINNER = {'⣾', '⣽', '⣻', '⢿', '⡿', '⣟', '⣯', '⣷'};
    private final Terminal terminal;
    private int spinCounter = 0;
    private boolean running = false;
    private String message = null;

    public Spinner(Terminal terminal) {
        this.terminal = terminal;
    }

    public <T> T spinWhile(String message, Supplier<T> blockingOperation) {
        this.message = message;
        this.running = true;
        Thread t = Thread.ofVirtual().start(this::draw);
        try {
            return blockingOperation.get();
        } finally {
            this.running = false;
            t.interrupt();
        }
    }


    private void draw() {
        terminal.writer().println();
        terminal.flush();
        while (running) {
            terminal.writer().println(EMPTY_ENTIRE_CURRENT_LINE + "\r" + getSpinnerChar() + "  " + message);
            terminal.flush();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                running = false;
            }
        }
        terminal.writer().println(EMPTY_ENTIRE_CURRENT_LINE + "                                                                                                  \r");
        terminal.flush();
    }

    private char getSpinnerChar() {
        char spinChar = SPINNER[spinCounter];
        spinCounter++;
        if (spinCounter == SPINNER.length) {
            spinCounter = 0;
        }
        return spinChar;
    }


}