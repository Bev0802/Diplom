package com.bev0802.salesaccounting.wholesale.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/login")
    public String login(@RequestParam String inn, @RequestParam String password, HttpServletRequest request, HttpServletResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("inn", inn);
        map.add("password", password);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, headers);

        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {};

        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange("http://productDB/api/organizations/authenticate", HttpMethod.POST, requestEntity, responseType);

        if (responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null) {
            Long organizationId = Long.valueOf(responseEntity.getBody().get("organizationId").toString());
            request.getSession().setAttribute("organizationId", organizationId);

            return "redirect:/organization/" + organizationId + "/employee/employeeLoginForm";
        } else {
            request.setAttribute("loginError", "ИНН или пароль введены неверно.");
            return "index";
        }
    }


}
