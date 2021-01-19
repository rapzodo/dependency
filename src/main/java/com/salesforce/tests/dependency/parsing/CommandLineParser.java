package com.salesforce.tests.dependency.parsing;

import com.salesforce.tests.dependency.enums.CommandType;
import com.salesforce.tests.dependency.exceptions.InvalidCommandException;
import com.salesforce.tests.dependency.models.ComponentCommand;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class CommandLineParser implements Parser<ComponentCommand> {

    private static final String DEFAULT_DELIMITER = "\\s+";
    private final String delimiter;


    public CommandLineParser() {
        this.delimiter = DEFAULT_DELIMITER;
    }

    public ComponentCommand parse(String input) {
        System.out.println(input);
        final String[] fields = input.split(delimiter);
        try {
            final CommandType commandType = CommandType.valueOf(fields[0]);
            if (isEndOrListCommand(commandType)) {
                validateCommand(fields,length -> length > 1);
                return new ComponentCommand(commandType);
            }
            final String componentName = fields[1];
            if (isDependCommand(commandType)) {
                validateCommand(fields,length -> length < 3);
                final String[] dependenciesArray = Arrays.copyOfRange(fields, 2, fields.length);
                return new ComponentCommand(commandType, componentName, Arrays.asList(dependenciesArray));
            }
            if (isInstallCommand(commandType)) {
                validateCommand(fields,length -> length != 2);
            }
            return new ComponentCommand(commandType, componentName);
        } catch (IllegalArgumentException ex) {
            throw new InvalidCommandException();
        }
    }

    private boolean isInstallCommand(CommandType commandType) {
        return commandType == CommandType.INSTALL;
    }

    private void validateCommand(String[] fields, Predicate<Integer> predicate) {
        if (predicate.test(fields.length)) {
            throw new InvalidCommandException();
        }
    }

    private boolean isDependCommand(CommandType commandType) {
        return commandType == CommandType.DEPEND;
    }

    private boolean isEndOrListCommand(CommandType commandType) {
        return commandType == CommandType.END || commandType == CommandType.LIST;
    }
}
