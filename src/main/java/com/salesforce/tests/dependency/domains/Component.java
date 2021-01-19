package com.salesforce.tests.dependency.domains;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class Component {

    private String componentName;
    private boolean explicitInstall;

    public Component(String componentName) {
        this.componentName = componentName;
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
        return Objects.hash(componentName);
    }
}
