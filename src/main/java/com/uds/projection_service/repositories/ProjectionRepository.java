package com.uds.projection_service.repositories;

import com.uds.projection_service.models.Projection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectionRepository extends MongoRepository<Projection, String> {

    // Tu peux ajouter ici des méthodes personnalisées si besoin, par exemple :
    // List<Projection> findByTitre(String titre);

}

