package com.salesforce.tests.dependency.service;

import com.salesforce.tests.dependency.domains.Component;
import com.salesforce.tests.dependency.models.ComponentCommand;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DefaultComponentManagementService implements ComponentManagementService {

    private Map<String, Component> dependenciesGraph = new HashMap<>();
    private Map<String, Component> installedComponents = new LinkedHashMap<>();

    @Override
    public void executeCommand(ComponentCommand componentCommand) {
        switch (componentCommand.getCommandType()) {
            case DEPEND:
                executeDepend(componentCommand);
                break;
            case INSTALL:
                executeInstall(componentCommand).stream().forEach(System.out::println);
                break;
            case REMOVE:
                executeRemove(componentCommand).stream().forEach(System.out::println);
                break;
            case LIST:
                executeList(componentCommand).stream().forEach(System.out::println);
                break;
        }
    }

    @Override
    public Set<String> executeList(ComponentCommand componentCommand) {
        return installedComponents.keySet();
    }

    @Override
    public void executeDepend(ComponentCommand dependCommand) {
        final String componentName = dependCommand.getComponentName();
        for (String dependency : dependCommand.getDependencies()) {
            if (checkForCyclicDependency(componentName, dependency)) {
                System.out.println(dependency + " depends on " + componentName + ", ignoring commandType");
                return;
            }
        }
        final Component component = new Component(componentName,
                dependCommand.getDependencies()
                        .stream()
                        .map(dependencyName -> {
                            final Component dependency = new Component(dependencyName);
                            return dependency;
                        })
                        .collect(Collectors.toList()));
        dependenciesGraph.put(component.getComponentName(), component);
    }

    private boolean checkForCyclicDependency(String parent, String dependency) {
        final Component foundDependency = dependenciesGraph.get(dependency);
        if (foundDependency == null) {
            return false;
        }
        if (Objects.isNull(foundDependency.getDependencies())) {
            return false;
        }
        return foundDependency.getDependencies().stream()
                .anyMatch(component -> component.getComponentName().equalsIgnoreCase(parent));
    }

    @Override
    public List<String> executeInstall(ComponentCommand dependCommand) {
        final ArrayList<String> outputs = new ArrayList<>();
        final String componentName = dependCommand.getComponentName();
        Component component = dependenciesGraph.get(componentName);
        if(isComponentWithNotDependencies(component) && !isComponentInstalled(componentName)){
            component = new Component(componentName);
            component.setExplicitInstall(true);
            outputs.add(executeInstallationSteps(component));
            return outputs;
        }
        if (isComponentInstalled(componentName)) {
            return Arrays.asList(componentName + " is already installed");
        }
        installRecursive(component, outputs);
        component.setExplicitInstall(true);
        return outputs;
    }

    private void installRecursive(Component component, List<String> outputs) {
        if (Objects.isNull(component) || isComponentInstalled(component.getComponentName())) {
            return;
        }
        final List<Component> dependencies = component.getDependencies();
        if (Objects.nonNull(dependencies)) {
            dependencies.stream().forEach(dependency -> {
                final Component comp = dependenciesGraph.get(dependency.getComponentName());
                if(isComponentWithNotDependencies(comp) && !isComponentInstalled(dependency.getComponentName())){
                    outputs.add(executeInstallationSteps(dependency));
                    return;
                }
                installRecursive(comp, outputs);
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
    public List<String> executeRemove(ComponentCommand dependCommand) {
        final ArrayList<String> outputs = new ArrayList<>();
        final String componentName = dependCommand.getComponentName();
        Component component = installedComponents.get(componentName);
        if(!isComponentInstalled(componentName)){
            return Arrays.asList(componentName + " is not installed");
        }
        if(hasDependents(component)){
            return Arrays.asList(componentName + " is still needed");
        }
        if(isComponentWithNotDependencies(component)){
            return Arrays.asList(executeUninstallSteps(new Component(componentName)));
        }
        outputs.add(executeUninstallSteps(component));
        removeRecursive(component.getDependencies(), outputs);
        return outputs;
    }

    private void removeRecursive(List<Component> dependencies, List<String> outputs) {
        if (Objects.isNull(dependencies)) {
            return;
        }
        if (Objects.nonNull(dependencies)) {
            dependencies.stream().forEach(dependency -> {
                final Component comp = installedComponents.get(dependency.getComponentName());
                if(comp.isExplicitInstall() || hasDependents(comp)){
                    return;
                }
                removeRecursive(comp.getDependencies(), outputs);
                outputs.add(executeUninstallSteps(comp));
            });
        }
    }

    private String executeUninstallSteps(Component component) {
        final String componentName = component.getComponentName();
        installedComponents.remove(componentName);
        return "Removing " + componentName;
    }

    private boolean hasDependents(Component component) {
        return installedComponents.values()
                .stream()
                .anyMatch(c -> Objects.nonNull(c.getDependencies()) && c.getDependencies().contains(component));
    }


    private boolean isComponentInstalled(String componentName) {
        return installedComponents.containsKey(componentName);
    }

    private boolean isComponentWithNotDependencies(Component component) {
        return Objects.isNull(component) || Objects.isNull(component.getDependencies()) || component.getDependencies().isEmpty() ;
    }

    public Map<String, Component> getDependenciesGraph() {
        return dependenciesGraph;
    }
}
