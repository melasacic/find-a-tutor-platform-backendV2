package com.findTutor.findTutor.controller.tutors;

import com.findTutor.findTutor.controller.tutors.model.TutorCreateRequest;
import com.findTutor.findTutor.controller.tutors.model.TutorCreateUpdate;
import com.findTutor.findTutor.controller.tutors.model.TutorResponse;
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
    public List<TutorResponse> getTutors() {
        return tutorService.getTutors();
    }

    // we take RequestBody and then we map it into an tutor
    @PostMapping
    public void registerNewTutor(@RequestBody TutorCreateRequest tutorCreateRequest) {
        tutorService.addNewTutor(tutorCreateRequest);
    }

    @DeleteMapping(path = "{tutorId}")
    public void deleteTutor(@PathVariable("tutorId") Long id) {
        tutorService.deleteTutor(id);
    }


   @PatchMapping("/{tutorId}")
    public void updateTutor(@PathVariable("tutorId") Long id,@RequestBody TutorCreateUpdate tutorCreateUpdate) {
        tutorService.updateTutor(id, tutorCreateUpdate);
    }
}