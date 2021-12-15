package com.findTutor.findTutor.service;

import com.findTutor.findTutor.controller.tutors.model.TutorCreateRequest;
import com.findTutor.findTutor.controller.tutors.model.TutorCreateUpdate;
import com.findTutor.findTutor.controller.tutors.model.TutorResponse;
import com.findTutor.findTutor.database.tutor.model.DBTutor;
import com.findTutor.findTutor.database.tutor.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TutorService {

    private final TutorRepository tutorRepository;

    @Autowired
    public TutorService(TutorRepository tutorRepository) {
        this.tutorRepository = tutorRepository;
    }

    public List<TutorResponse> getTutors(){

        // 1. Get list of DBTutors from DB

        List<DBTutor> tutors=tutorRepository.findAll();

        // 2. Convert this list into list of TutorResponse

        List<TutorResponse> tutorResponses=new ArrayList<>();
        tutors.forEach(dbTutor -> {
            TutorResponse tutorResponse=new TutorResponse();
            tutorResponse.setId(dbTutor.getId());
            tutorResponse.setFirstName(dbTutor.getFirstName());
            tutorResponse.setLastName(dbTutor.getLastName());
            tutorResponse.setUsername(dbTutor.getUsername());
            tutorResponse.setEmail(dbTutor.getEmail());
            tutorResponse.setCity(dbTutor.getCity());
            tutorResponse.setPhoneNumber(dbTutor.getPhoneNumber());

            tutorResponses.add(tutorResponse);
        });

        // 3. return this new list

        return tutorResponses;
    }

    public void addNewTutor(TutorCreateRequest tutorCreateRequest) {

        // Validacija request body-a prije nego sto se spasi u bazu
        // validirat da nijedno polje nije null
        // ako jeste, bacit IllegalStateException

        if(tutorCreateRequest.getFirstName()==null){
            throw new IllegalStateException("value is null");
        }
        if(tutorCreateRequest.getLastName()==null){
            throw new IllegalStateException("value is null");
        }
        if(tutorCreateRequest.getUsername()==null){
            throw new IllegalStateException("value is null");
        }
        if(tutorCreateRequest.getPassword()==null){
            throw new IllegalStateException("value is null");
        }
        if(tutorCreateRequest.getEmail()==null){
            throw new IllegalStateException("value is null");
        }
        if(tutorCreateRequest.getCity()==null){
            throw new IllegalStateException("value is null");
        }
        if(tutorCreateRequest.getPhoneNumber()==null){
            throw new IllegalStateException("value is null");
        }


        Optional<DBTutor> tutorOptional=tutorRepository.findTutorByEmail((tutorCreateRequest.getEmail()));

        if(tutorOptional.isPresent()){
            throw new IllegalStateException("email taken");

        }else{
            DBTutor tutor=new DBTutor();

            tutor.setFirstName(tutorCreateRequest.getFirstName());
            tutor.setLastName(tutorCreateRequest.getLastName());
            tutor.setUsername(tutorCreateRequest.getUsername());
            tutor.setPassword(tutorCreateRequest.getPassword());
            tutor.setEmail(tutorCreateRequest.getEmail());
            tutor.setCity(tutorCreateRequest.getCity());
            tutor.setPhoneNumber(tutorCreateRequest.getPhoneNumber());

            tutorRepository.save(tutor);
        }
    }

    public void deleteTutor(Long tutorId){
        boolean exists=tutorRepository.existsById(tutorId);
        if(!exists){
            throw new IllegalStateException(
                    "tutor with id " + tutorId + "does not exist!");
        }
        tutorRepository.deleteById(tutorId);
    }

    // HTTP PATCH /id
    // Napravit klasu koja odgovara json body-u update metode
    // polja koja se mogu update: sva polja osim id i password
    // ako je poslan null ignorisati to polje

    // 1. iz baze ucitat tutora s odgovarajucim id-em
    // 2. ako polje iz requesta nije null, nasetat to polje na db tutora, ako je null - ignorisat
    // nakon sto si sva polja provjerila i nasetala, spasit updatean db tutor ponovo u bazu

    public void updateTutor(TutorCreateUpdate tutorCreateUpdate){

        Optional <DBTutor> tutors= tutorRepository.findTutorById(tutorCreateUpdate.getId());

        DBTutor tutor=new DBTutor();

        if(tutorCreateUpdate.getFirstName()!=null){
            tutor.setFirstName(tutorCreateUpdate.getFirstName());
        }
        if (tutorCreateUpdate.getLastName()!=null){
            tutor.setLastName(tutorCreateUpdate.getLastName());
        }
        if(tutorCreateUpdate.getUsername()!=null){
            tutor.setUsername(tutorCreateUpdate.getUsername());
        }
        if(tutorCreateUpdate.getEmail()!=null){
            tutor.setEmail(tutorCreateUpdate.getEmail());
        }
        if(tutorCreateUpdate.getCity()!=null){
            tutor.setCity(tutorCreateUpdate.getCity());
        }
        if(tutorCreateUpdate.getPhoneNumber()!=null){
            tutor.setPhoneNumber(tutorCreateUpdate.getPhoneNumber());
        }
        tutorRepository.save(tutor);
    }


}
