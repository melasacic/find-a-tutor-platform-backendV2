package com.findTutor.findTutor.controller.users;

import com.findTutor.findTutor.controller.users.model.LoginUserModel;
import com.findTutor.findTutor.controller.users.model.UserCreateRequest;
import com.findTutor.findTutor.controller.users.model.UserCreateUpdate;
import com.findTutor.findTutor.controller.users.model.UserResponse;
import com.findTutor.findTutor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponse> getUsers() {
        return userService.getUsers();
    }

    // we take RequestBody and then we map it into an tutor
    @PostMapping
    public void registerNewUser(@RequestBody UserCreateRequest userCreateRequest) {
        userService.addNewUser(userCreateRequest);
    }

    @DeleteMapping(path = "{userId}")
    public ResponseEntity delete(
            @PathVariable("userId") Long id,
            @RequestHeader("Authorization") String token) {
        try {
            userService.validateToken(token, id.toString());
            userService.deleteUser(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @PatchMapping("/{userId}")
    public ResponseEntity updateUser(
            @PathVariable("userId") Long id,
            @RequestBody UserCreateUpdate userCreateUpdate,
            @RequestHeader("Authorization") String token) {
        try {
            userService.validateToken(token, id.toString());
            userService.updateUser(id, userCreateUpdate);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/token")
    public ResponseEntity<String> getUserToken(@RequestBody LoginUserModel loginUserModel) {
        try {
            return ResponseEntity.ok(userService.getUserToken(loginUserModel));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}