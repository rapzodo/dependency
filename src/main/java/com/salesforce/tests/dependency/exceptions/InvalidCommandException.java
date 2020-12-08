package com.salesforce.tests.dependency.exceptions;

public class InvalidCommandException extends RuntimeException {

    public InvalidCommandException() {
        super("Invalid commandType");
    }
}
