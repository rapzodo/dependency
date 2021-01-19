package com.salesforce.tests.dependency.models;

import com.salesforce.tests.dependency.domains.Component;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ComponentGraph {

    private int size;

    private Map<Component, List<Component>> dependencies = new HashMap<>();

    public void addComponent(Component component) {
        dependencies.put(component, new ArrayList<>());
        size++;
    }

    public void addDependency(Component component, Component dependency) {
        final List<Component> components = dependencies.get(component);
        if (components == null) {
            throw new RuntimeException("You need to add component first");
        }
        components.add(dependency);
    }

    public List<Component> getComponentDependencies(Component component){
        return dependencies.get(component);
    }
}
