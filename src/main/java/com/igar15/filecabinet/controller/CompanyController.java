package com.igar15.filecabinet.controller;

import com.igar15.filecabinet.entity.Company;
import com.igar15.filecabinet.entity.InternalDispatch;
import com.igar15.filecabinet.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
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
    public String showAll(@SortDefault("name") Pageable pageable,
                          @RequestParam(value = "companyName", required = false) String companyName,
                          Model model) {
        if(companyName != null) {
            companyName = companyName.trim();
        }
        companyName = "".equals(companyName) ? null : companyName;
        Page<Company> companies = null;
        if (companyName != null) {
            companies = companyService.findByNameContainsIgnoreCase(companyName, pageable);
        }
        else {
            companies = companyService.findAll(pageable);
        }
        model.addAttribute("companies", companies);
        return "/companies/company-list";
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
        model.addAttribute("company", company);
        return "/companies/company-info";
    }

    @GetMapping("/showCompanyInfo/{id}")
    public String showCompanyInfo(@PathVariable("id") int id, Model model) {
        model.addAttribute("company", companyService.findById(id));
        return "/companies/company-info";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable("id") int id, Model model) {
        model.addAttribute("company", companyService.findById(id));
        return "/companies/company-form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        companyService.deleteById(id);
        return "redirect:/companies/list";
    }

}
