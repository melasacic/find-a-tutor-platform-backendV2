package com.findTutor.findTutor.service;

import com.findTutor.findTutor.database.Tutor;
import com.findTutor.findTutor.database.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TutorService {

    private final TutorRepository tutorRepository;

    @Autowired
    public TutorService(TutorRepository tutorRepository) {
        this.tutorRepository = tutorRepository;
    }

    public List<Tutor> getTutors(){
        return tutorRepository.findAll();
    }

    public void addNewTutor(Tutor tutor) {
        Optional<Tutor> tutorOptional=tutorRepository
                .findTutorByEmail((tutor.getEmail()));
        if(tutorOptional.isPresent()){
            throw new IllegalStateException("email taken");
        }
        tutorRepository.save(tutor);

    }

    public void deleteTutor(Long tutorId){
        boolean exists=tutorRepository.existsById(tutorId);
        if(!exists){
            throw new IllegalStateException(
                    "tutor with id " + tutorId + "does not exist!");
        }
        tutorRepository.deleteById(tutorId);
    }
}
