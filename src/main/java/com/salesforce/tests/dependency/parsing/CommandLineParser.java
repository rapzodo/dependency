package com.salesforce.tests.dependency.parsing;

import com.salesforce.tests.dependency.enums.CommandType;
import com.salesforce.tests.dependency.models.ComponentCommand;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

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
        final CommandType commandType = CommandType.valueOf(fields[0]);
        if (isEndOrListCommand(commandType)) {
            return new ComponentCommand(commandType);
        }
        final String componentName = fields[1];
        if (isDependCommand(commandType)) {
            final String[] dependenciesArray = Arrays.copyOfRange(fields, 2, fields.length);
            return new ComponentCommand(commandType, componentName, Arrays.asList(dependenciesArray));
        }
        return new ComponentCommand(commandType, componentName);
    }

    private boolean isDependCommand(CommandType commandType) {
        return commandType == CommandType.DEPEND;
    }

    private boolean isEndOrListCommand(CommandType commandType) {
        return commandType == CommandType.END || commandType == CommandType.LIST;
    }
}
