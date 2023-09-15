package com.haubir.nativeimage.cli.commands;

import com.haubir.nativeimage.cli.gum.Gum;
import com.haubir.nativeimage.cli.model.hello.FirstName;
import com.haubir.nativeimage.cli.model.hello.FullName;
import com.haubir.nativeimage.cli.model.hello.LastName;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import java.util.List;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;

@Command(
        name = "hello",
        description = "Lets user input name and then greets the user",
        mixinStandardHelpOptions = true
)
public class Hello implements Callable<Validation<Integer, Integer>> {
    private final Gum gum;

    public Hello(final Gum gum) {
        this.gum = gum;
    }

    @Override
    public Validation<Integer, Integer> call() {
        // Gum handles user input and also renders the terminal for us!
        final String firstName = gum.input("First name");
        final String lastName = gum.input("Last name");

        // Validate input
        return validateInput(firstName, lastName)
                .map(this::greetUser)
                .mapError(this::invalidInput);
    }

    private Validation<List<String>, FullName> validateInput(final String firstName, final String lastName) {
        return Validation.combine(
                        FirstName.validate(firstName),
                        LastName.validate(lastName)
                )
                .ap(FullName::new)
                .mapError(Seq::asJava);
    }

    private Integer invalidInput(final List<String> reasons) {
        gum.message("Could not greet user due to:\n%s...".formatted(String.join("\n", reasons)));
        return -1;
    }

    private Integer greetUser(final FullName fullName) {
        gum.message("Hello %s %s!".formatted(fullName.firstName().value(), fullName.lastName().value()));
        return 0;
    }
}
