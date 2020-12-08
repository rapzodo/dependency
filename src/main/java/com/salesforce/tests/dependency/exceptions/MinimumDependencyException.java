package com.salesforce.tests.dependency.exceptions;

public class MinimumDependencyException extends RuntimeException {

    public MinimumDependencyException() {
        super("You must provide at least one dependency within DEPEND commandType");
    }
}
