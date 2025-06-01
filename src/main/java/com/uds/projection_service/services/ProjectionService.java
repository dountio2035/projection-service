package com.uds.projection_service.services;

import com.uds.projection_service.models.Projection;

import java.util.List;
import java.util.Optional;


public interface ProjectionService {

    Projection createProjection(Projection projection);

    List<Projection> getAllProjections();

    Optional<Projection> getProjectionById(String id);

    Projection updateProjection(String id, Projection projection);

    void deleteProjection(String id);

    Projection addVideoToProjection(String projectionId, String video_uri);
}
