package com.salesforce.tests.dependency.service;

import com.salesforce.tests.dependency.models.ComponentCommand;

import java.util.List;
import java.util.Set;

public interface ComponentManagementService {

    void executeCommand(ComponentCommand componentCommand);

    Set<String> executeList(ComponentCommand componentCommand);

    void executeDepend(ComponentCommand dependCommand);

    List<String> executeInstall(ComponentCommand dependCommand);

    List<String> executeRemove(ComponentCommand dependCommand);
}
