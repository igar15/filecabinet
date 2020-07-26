package com.igar15.filecabinet.controller;

import com.igar15.filecabinet.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping("/list")
    public String showAll(Model model) {
        model.addAttribute("companies", companyService.findAll());
        return "/companies/list-companies";
    }

}
