package com.salesforce.tests.dependency.service;

import com.salesforce.tests.dependency.models.ComponentCommand;

public interface ComponentManagementService {

    void decideAndExecute(ComponentCommand componentCommand);
}
