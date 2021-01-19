package com.salesforce.tests.dependency.service;

import com.salesforce.tests.dependency.domains.Component;
import com.salesforce.tests.dependency.enums.CommandType;
import com.salesforce.tests.dependency.models.ComponentCommand;
import com.salesforce.tests.dependency.models.ComponentGraph;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

import java.util.Arrays;

public class DefaultDependencyServiceTest {
    private DefaultDependencyService defaultDependencyService;
    private ComponentGraph componentGraph;
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().muteForSuccessfulTests().enableLog();

    @Before
    public void setup() {
        componentGraph = new ComponentGraph();
        defaultDependencyService = new DefaultDependencyService(componentGraph);
    }

    @Test
    public void addDependencies() {
        defaultDependencyService.addDependencies(new ComponentCommand(CommandType.DEPEND, "item1", Arrays.asList("item2", "item3")));
        Assertions.assertThat(componentGraph.getDependencies().get(new Component("item1")))
                .extracting(component -> component.getComponentName())
                .contains("item2", "item3");
    }

    @Test
    public void shouldIgnoreCommandIfCyclicDependency() {
        defaultDependencyService.addDependencies(new ComponentCommand(CommandType.DEPEND, "item1", Arrays.asList("item2")));
        defaultDependencyService.addDependencies(new ComponentCommand(CommandType.DEPEND, "item2", Arrays.asList("item1")));
        Assertions.assertThat("item1 depends on item2, ignoring commandType\n").isEqualTo(systemOutRule.getLogWithNormalizedLineSeparator());
    }
}