package com.uds.projection_service.models;

import java.util.UUID;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Film {
    private String idFilm = UUID.randomUUID().toString(); 
    private String title;
    private String storyling;
    private String language;
    private String actors;
    @Field("category")
    private Category category;
    private String video;
}

