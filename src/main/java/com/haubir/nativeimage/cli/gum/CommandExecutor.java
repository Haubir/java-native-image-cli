package com.haubir.nativeimage.cli.gum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class CommandExecutor {

    public List<String> executeWithStdin(Mode mode, String stdin, String... args) {
        try {
            var processBuilder = new ProcessBuilder(args);
            processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
            if (mode == Mode.STANDARD) {
                processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            }
            Process p = processBuilder.start();
            OutputStream outputStream = p.getOutputStream();
            if (stdin != null) {
                outputStream.write(stdin.getBytes());
            }
            outputStream.close();
            p.waitFor();
            int exitValue = p.exitValue();
            if (mode == Mode.INTERACTIVE && exitValue != 0) {
                throw new ProcessTerminatedAbnormallyException("Command terminated with abnormal exit value: " + exitValue);
            }
            if (mode == Mode.INTERACTIVE) {
                var reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                List<String> ret = new ArrayList<>();
                String line;
                while ((line = reader.readLine()) != null) {
                    ret.add(line);
                }
                return ret;
            }
            return null;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error while executing command", e);
        }
    }

    public List<String> execute(Mode mode, String... args) {
        return executeWithStdin(mode, null, args);
    }


    public enum Mode {
        INTERACTIVE,
        STANDARD
    }
}
