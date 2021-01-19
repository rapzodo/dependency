package com.salesforce.tests.dependency.service;

import com.salesforce.tests.dependency.models.ComponentCommand;

public interface DependencyService {

    void addDependencies(ComponentCommand dependCommand);

}
