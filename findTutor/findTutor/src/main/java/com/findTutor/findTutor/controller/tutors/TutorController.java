package com.findTutor.findTutor.controller.tutors;

import com.findTutor.findTutor.controller.tutors.model.*;
import com.findTutor.findTutor.service.TutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/tutor")
public class TutorController {

    private final TutorService tutorService;

    @Autowired
    public TutorController(TutorService tutorService) {
        this.tutorService = tutorService;
    }

    @GetMapping
    public List<TutorResponse> getTutors() {
        return tutorService.getTutors();
    }

    // we take RequestBody and then we map it into an tutor
    @PostMapping
    public void registerNewTutor(@RequestBody TutorCreateRequest tutorCreateRequest) {
        tutorService.addNewTutor(tutorCreateRequest);
    }

    @DeleteMapping(path = "{tutorId}")
    public ResponseEntity deleteTutor(@PathVariable("tutorId") Long id, @RequestHeader("Authorization") String token) {
        try {
            tutorService.validateToken(token, id.toString());
            tutorService.deleteTutor(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @PatchMapping("/{tutorId}")
    public ResponseEntity updateTutor(
            @PathVariable("tutorId") Long id,
            @RequestBody TutorCreateUpdate tutorCreateUpdate,
            @RequestHeader("Authorization") String token) {
        try {
            tutorService.validateToken(token, id.toString());
            tutorService.updateTutor(id, tutorCreateUpdate);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/token")
    public ResponseEntity<LoginResponse> getTutorToken(@RequestBody LoginTutorModel loginTutorModel) {
        try {
            return ResponseEntity.ok(tutorService.getTutorToken(loginTutorModel));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}