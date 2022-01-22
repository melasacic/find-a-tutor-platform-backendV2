package com.findTutor.findTutor.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.findTutor.findTutor.controller.users.model.LoginUserModel;
import com.findTutor.findTutor.controller.users.model.UserCreateRequest;
import com.findTutor.findTutor.controller.users.model.UserCreateUpdate;
import com.findTutor.findTutor.controller.users.model.UserResponse;
import com.findTutor.findTutor.database.user.UserRepository;
import com.findTutor.findTutor.database.user.model.DBUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> getUsers() {

        // 1. Get list of DBTutors from DB

        List<DBUser> users = userRepository.findAll();

        // 2. Convert this list into list of TutorResponse

        List<UserResponse> userResponses = new ArrayList<>();
        users.forEach(dbUser -> {
            UserResponse userResponse = new UserResponse();
            userResponse.setId(dbUser.getId());
            userResponse.setFirstName(dbUser.getFirstName());
            userResponse.setLastName(dbUser.getLastName());
            userResponse.setUsername(dbUser.getUsername());
            userResponse.setEmail(dbUser.getEmail());
            userResponse.setCity(dbUser.getCity());
            userResponse.setPhoneNumber(dbUser.getPhoneNumber());

            userResponses.add(userResponse);
        });

        // 3. return this new list

        return userResponses;
    }

    public void addNewUser(UserCreateRequest userCreateRequest) {

        // Validacija request body-a prije nego sto se spasi u bazu
        // validirat da nijedno polje nije null
        // ako jeste, bacit IllegalStateException

        if (userCreateRequest.getFirstName() == null) {
            throw new IllegalStateException("first name can not be null");
        }
        if (userCreateRequest.getLastName() == null) {
            throw new IllegalStateException("last name can not be null");
        }
        if (userCreateRequest.getUsername() == null) {
            throw new IllegalStateException("username can not be null");
        }
        if (userCreateRequest.getPassword() == null) {
            throw new IllegalStateException("password can not be null null");
        }
        if (userCreateRequest.getEmail() == null) {
            throw new IllegalStateException("email can not be null");
        }
        if (userCreateRequest.getCity() == null) {
            throw new IllegalStateException("city can not be null");
        }
        if (userCreateRequest.getPhoneNumber() == null) {
            throw new IllegalStateException("phone number can not be null");
        }


        Optional<DBUser> userOptional = userRepository.findUserByEmail((userCreateRequest.getEmail()));

        if (userOptional.isPresent()) {
            throw new IllegalStateException("email taken");
        } else {
            DBUser user = new DBUser();

            user.setFirstName(userCreateRequest.getFirstName());
            user.setLastName(userCreateRequest.getLastName());
            user.setUsername(userCreateRequest.getUsername());
            user.setPassword(userCreateRequest.getPassword());
            user.setEmail(userCreateRequest.getEmail());
            user.setCity(userCreateRequest.getCity());
            user.setPhoneNumber(userCreateRequest.getPhoneNumber());

            userRepository.save(user);
        }
    }

    public void deleteUser(Long userId) {
        boolean exists = userRepository.existsById(userId);
        if (!exists) {
            throw new IllegalStateException(
                    "tutor with id " + userId + "does not exist!");
        }
        userRepository.deleteById(userId);
    }

    // HTTP PATCH /id
    // Napravit klasu koja odgovara json body-u update metode
    // polja koja se mogu update: sva polja osim id i password
    // ako je poslan null ignorisati to polje

    // 1. iz baze ucitat tutora s odgovarajucim id-em
    // 2. ako polje iz requesta nije null, nasetat to polje na db tutora, ako je null - ignorisat
    // nakon sto si sva polja provjerila i nasetala, spasit updatean db tutor ponovo u bazu

    public void updateUser(Long userId, UserCreateUpdate userCreateUpdate) {

        Optional<DBUser> users = userRepository.findById(userId);

        DBUser userUpdate = users.get();

        if (userCreateUpdate.getFirstName() != null) {
            userUpdate.setFirstName(userCreateUpdate.getFirstName());
        }
        if (userCreateUpdate.getLastName() != null) {
            userUpdate.setLastName(userCreateUpdate.getLastName());
        }
        if (userCreateUpdate.getUsername() != null) {
            userUpdate.setUsername(userCreateUpdate.getUsername());
        }
        if (userCreateUpdate.getEmail() != null) {
            userUpdate.setEmail(userCreateUpdate.getEmail());
        }
        if (userCreateUpdate.getCity() != null) {
            userUpdate.setCity(userCreateUpdate.getCity());
        }
        if (userCreateUpdate.getPhoneNumber() != null) {
            userUpdate.setPhoneNumber(userCreateUpdate.getPhoneNumber());
        }
        userRepository.save(userUpdate);
    }

    public String getUserToken(LoginUserModel model) {
        DBUser user = userRepository.findByEmailAndPassword(model.getEmail(), model.getPassword())
                .orElseThrow(() -> new IllegalStateException("Invalid username or password"));

        Algorithm algorithm = Algorithm.HMAC256("secret");
        return JWT.create()
                .withIssuer("tutorServer")
                .withClaim("id", user.getId())
                .withClaim("type", "user")
                .sign(algorithm);
    }

    public void validateToken(String token, String id){
        DecodedJWT jwt = JWT.decode(token);

        String type = jwt.getClaim("type").toString();

        if(type.equals("user") && !id.equals(jwt.getClaim("id").toString())){
            throw new IllegalStateException("Invalid token");
        }
    }
}
