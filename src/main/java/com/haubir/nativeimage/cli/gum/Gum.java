package com.haubir.nativeimage.cli.gum;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Gum {

    private final CommandExecutor executor;
    private final Spinner spinner;

    public Gum(CommandExecutor executor, Spinner spinner) {
        this.executor = executor;
        this.spinner = spinner;
    }

    public <T> T choose(List<T> items, Function<T, String> displayFunction) {
        String lines = items.stream().map(displayFunction).collect(Collectors.joining("\n"));
        List<String> ret = executor.executeWithStdin(CommandExecutor.Mode.INTERACTIVE, lines, "gum", "choose");
        if (ret.isEmpty()) {
            return null;
        }
        return findOneMatch(ret.get(0), items, displayFunction);
    }

    public <T> List<T> multiChoose(List<T> items, List<T> itemsPreSelected, Function<T, String> displayFunction) {
        String lines = items.stream().map(displayFunction).collect(Collectors.joining("\n"));
        String preSelected = itemsPreSelected.stream().map(displayFunction).collect(Collectors.joining(","));
        List<String> matches = executor.executeWithStdin(CommandExecutor.Mode.INTERACTIVE, lines,
                "gum",
                "choose",
                "--no-limit",
                "--selected",
                preSelected);
        return matchItems(matches, items, displayFunction);
    }

    public void message(String message) {
        executor.execute(CommandExecutor.Mode.STANDARD, "gum", "style",
                "--border", "double",
                "--padding", "1",
                "--border-foreground", "212",
                "--bold",
                message);
    }

    public <T> T spinner(String title, Supplier<T> blockingOperation) {
        return spinner.spinWhile(title, blockingOperation);
    }

    public <T> T chooseFromTable(List<String> headers, List<T> items, Function<T, String> displayFunction) {
        String csvHeaders = String.join(",", headers);
        String csvData = csvHeaders + "\n" + items.stream()
                .map(displayFunction)
                .collect(Collectors.joining(System.lineSeparator()));

        List<String> ret = executor.executeWithStdin(
                CommandExecutor.Mode.INTERACTIVE,
                csvData,
                "gum",
                "table",
                "--widths", "40,30,30"
        );
        if (ret.isEmpty()) {
            return null;
        }
        return findOneMatch(ret.get(0), items, displayFunction);
    }

    private <T> List<T> matchItems(List<String> matches, List<T> items, Function<T, String> displayFunction) {
        List<T> ret = new ArrayList<>();
        int matchIndex = 0;
        for (T item : items) {
            if (matchIndex >= matches.size()) {
                break;
            }
            String lookingFor = matches.get(matchIndex);
            if (lookingFor.equals(displayFunction.apply(item))) {
                ret.add(item);
                matchIndex++;
            }
        }
        return ret;
    }

    private <T> T findOneMatch(String s, List<T> items, Function<T, String> displayFunction) {
        return items.stream()
                .filter(item -> s.equals(displayFunction.apply(item)))
                .findFirst()
                .orElse(null);
    }

    public String input(String title) {
        List<String> ret = executor.execute(CommandExecutor.Mode.INTERACTIVE, "gum", "input", "--header", title);
        if (ret.isEmpty()) {
            return null;
        }
        return ret.get(0);
    }

    public String input(String title, String value) {
        List<String> ret = executor.execute(CommandExecutor.Mode.INTERACTIVE, "gum", "input",
                "--header", title,
                "--value", value
        );
        if (ret.isEmpty()) {
            return null;
        }
        return ret.get(0);
    }

    public String password(String title, String placeholder) {
        List<String> ret = executor.execute(CommandExecutor.Mode.INTERACTIVE, "gum", "input",
                "--password",
                "--header", title,
                "--placeholder", placeholder,
                "--char-limit", "0"
        );
        if (ret.isEmpty()) {
            return null;
        }
        return ret.get(0);
    }

    public boolean confirm(String question, String affirmativeChoice) {
        executor.execute(CommandExecutor.Mode.INTERACTIVE, "gum", "confirm",
                "--affirmative", affirmativeChoice,
                question
        );
        return true;
    }

    public String write(String placeholder) {
        List<String> ret = executor.execute(CommandExecutor.Mode.INTERACTIVE, "gum", "write",
                "--placeholder", placeholder,
                "--char-limit", "0"
        );
        if (ret.isEmpty()) {
            return null;
        }
        return String.join("\n", ret);
    }
}
