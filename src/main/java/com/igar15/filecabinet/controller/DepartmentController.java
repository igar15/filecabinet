package com.igar15.filecabinet.controller;

import com.igar15.filecabinet.entity.Department;
import com.igar15.filecabinet.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/list")
    public String showAll(Model model, @SortDefault("name") Pageable pageable) {
        model.addAttribute("departments", departmentService.findAll(pageable));
        return "/departments/department-list";
    }

    @GetMapping("/showAddForm")
    @Secured({"ROLE_ADMIN", "ROLE_OTD_WORKER"})
    public String showAddForm(Model model) {
        model.addAttribute("department", new Department());
        return "/departments/department-form";
    }

    @PostMapping("/save")
    @Secured({"ROLE_ADMIN", "ROLE_OTD_WORKER"})
    public String save(@Validated Department department, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "/departments/department-form";
        }
        if (department.isNew()) {
            departmentService.create(department);
        } else {
            departmentService.update(department);
        }
        model.addAttribute("department", department);
        return "/departments/department-info";
    }

    @GetMapping("/showFormForUpdate/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_OTD_WORKER"})
    public String showFormForUpdate(@PathVariable("id") int id, Model model) {
        model.addAttribute("department", departmentService.findById(id));
        return "/departments/department-form";
    }

    @GetMapping("/showDepartmentInfo/{id}")
    public String showDepartmentInfo(@PathVariable int id, Model model) {
        model.addAttribute("department", departmentService.findById(id));
        return "/departments/department-info";
    }

    @GetMapping("/delete/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_OTD_WORKER"})
    public String delete(@PathVariable("id") int id) {
        departmentService.deleteById(id);
        return "redirect:/departments/list";
    }
}
