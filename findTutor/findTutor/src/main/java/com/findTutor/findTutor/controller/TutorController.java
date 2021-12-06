package com.findTutor.findTutor.controller;

import com.findTutor.findTutor.database.Tutor;
import com.findTutor.findTutor.service.TutorService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Tutor> getTutors() {
        return tutorService.getTutors();
    }

    // we take RequestBody and then we map it into an tutor
    @PostMapping
    public void registerNewTutor (@RequestBody Tutor tutor){
        tutorService.addNewTutor(tutor);
    }

    @DeleteMapping(path="{tutorId}")
    public void deleteTutor(@PathVariable("tutorId") Long id){
        tutorService.deleteTutor(id);
    }
}