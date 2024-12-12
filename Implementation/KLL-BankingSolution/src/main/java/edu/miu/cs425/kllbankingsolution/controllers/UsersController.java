package edu.miu.cs425.kllbankingsolution.controllers;

import edu.miu.cs425.kllbankingsolution.dto.Response;
import edu.miu.cs425.kllbankingsolution.dto.UserPojo;
import edu.miu.cs425.kllbankingsolution.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserService userService;

    @PostMapping("/create-user")
    public Response createUser(@RequestBody UserPojo userPojo) {

        return userService.createUser(userPojo);
    }

    @PutMapping("/update-user")
    public Response updateUser(@RequestBody UserPojo userPojo) {
        return userService.updateUser(userPojo);
    }
}
