package edu.miu.cs425.kllbankingsolution.controllers;

import edu.miu.cs425.kllbankingsolution.dto.Response;
import edu.miu.cs425.kllbankingsolution.dto.UserPojo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/home")
    public String home() {

        return "admin-home-page";
    }


}
