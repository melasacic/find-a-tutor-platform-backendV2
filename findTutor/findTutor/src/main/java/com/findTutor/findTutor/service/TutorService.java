package com.findTutor.findTutor.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.findTutor.findTutor.controller.tutors.model.*;
import com.findTutor.findTutor.database.tutor.TutorRepository;
import com.findTutor.findTutor.database.tutor.model.DBTutor;
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

    public List<TutorResponse> getTutors() {

        // 1. Get list of DBTutors from DB

        List<DBTutor> tutors = tutorRepository.findAll();

        // 2. Convert this list into list of TutorResponse

        List<TutorResponse> tutorResponses = new ArrayList<>();
        tutors.forEach(dbTutor -> {
            TutorResponse tutorResponse = new TutorResponse();
            tutorResponse.setId(dbTutor.getId());
            tutorResponse.setFirstName(dbTutor.getFirstName());
            tutorResponse.setLastName(dbTutor.getLastName());
            tutorResponse.setUsername(dbTutor.getUsername());
            tutorResponse.setEmail(dbTutor.getEmail());
            tutorResponse.setCity(dbTutor.getCity());
            tutorResponse.setPhoneNumber(dbTutor.getPhoneNumber());
            tutorResponse.setRating(dbTutor.getRating());

            tutorResponses.add(tutorResponse);
        });

        // 3. return this new list

        return tutorResponses;
    }

    public TutorResponse getTutorDetails(String token) {
        DecodedJWT jwt = JWT.decode(token);
        String type = jwt.getClaim("type").asString();

        if (!"tutor".equals(type)) {
            throw new IllegalStateException("Invalid token type");
        }

        String tutorId = jwt.getClaim("id").toString();

        DBTutor dbTutor = tutorRepository.getById(Long.valueOf(tutorId));
        TutorResponse tutorResponse = new TutorResponse();
        tutorResponse.setId(dbTutor.getId());
        tutorResponse.setFirstName(dbTutor.getFirstName());
        tutorResponse.setLastName(dbTutor.getLastName());
        tutorResponse.setUsername(dbTutor.getUsername());
        tutorResponse.setEmail(dbTutor.getEmail());
        tutorResponse.setCity(dbTutor.getCity());
        tutorResponse.setPhoneNumber(dbTutor.getPhoneNumber());
        tutorResponse.setRating(dbTutor.getRating());

        return tutorResponse;
    }

    public void addNewTutor(TutorCreateRequest tutorCreateRequest) {

        // Validacija request body-a prije nego sto se spasi u bazu
        // validirat da nijedno polje nije null
        // ako jeste, bacit IllegalStateException

        if (tutorCreateRequest.getFirstName() == null) {
            throw new IllegalStateException("first name can not be null");
        }
        if (tutorCreateRequest.getLastName() == null) {
            throw new IllegalStateException("last name can not be null");
        }
        if (tutorCreateRequest.getUsername() == null) {
            throw new IllegalStateException("username can not be null");
        }
        if (tutorCreateRequest.getPassword() == null) {
            throw new IllegalStateException("password can not be null null");
        }
        if (tutorCreateRequest.getEmail() == null) {
            throw new IllegalStateException("email can not be null");
        }
        if (tutorCreateRequest.getCity() == null) {
            throw new IllegalStateException("city can not be null");
        }
        if (tutorCreateRequest.getPhoneNumber() == null) {
            throw new IllegalStateException("phone number can not be null");
        }


        Optional<DBTutor> tutorOptional = tutorRepository.findTutorByEmail((tutorCreateRequest.getEmail()));

        if (tutorOptional.isPresent()) {
            throw new IllegalStateException("email taken");
        } else {
            DBTutor tutor = new DBTutor();

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

    public void deleteTutor(Long tutorId) {
        boolean exists = tutorRepository.existsById(tutorId);
        if (!exists) {
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

    public void updateTutor(Long tutorId, TutorCreateUpdate tutorCreateUpdate) {

        Optional<DBTutor> tutors = tutorRepository.findById(tutorId);

        DBTutor tutorUpdate = tutors.get();

        if (tutorCreateUpdate.getFirstName() != null) {
            tutorUpdate.setFirstName(tutorCreateUpdate.getFirstName());
        }
        if (tutorCreateUpdate.getLastName() != null) {
            tutorUpdate.setLastName(tutorCreateUpdate.getLastName());
        }
        if (tutorCreateUpdate.getUsername() != null) {
            tutorUpdate.setUsername(tutorCreateUpdate.getUsername());
        }
        if (tutorCreateUpdate.getEmail() != null) {
            tutorUpdate.setEmail(tutorCreateUpdate.getEmail());
        }
        if (tutorCreateUpdate.getCity() != null) {
            tutorUpdate.setCity(tutorCreateUpdate.getCity());
        }
        if (tutorCreateUpdate.getPhoneNumber() != null) {
            tutorUpdate.setPhoneNumber(tutorCreateUpdate.getPhoneNumber());
        }
        tutorRepository.save(tutorUpdate);
    }

    public LoginResponse getTutorToken(LoginTutorModel model) {
        DBTutor tutor = tutorRepository.findByEmailAndPassword(model.getEmail(), model.getPassword())
                .orElseThrow(() -> new IllegalStateException("Invalid username or password"));

        Algorithm algorithm = Algorithm.HMAC256("secret");
        String token = JWT.create()
                .withIssuer("tutorServer")
                .withClaim("id", tutor.getId())
                .withClaim("type", "tutor")
                .sign(algorithm);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        return loginResponse;
    }

    public void validateToken(String token, String id) {
        DecodedJWT jwt = JWT.decode(token);

        String type = jwt.getClaim("type").toString();

        if (type.equals("tutor") && !id.equals(jwt.getClaim("id").toString())) {
            throw new IllegalStateException("Invalid token");
        }
    }

    public void validateType(String token, String expectedType) {
        DecodedJWT jwt = JWT.decode(token);
        String type = jwt.getClaim("type").asString();

        if (!expectedType.equals(type)) {
            throw new IllegalStateException("Invalid token type " + token);
        }
    }

    public DBTutor rateTutor(RatingModel ratingModel, Long id) {
        DBTutor dbTutor = tutorRepository.getById(id);
        if (dbTutor == null) {
            throw new IllegalStateException("Tutor with id does not exist " + id);
        }

        if(dbTutor.getRating() == null){
            dbTutor.setRating(ratingModel.getRating());
        }else{
            Double newRating = (dbTutor.getRating() + ratingModel.getRating()) / 2;
            dbTutor.setRating(newRating);
        }

        tutorRepository.save(dbTutor);
        return dbTutor;
    }
}
