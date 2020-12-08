package com.salesforce.tests.dependency.domains;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class Component {

    private String componentName;
    private List<Component> dependencies;
    private boolean explicitInstall;

    public Component(String componentName) {
        this.componentName = componentName;
    }

    public Component(String componentName, List<Component> dependencies) {
        this.componentName = componentName;
        this.dependencies = dependencies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Component component = (Component) o;
        return componentName.equals(component.componentName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(componentName, explicitInstall);
    }
}
