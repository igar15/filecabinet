package com.igar15.filecabinet.controller;

import com.igar15.filecabinet.service.InternalDispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/internaldispatches")
public class InternalDispatchController {

    @Autowired
    private InternalDispatchService internalDispatchService;

    @GetMapping("/list")
    public String showAll(Model model) {
        model.addAttribute("internalDispatches", internalDispatchService.findAll());
        return "/internaldispathes/list-internaldispatches";
    }
}
