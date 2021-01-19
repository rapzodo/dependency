package com.salesforce.tests.dependency.service;

import com.salesforce.tests.dependency.domains.Component;
import com.salesforce.tests.dependency.models.ComponentCommand;
import com.salesforce.tests.dependency.models.ComponentGraph;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class DefaultDependencyService implements DependencyService {

    private final ComponentGraph componentGraph;

    @Override
    public void addDependencies(ComponentCommand dependCommand) {
        final String componentName = dependCommand.getComponentName();
        final Component component = new Component(componentName);
        componentGraph.addComponent(component);
        for (String dependency : dependCommand.getDependencies()) {
            if (checkForCyclicDependency(componentName, componentGraph.getComponentDependencies(new Component(dependency)))) {
                componentGraph.getDependencies().remove(component);
                System.out.println(dependency + " depends on " + componentName + ", ignoring commandType");
                return;
            }
        }
        dependCommand.getDependencies().forEach(dependencyName -> this.componentGraph.addDependency(component, new Component(dependencyName)));
    }

    private boolean checkForCyclicDependency(String parent, List<Component> dependencies) {
        if (Objects.isNull(dependencies)) {
            return false;
        }
        return dependencies.stream()
                .anyMatch(component -> component.getComponentName().equalsIgnoreCase(parent));
    }
}
