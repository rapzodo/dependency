package com.salesforce.tests.dependency.parsing;

import com.salesforce.tests.dependency.enums.CommandType;
import com.salesforce.tests.dependency.exceptions.InvalidCommandException;
import com.salesforce.tests.dependency.models.ComponentCommand;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Arrays;

public class CommandLineParserTest {

    private CommandLineParser commandParser = new CommandLineParser();

    @Test(expected = InvalidCommandException.class)
    public void shouldThrowErrorOnInvalidCommand() {
        commandParser.parse("INVALID command here");
    }

    @Test(expected = InvalidCommandException.class)
    public void shouldThrowErrorOnInvalidListCommand(){
        commandParser.parse("LIST item1 item2");
    }

    @Test(expected = InvalidCommandException.class)
    public void shouldThrowErrorOnInvalidDependCommand() {
        commandParser.parse("DEPEND item1");
    }

    @Test(expected = InvalidCommandException.class)
    public void shouldThrowErrorOnInvalidInstallCommand() {
        commandParser.parse("INSTALL item1 item2");
    }
    @Test
    public void shouldParseDependCommand() {
        String dependCommand = "DEPEND item1  item2 item3";
        ComponentCommand expectedCommand = new ComponentCommand(CommandType.DEPEND, "item1", Arrays.asList("item2", "item3"));
        Assertions.assertThat(commandParser.parse(dependCommand)).isEqualTo(expectedCommand);
    }


    @Test
    public void shouldParseInstallCommand() {
        String dependCommand = "INSTALL item1";
        ComponentCommand expectedCommand = new ComponentCommand(CommandType.INSTALL, "item1");
        Assertions.assertThat(commandParser.parse(dependCommand)).isEqualTo(expectedCommand);
    }

    @Test
    public void shouldParseRemoveCommand() {
        String dependCommand = "REMOVE item1";
        ComponentCommand expectedCommand = new ComponentCommand(CommandType.REMOVE, "item1");
        Assertions.assertThat(commandParser.parse(dependCommand)).isEqualTo(expectedCommand);
    }

    @Test
    public void shouldParseListCommand() {
        String dependCommand = "LIST";
        ComponentCommand expectedCommand = new ComponentCommand(CommandType.LIST);
        Assertions.assertThat(commandParser.parse(dependCommand)).isEqualTo(expectedCommand);
    }

    @Test
    public void shouldParseEndCommand() {
        String dependCommand = "END";
        ComponentCommand expectedCommand = new ComponentCommand(CommandType.END);
        Assertions.assertThat(commandParser.parse(dependCommand)).isEqualTo(expectedCommand);
    }
}