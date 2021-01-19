package com.salesforce.tests.dependency.service;

import com.salesforce.tests.dependency.enums.CommandType;
import com.salesforce.tests.dependency.models.ComponentCommand;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

import java.util.Arrays;

public class DefaultComponentManagementServiceTest {

    private DefaultComponentManagementService defaultComponentManagementService = new DefaultComponentManagementService();

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().muteForSuccessfulTests().enableLog();

    @Test
    public void executeDependCommand() {
        ComponentCommand dependCommand = new ComponentCommand(CommandType.DEPEND, "TELNET", Arrays.asList("TCPIP", "NETCARD"));

        defaultComponentManagementService.decideAndExecute(dependCommand);
        Assert.assertEquals("", systemOutRule.getLogWithNormalizedLineSeparator());
    }

    @Test
    public void executeInstallCommand() {
        ComponentCommand installCommand = new ComponentCommand(CommandType.INSTALL, "TELNET");

        defaultComponentManagementService.decideAndExecute(installCommand);
        String expectedOutput = "Installing TELNET\n";
        Assert.assertEquals(expectedOutput, systemOutRule.getLogWithNormalizedLineSeparator());
    }

    @Test
    public void executeRemoveCommand() {
        ComponentCommand installCommand = new ComponentCommand(CommandType.INSTALL, "TELNET");

        defaultComponentManagementService.decideAndExecute(installCommand);
        ComponentCommand remove = new ComponentCommand(CommandType.REMOVE, "TELNET");

        defaultComponentManagementService.decideAndExecute(remove);
        String expectedOutput = "Installing TELNET\n" +
                "Removing TELNET\n";
        Assert.assertEquals(expectedOutput, systemOutRule.getLogWithNormalizedLineSeparator());
    }

    @Test
    public void executeListCommand() {
        ComponentCommand command = new ComponentCommand(CommandType.INSTALL, "TELNET");
        defaultComponentManagementService.decideAndExecute(command);
        command = new ComponentCommand(CommandType.INSTALL, "TCP_IP");
        defaultComponentManagementService.decideAndExecute(command);
        String expectedOutput = "Installing TELNET\n" +
                "Installing TCP_IP\n" +
                "TELNET\n" +
                "TCP_IP\n";
        command = new ComponentCommand(CommandType.LIST);
        defaultComponentManagementService.decideAndExecute(command);
        Assert.assertEquals(expectedOutput, systemOutRule.getLogWithNormalizedLineSeparator());
    }
}