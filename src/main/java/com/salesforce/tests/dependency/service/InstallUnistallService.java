package com.salesforce.tests.dependency.service;

import com.salesforce.tests.dependency.models.ComponentCommand;

import java.util.List;
import java.util.Set;

public interface InstallUnistallService {

    List<String> installComponent(ComponentCommand componentCommand);

    List<String> uninstallComponent(ComponentCommand dependCommand);

    Set<String> listInstalledComponentNames();
}
