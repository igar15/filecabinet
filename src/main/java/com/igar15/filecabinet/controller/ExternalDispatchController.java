package com.igar15.filecabinet.controller;

import com.igar15.filecabinet.dto.ExternalDispatchTo;
import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.entity.ExternalDispatch;
import com.igar15.filecabinet.service.CompanyService;
import com.igar15.filecabinet.service.DocumentService;
import com.igar15.filecabinet.service.ExternalDispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/externaldispatches")
public class ExternalDispatchController {

    @Autowired
    private ExternalDispatchService externalDispatchService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private DocumentService documentService;

    @GetMapping("/list")
    public String showAll(Model model) {
        model.addAttribute("externalDispatches", externalDispatchService.findAll());
        return "/externaldispatches/list-externaldispatches";
    }

    @GetMapping("/showAddForm")
    public String showAddForm(Model model) {
        model.addAttribute("externalDispatchTo", new ExternalDispatchTo());
        model.addAttribute("companies", companyService.findAll());
        return "/externaldispatches/externalDispatchTo-form";
    }

    @PostMapping("/save")
    public String save(@Valid ExternalDispatchTo externalDispatchTo, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("companies", companyService.findAll());
            return "/externaldispatches/externalDispatchTo-form";
        }
        else {
            if (externalDispatchTo.getId() == null) {
                model.addAttribute("externalDispatchTo", externalDispatchTo);
                return "/externaldispatches/externalDispatchToInfo";
            }
            else {
                externalDispatchService.update(convertFromTo(externalDispatchTo));
                return "redirect:/externaldispatches/showExternalDispatchInfo/" + externalDispatchTo.getId();
            }
        }
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable("id") int id, Model model) {
        model.addAttribute("externalDispatchTo", convertToToById(id));
        model.addAttribute("companies", companyService.findAll());
        return "/externaldispatches/externalDispatchTo-form";
    }

    @GetMapping("/showExternalDispatchInfo/{id}")
    public String showExternalDispatchInfo(@PathVariable("id") int id, Model model) {
        model.addAttribute("externalDispatchTo", convertToToById(id));
        return "/externaldispatches/externalDispatchToInfo";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        externalDispatchService.deleteById(id);
        return "redirect:/externaldispatches/list";
    }

    @PostMapping("/addDocument")
    public String addDocument(@Valid ExternalDispatchTo externalDispatchTo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/externaldispatches/externalDispatchToInfo";
        }
        Document added = documentService.findByDecimalNumber(externalDispatchTo.getTempDocumentDecimalNumber());
        externalDispatchTo.getDocuments().add(added);
        if (externalDispatchTo.getId() == null) {
            ExternalDispatch newExternalDispatch = externalDispatchService.create(convertFromTo(externalDispatchTo));
            externalDispatchTo.setId(newExternalDispatch.getId());
        }
        else {
            externalDispatchService.update(convertFromTo(externalDispatchTo));
        }
        return "redirect:/externaldispatches/showExternalDispatchInfo/" + externalDispatchTo.getId();
    }

    @GetMapping("/removeDoc/{id}/{decimalNumber}")
    public String removeDoc(@PathVariable("id") int id, @PathVariable("decimalNumber") String decimalNumber, Model model) {
        Document removed = documentService.findByDecimalNumber(decimalNumber);
        ExternalDispatch found = externalDispatchService.findById(id);
        if (found.getDocuments().size() == 1) {
            model.addAttribute("externalDispatchTo", convertToToById(id));
            String errorMessage = "External dispatch " + found.getWaybill() + " can not exist without any documents!";
            model.addAttribute("errorMessage", errorMessage);
            return "/externaldispatches/externalDispatchToInfo";
        }
        found.getDocuments().remove(removed);
        externalDispatchService.update(found);
        return "redirect:/externaldispatches/showExternalDispatchInfo/" + id;
    }





    private ExternalDispatchTo convertToToById(int id) {
        ExternalDispatch found = externalDispatchService.findById(id);
        return new ExternalDispatchTo(found.getId(), found.getWaybill(), found.getDispatchDate(), found.getStatus(), found.getRemark(),
                found.getLetterOutgoingNumber(), found.getCompany(), found.getDocuments());
    }

    private ExternalDispatch convertFromTo(ExternalDispatchTo externalDispatchTo) {
        return new ExternalDispatch(externalDispatchTo.getId(), externalDispatchTo.getWaybill(), externalDispatchTo.getDispatchDate(),
                externalDispatchTo.getStatus(), externalDispatchTo.getRemark(), externalDispatchTo.getLetterOutgoingNumber(),
                externalDispatchTo.getCompany(), externalDispatchTo.getDocuments());
    }


}
