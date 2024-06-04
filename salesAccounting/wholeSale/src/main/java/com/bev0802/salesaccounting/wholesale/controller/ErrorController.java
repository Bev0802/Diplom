package com.bev0802.salesaccounting.wholesale.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
@Controller
public class ErrorController {

    @GetMapping("/error")
    public String handleError(@ModelAttribute("errorMessage") String errorMessage, Model model) {
        model.addAttribute("errorMessage", errorMessage);
        return "error";
    }
}