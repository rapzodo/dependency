package com.salesforce.tests.dependency.models;

import com.salesforce.tests.dependency.enums.CommandType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComponentCommand {

    private CommandType commandType;
    private String componentName;
    private List<String> dependencies;

    public ComponentCommand(CommandType commandType) {
        this.commandType = commandType;
    }

    public ComponentCommand(CommandType commandType, String componentName) {
        this.commandType = commandType;
        this.componentName = componentName;
    }
}