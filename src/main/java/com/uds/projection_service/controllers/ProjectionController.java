package com.uds.projection_service.controllers;

import com.uds.projection_service.models.Projection;
import com.uds.projection_service.services.ProjectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/projections")
@RequiredArgsConstructor
public class ProjectionController {

    private final ProjectionService projectionService;
    Map<String, Object> response = new HashMap<>();

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

    // end point to update projection an add a video: for perform good optimisation,
    // video will be send allone
    @PutMapping("/{projection_id}")
    public ResponseEntity<Object> addVideoToProjection(
            @RequestParam("video") MultipartFile video,
            @RequestParam("projection_id") Long projection_id) {

        response.clear();
        String videoURI;

        if (video.isEmpty()) {
            response.put("status_code", 400);
            response.put("message", "Please select a video file to upload");
            return ResponseEntity.badRequest().body(response);
        }
        if (projectionService.getProjectionById(projection_id.toString()).isEmpty()) {
            response.put("status_code", 404);
            response.put("message", "Projection not found");
            return ResponseEntity.status(404).body(response);
        }
        if (!video.getContentType().startsWith("video/")) {
            response.put("status_code", 400);
            response.put("message", "Selected file must be a video with extension [.mp4,.avi,.mov]");
            return ResponseEntity.badRequest().body(response);
        }
        try {
            // Save the video file to the server
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(video.getOriginalFilename()));
            String uploadDir = "${API.VIDEOS_UPLOADED_DIR}";
            Path path = Paths.get(uploadDir + fileName);
            Files.copy(video.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            videoURI = "${API.HOST}" + "/projection/videos/" + fileName;

            try {

                response.put("status_code", 400);
                response.put("message", "video added successfully");
                response.put("video_uri", videoURI);
                return ResponseEntity.badRequest().body(response);
            } catch (Exception e) {
                response.put("status_code", 400);
                response.put("message", "We got an error while saving the image");
                return ResponseEntity.badRequest().body(response);
            }

        } catch (Exception e) {
            response.put("status_code", 400);
            response.put("message", "Selected file must be an image with extension [.png,.jpg,.jpeg]");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjection(@PathVariable String id) {
        projectionService.deleteProjection(id);
        return ResponseEntity.noContent().build();
    }
}
