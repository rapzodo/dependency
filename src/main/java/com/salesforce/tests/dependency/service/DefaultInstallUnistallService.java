package com.salesforce.tests.dependency.service;

import com.salesforce.tests.dependency.domains.Component;
import com.salesforce.tests.dependency.enums.CommandType;
import com.salesforce.tests.dependency.models.ComponentCommand;
import com.salesforce.tests.dependency.models.ComponentGraph;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
public class DefaultInstallUnistallService implements InstallUnistallService {

    @Getter
    private final Map<String, Component> installedComponents = new LinkedHashMap<>();
    private final DependencyService dependencyService;
    private final ComponentGraph componentGraph;

    @Override
    public List<String> installComponent(ComponentCommand componentCommand) {
        if (componentCommand.getCommandType() == CommandType.LIST) {
            return new ArrayList<>(installedComponents.keySet());
        }
        final String componentName = componentCommand.getComponentName();
        final Component component = new Component(componentName);
        if (this.componentGraph.getComponentDependencies(component) == null && !isComponentInstalled(componentName)) {
            component.setExplicitInstall(true);
            return Arrays.asList(executeInstallationSteps(component));
        }
        if (isComponentInstalled(componentName)) {
            return Arrays.asList(componentName + " is already installed");
        }
        final ArrayList<String> outputs = new ArrayList<>();
        installRecursive(component, outputs);
        component.setExplicitInstall(true);
        return outputs;
    }

    private void installRecursive(Component component, List<String> outputs) {
        if (Objects.isNull(component) || isComponentInstalled(component.getComponentName())) {
            return;
        }
        final List<Component> dependencies = componentGraph.getComponentDependencies(component);
        if (Objects.nonNull(dependencies)) {
            dependencies.forEach(dependency -> {
                if (isComponentWithNotDependencies(dependency) && !isComponentInstalled(dependency.getComponentName())) {
                    outputs.add(executeInstallationSteps(dependency));
                    return;
                }
                installRecursive(dependency, outputs);
            });
        }
        outputs.add(executeInstallationSteps(component));
    }

    private String executeInstallationSteps(Component component) {
        final String componentName = component.getComponentName();
        installedComponents.put(componentName, component);
        return "Installing " + componentName;
    }

    @Override
    public List<String> uninstallComponent(ComponentCommand dependCommand) {
        final ArrayList<String> outputs = new ArrayList<>();
        final String componentName = dependCommand.getComponentName();
        Component component = installedComponents.get(componentName);
        if(!isComponentInstalled(componentName)){
            return Arrays.asList(componentName + " is not installed");
        }
        if(hasDependents(component, componentGraph)){
            return Arrays.asList(componentName + " is still needed");
        }
        if(isComponentWithNotDependencies(component)){
            return Arrays.asList(executeUninstallSteps(new Component(componentName)));
        }
        outputs.add(executeUninstallSteps(component));
        removeRecursive(componentGraph.getComponentDependencies(component), outputs);
        return outputs;
    }

    private void removeRecursive(List<Component> dependencies, List<String> outputs) {
        if (Objects.isNull(dependencies)) {
            return;
        }
        if (Objects.nonNull(dependencies)) {
            dependencies.stream().forEach(dependency -> {
                final Component comp = installedComponents.get(dependency.getComponentName());
                if(comp.isExplicitInstall() || hasDependents(comp, componentGraph)){
                    return;
                }
                removeRecursive(componentGraph.getComponentDependencies(comp), outputs);
                outputs.add(executeUninstallSteps(comp));
            });
        }
    }

    private String executeUninstallSteps(Component component) {
        final String componentName = component.getComponentName();
        installedComponents.remove(componentName);
        return "Removing " + componentName;
    }


    @Override
    public Set<String> listInstalledComponentNames() {
        return installedComponents.keySet();
    }

    public boolean isComponentInstalled(String componentName) {
        return installedComponents.containsKey(componentName);
    }

    private boolean isComponentWithNotDependencies(Component component) {
        final List<Component> componentDependencies = componentGraph.getComponentDependencies(component);
        return Objects.isNull(component) || Objects.isNull(componentDependencies) || componentDependencies.isEmpty();
    }

    private boolean hasDependents(Component component, ComponentGraph componentGraph) {
        return installedComponents.values()
                .stream()
                .anyMatch(installedComponent -> {
                    final List<Component> componentDependencies = componentGraph.getComponentDependencies(installedComponent);
                    return Objects.nonNull(componentDependencies) && componentDependencies.contains(component);
                });
    }

}
