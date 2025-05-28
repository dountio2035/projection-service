package com.uds.projection_service.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// import java.time.LocalDate; 
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document(collection = "projections")
public class Projection {

    @Id
    private String id;

    private LocalDateTime dateDif;
    private Integer duration;
    private String imgProj;
    private Float price;
    private String visibility;
    @Field("film")
    private Film film; 

}


  