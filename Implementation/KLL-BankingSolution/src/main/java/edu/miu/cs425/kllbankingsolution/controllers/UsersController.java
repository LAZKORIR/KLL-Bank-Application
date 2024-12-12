package edu.miu.cs425.kllbankingsolution.controllers;

import edu.miu.cs425.kllbankingsolution.dto.Response;
import edu.miu.cs425.kllbankingsolution.dto.UserPojo;
import edu.miu.cs425.kllbankingsolution.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserService userService;

    @PostMapping("/create-user")
    public ResponseEntity<Response> createUser(@RequestBody UserPojo userPojo) {

        return new ResponseEntity<>(userService.createUser(userPojo), HttpStatus.OK);
    }

    @PostMapping("/update-user")
    public ResponseEntity<Response> updateUser(@RequestBody UserPojo userPojo) {
        return new ResponseEntity<>(userService.updateUser(userPojo), HttpStatus.OK);
    }

    @PostMapping("/delete-user")
    public ResponseEntity<Response> deleteUser(@RequestBody UserPojo userPojo) {
        return new ResponseEntity<>(userService.deleteUser(userPojo), HttpStatus.OK);
    }

    @GetMapping("/get-users")
    public ResponseEntity<Response> getUsers() {

        return new ResponseEntity<>(userService.getAllUsers(),HttpStatus.OK);
    }

}
