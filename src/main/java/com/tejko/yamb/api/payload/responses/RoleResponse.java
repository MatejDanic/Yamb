package com.tejko.yamb.api.payload.responses;

public class RoleResponse {

    private String name;
    private String description;

    public RoleResponse() {}

    public RoleResponse(String name, String description) {
        this.name = name;
        this.description = description;
    }

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
