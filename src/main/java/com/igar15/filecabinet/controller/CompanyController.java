package com.igar15.filecabinet.controller;

import com.igar15.filecabinet.entity.Company;
import com.igar15.filecabinet.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @GetMapping("/showAddForm")
    public String showAddForm(Model model) {
        model.addAttribute("company", new Company());
        return "/companies/company-form";
    }

    @PostMapping("/save")
    public String save(@Valid Company company, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "/companies/company-form";
        }
        if (company.isNew()) {
            companyService.create(company);
        }
        else {
            companyService.update(company);
        }
        return "redirect:/companies/showCompanyInfo/" + company.getId();
    }

    @GetMapping("/showCompanyInfo/{id}")
    public String showCompanyInfo(@PathVariable("id") int id, Model model) {
        model.addAttribute("company", companyService.findById(id));
        return "/companies/companyInfo";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("companyId") int id, Model model) {
        model.addAttribute("company", companyService.findById(id));
        return "/companies/company-form";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("companyId") int id) {
        companyService.deleteById(id);
        return "redirect:/companies/list";
    }

}
