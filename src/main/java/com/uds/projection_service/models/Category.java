package com.uds.projection_service.models;

import java.util.UUID;


import lombok.Data;

@Data
public class Category {
    private String id = UUID.randomUUID().toString(); 
    private String name;
}
