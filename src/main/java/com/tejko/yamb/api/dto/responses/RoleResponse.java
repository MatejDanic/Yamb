package com.tejko.yamb.api.dto.responses;

public class RoleResponse {

    private String name;
    private String description;

    public RoleResponse() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
