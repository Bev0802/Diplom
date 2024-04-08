package com.bev0802.salesaccounting.wholesale.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping("/login")
    public String index() {
        return "index.html";
    }
}
