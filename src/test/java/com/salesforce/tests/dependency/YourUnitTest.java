package com.salesforce.tests.dependency;

import com.salesforce.tests.dependency.domains.Component;
import com.salesforce.tests.dependency.enums.CommandType;
import com.salesforce.tests.dependency.models.ComponentCommand;
import com.salesforce.tests.dependency.parsing.CommandLineParser;
import com.salesforce.tests.dependency.service.DefaultComponentManagementService;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Place holder for your unit tests
 */
public class YourUnitTest {

    private DefaultComponentManagementService service = new DefaultComponentManagementService();

    private CommandLineParser commandParser = new CommandLineParser();

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

    @Test
    public void shouldExecuteDependCommand() {
        ComponentCommand dependCommand = new ComponentCommand(CommandType.DEPEND, "TELNET", Arrays.asList("TCPIP", "NETCARD"));
        final List<Component> dependencies = Arrays.asList(new Component("TCPIP"), new Component("NETCARD"));
        final Component expectedComponent = new Component("TELNET", dependencies);
        service.executeDepend(dependCommand);
        final Component component = service.getDependenciesGraph().get("TELNET");
        assertThat(component).isEqualToComparingFieldByField(expectedComponent);
        assertThat(component.getDependencies().get(0).getComponentName()).isEqualTo("TCPIP");
        assertThat(component.getDependencies().get(1).getComponentName()).isEqualTo("NETCARD");
    }

    @Test
    public void shouldIgnoreDependCommandWhenCyclicDependency() {
        ComponentCommand dependCommand = new ComponentCommand(CommandType.DEPEND, "item1", Arrays.asList("item2", "item3"));
        ComponentCommand dependCommand2 = new ComponentCommand(CommandType.DEPEND, "item2", Arrays.asList("item1"));
        service.executeDepend(dependCommand);
        service.executeDepend(dependCommand2);
        assertThat(service.getDependenciesGraph().get("item1")).isNotNull();
        assertThat(service.getDependenciesGraph().get("item2")).isNull();
    }

    @Test
    public void shouldExecuteInstallCommand() {
        ComponentCommand dependCommand = new ComponentCommand(CommandType.DEPEND, "item1", Arrays.asList("item2", "item3"));
        ComponentCommand dependCommand2 = new ComponentCommand(CommandType.DEPEND, "item2", Arrays.asList("item4"));
        service.executeDepend(dependCommand);
        service.executeDepend(dependCommand2);

        List<String> expectedOutputs = Arrays.asList("Installing item4", "Installing item2", "Installing item3", "Installing item1");
        ComponentCommand installCommand = new ComponentCommand(CommandType.INSTALL, "item1");
        assertThat(service.executeInstall(installCommand)).hasSameElementsAs(expectedOutputs);
    }

    @Test
    public void shouldExecuteInstallCommandWithNoDependencies() {
        List<String> expectedOutputs = Arrays.asList("Installing compWithNoDependencies");
        ComponentCommand installCommand = new ComponentCommand(CommandType.INSTALL, "compWithNoDependencies");
        assertThat(service.executeInstall(installCommand)).hasSameElementsAs(expectedOutputs);
    }

    @Test
    public void shouldExecuteListCommand() {
        ComponentCommand dependCommand = new ComponentCommand(CommandType.DEPEND, "item1", Arrays.asList("item2", "item3"));
        ComponentCommand dependCommand2 = new ComponentCommand(CommandType.DEPEND, "item2", Arrays.asList("item4"));
        service.executeDepend(dependCommand);
        service.executeDepend(dependCommand2);

        ComponentCommand installCommand = new ComponentCommand(CommandType.INSTALL, "item1");
        service.executeInstall(installCommand);

        List<String> expectedOutputs = Arrays.asList("item4", "item2", "item3", "item1");
        assertThat(service.executeList(new ComponentCommand(CommandType.LIST))).hasSameElementsAs(expectedOutputs);
    }

    @Test
    public void shouldExecuteRemoveCommand() {
        ComponentCommand dependCommand = new ComponentCommand(CommandType.DEPEND, "item1", Arrays.asList("item2", "item3"));
        ComponentCommand dependCommand2 = new ComponentCommand(CommandType.DEPEND, "item2", Arrays.asList("item4"));
        service.executeDepend(dependCommand);
        service.executeDepend(dependCommand2);

        ComponentCommand installCommand = new ComponentCommand(CommandType.INSTALL, "item1");
        service.executeInstall(installCommand);

        List<String> expectedOutputs = Arrays.asList("item4", "item2", "item3", "item1");
        assertThat(service.executeList(new ComponentCommand(CommandType.LIST))).hasSameElementsAs(expectedOutputs);
    }
}