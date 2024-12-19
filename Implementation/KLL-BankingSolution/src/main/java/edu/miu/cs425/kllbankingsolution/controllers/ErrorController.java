//package edu.miu.cs425.kllbankingsolution.controllers;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@Controller
//public class ErrorController {
//
//    @RequestMapping("/error")
//    public String handleError(@RequestParam(required = false) String message, Model model) {
//        model.addAttribute("errorMessage", message != null ? message : "An unknown error occurred.");
//        return "error"; // Return an error view (e.g., error.html)
//    }
//}
