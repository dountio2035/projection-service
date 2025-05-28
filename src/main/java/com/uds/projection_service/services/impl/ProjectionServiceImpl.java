package com.uds.projection_service.services.impl;


import com.uds.projection_service.models.Projection;
import com.uds.projection_service.repositories.ProjectionRepository;
import com.uds.projection_service.services.ProjectionService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectionServiceImpl implements ProjectionService {

    private final ProjectionRepository projectionRepository;

    @Override
    public Projection createProjection(Projection projection) {
        return projectionRepository.save(projection);
    }

    @Override
    public List<Projection> getAllProjections() {
        return projectionRepository.findAll();
    }

    @Override
    public Optional<Projection> getProjectionById(String id) {
        return projectionRepository.findById(id);
    }

 @Override
public Projection updateProjection(String id, Projection updatedProjection) {
    return projectionRepository.findById(id)
            .map(projection -> {
              
                if (updatedProjection.getDateDif() != null) {
                    projection.setDateDif(updatedProjection.getDateDif());
                }
                if (updatedProjection.getDuration() != null) {
                    projection.setDuration(updatedProjection.getDuration());
                }
                if (updatedProjection.getImgProj() != null) {
                    projection.setImgProj(updatedProjection.getImgProj());
                }
                /* if (updatedProjection.getFilmId() != null) {
                    projection.setFilmId(updatedProjection.getFilmId());
                } */

                return projectionRepository.save(projection);
            })
            .orElseThrow(() -> new RuntimeException("Projection non trouvée avec l'id : " + id));
}


    @Override
    public void deleteProjection(String id) {
        projectionRepository.deleteById(id);
    }
}

