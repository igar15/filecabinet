package com.igar15.filecabinet.controller;

import com.igar15.filecabinet.service.ExternalDispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/externaldispatches")
public class ExternalDispatchController {

    @Autowired
    private ExternalDispatchService externalDispatchService;

    @GetMapping("/list")
    public String showAll(Model model) {
        model.addAttribute("externalDispatches", externalDispatchService.findAll());
        return "/externaldispatches/list-externaldispatches";
    }


}
