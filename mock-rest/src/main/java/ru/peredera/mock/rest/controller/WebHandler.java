package ru.peredera.mock.rest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/mock/rest")
public class WebHandler {


    @RequestMapping("/manager")
    public String restMocks() {
        return "manager";
    }

}
