package com.salesforce.tests.dependency.service;

import com.salesforce.tests.dependency.models.ComponentCommand;
import com.salesforce.tests.dependency.models.ComponentGraph;

public class DefaultComponentManagementService implements ComponentManagementService {

    private InstallUnistallService installUnistallService;
    private DependencyService dependencyService ;

    public DefaultComponentManagementService(){
        ComponentGraph componentGraph = new ComponentGraph();
        dependencyService = new DefaultDependencyService(componentGraph);
        installUnistallService = new DefaultInstallUnistallService(dependencyService, componentGraph);
    }

    @Override
    public void decideAndExecute(ComponentCommand componentCommand) {
        switch (componentCommand.getCommandType()) {
            case DEPEND:
                dependencyService.addDependencies(componentCommand);
                break;
            case INSTALL:
                installUnistallService.installComponent(componentCommand).forEach(System.out::println);
                break;
            case LIST:
                installUnistallService.listInstalledComponentNames().forEach(System.out::println);
                break;
            case REMOVE:
                installUnistallService.uninstallComponent(componentCommand).forEach(System.out::println);
                break;
        }
    }
}
