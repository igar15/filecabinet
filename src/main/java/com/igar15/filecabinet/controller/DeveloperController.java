package com.igar15.filecabinet.controller;

import com.igar15.filecabinet.entity.Developer;
import com.igar15.filecabinet.service.DeveloperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/developers")
public class DeveloperController {

    @Autowired
    private DeveloperService developerService;

    @GetMapping("/list")
    public String showAll(Model model) {
        model.addAttribute("developers", developerService.findAll());
        return "/developers/list-developers";
    }

    @GetMapping("/showAddForm")
    public String showAddForm(Model model) {
        model.addAttribute("developer", new Developer());
        return "/developers/developer-form";
    }

    @PostMapping("/save")
    public String save(@Valid Developer developer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/developers/developer-form";
        }
        if (developer.getId() == null) {
            developerService.create(developer);
        } else {
            developerService.update(developer);
        }
        return "redirect:/developers/list";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("developerId") int id, Model model) {
        model.addAttribute("developer", developerService.findById(id));
        return "/developers/developer-form";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("developerId") int id) {
        developerService.deleteById(id);
        return "redirect:/developers/list";
    }
}
