package com.salesforce.tests.dependency.parsing;

public interface Parser<T> {

    T parse(String input);
}
