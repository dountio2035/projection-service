package com.uds.projection_service.controllers;

import com.uds.projection_service.models.Film;
import com.uds.projection_service.models.Projection;
import com.uds.projection_service.services.ProjectionService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
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

    @Value("${API.HOST}")
    String apiHost;
    @Value("${API.VIDEOS_UPLOADED_DIR}")
    String videosUploadedDir;
    @Value("${API.IMAGES_UPLOADED_DIR}")
    String imagesUploadedDir;

    @PostMapping
    public ResponseEntity<Object> createProjection(
            @RequestParam("title") String title,
            @RequestParam("date") String dateDiff,
            @RequestParam("duration") String duration,
            @RequestParam("visibility") String visibility,
            @RequestParam("price") String price,
            @RequestParam("actors") String actors,
            @RequestParam("storyling") String storyling,
            @RequestParam("image") MultipartFile image) {

        response.clear();

        if (image.isEmpty()) {
            response.put("status_code", 400);
            response.put("message", "Please select an image file to upload");
            return ResponseEntity.badRequest().body(null);
        }
        if (!image.getContentType().startsWith("image/")) {
            response.put("status_code", 400);
            response.put("message", "Selected file must be an image with extension [.png,.jpg,.jpeg]");
            return ResponseEntity.badRequest().body(null);
        }
        String imageURI;
        try {
            // Save the image file to the server
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
            Path path = Paths.get(imagesUploadedDir + fileName);
            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            imageURI = apiHost + "/images/" + fileName;

        } catch (Exception e) {

            response.put("status_code", 400);
            response.put("message", "Selected file must be an image with extension [.png,.jpg,.jpeg]");
            return ResponseEntity.badRequest().body(null);

        }
        // create film
        Film film = new Film();
        film.setTitle(title);
        film.setStoryling(storyling);
        film.setActors(actors);

        // Create a new Projection object
        Projection projection = new Projection();
        projection.setDateDif(LocalDateTime.parse(dateDiff));
        projection.setDuration(Integer.parseInt(duration));
        projection.setVisibility(visibility);
        projection.setPrice(Float.parseFloat(price));
        projection.setFilm(film);
        projection.setImgProj(imageURI);

        try {

            // Save the projection using the service
            Projection savedProjection = projectionService.createProjection(projection);
            response.put("status_code", 201);
            response.put("message", "Projection created successfully");
            response.put("projection", savedProjection);
            return ResponseEntity.status(201).body(response);

        } catch (RuntimeException e) {
            response.put("status_code", 400);
            response.put("message", "We got an error while saving the projection");
            return ResponseEntity.badRequest().body(null);
        }

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
    @PutMapping("/{projection_id}/video")
    public ResponseEntity<Object> addVideoToProjection(
            @PathVariable String projection_id,
            @RequestParam("video") MultipartFile video) {

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
            String fileName = projection_id.concat(
                    "." + StringUtils.getFilenameExtension(Objects.requireNonNull(video.getOriginalFilename())));
            Path path = Paths.get(videosUploadedDir + fileName);
            Files.copy(video.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            videoURI = apiHost + "/videos/" + fileName;

            try {
                // update the projection with the video URI
                projectionService.addVideoToProjection(projection_id, videoURI);
                response.put("status_code", 400);
                response.put("message", "video added successfully");
                response.put("video_uri", videoURI);
                return ResponseEntity.badRequest().body(response);
            } catch (Exception e) {
                response.put("status_code", 400);
                response.put("message", "We got an error while saving the video");
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
