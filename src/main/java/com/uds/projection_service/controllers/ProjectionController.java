package com.uds.projection_service.controllers;

import com.uds.projection_service.models.Film;
import com.uds.projection_service.models.Projection;
import com.uds.projection_service.services.ProjectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/projections")
@RequiredArgsConstructor
public class ProjectionController {

    private final ProjectionService projectionService;

    @PostMapping
    public ResponseEntity<Projection> createProjection(@RequestBody Projection projection) {
         Projection saved = projectionService.createProjection(projection);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Projection>> getAllProjections() {
        return ResponseEntity.ok(projectionService.getAllProjections());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Projection> getProjectionById(@PathVariable String id) {
        return projectionService.getProjectionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Projection> updateProjection(@PathVariable String id, @RequestBody Projection projection) {
        try {
            System.out.println(projection.toString());
            Projection updated = projectionService.updateProjection(id, projection);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjection(@PathVariable String id) {
        projectionService.deleteProjection(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/add-film")
    public ResponseEntity<Projection> addFilmToProjection(
        @PathVariable String id,
        @RequestPart("film") Film film,
        @RequestPart(value = "file", required = false) MultipartFile file) {
    try {
        Projection updated = projectionService.addFilmToProjection(id, film, file);
        return ResponseEntity.ok(updated);
    } catch (RuntimeException e) {
        return ResponseEntity.notFound().build();
    }
}


}

