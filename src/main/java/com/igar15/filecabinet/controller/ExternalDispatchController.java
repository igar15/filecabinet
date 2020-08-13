package com.igar15.filecabinet.controller;

import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.entity.ExternalDispatch;
import com.igar15.filecabinet.service.CompanyService;
import com.igar15.filecabinet.service.DocumentService;
import com.igar15.filecabinet.service.ExternalDispatchService;
import com.igar15.filecabinet.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


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
    public String showAll(@SortDefault("dispatchDate") Pageable pageable, Model model) {
        model.addAttribute("externalDispatches", externalDispatchService.findAll(pageable));
        return "/externaldispatches/externaldispatch-list";
    }

    @GetMapping("/showAddForm")
    public String showAddForm(Model model) {
        model.addAttribute("externalDispatch", new ExternalDispatch());
        model.addAttribute("companies", companyService.findAll());
        return "/externaldispatches/externaldispatch-form";
    }

    @PostMapping("/save")
    public String save(@Validated ExternalDispatch externalDispatch, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("companies", companyService.findAll());
            return "/externaldispatches/externaldispatch-form";
        }
        else {
            if (externalDispatch.isNew()) {
                externalDispatchService.create(externalDispatch);
            }
            else {
                externalDispatchService.updateWithoutChildren(externalDispatch);
            }
            return "redirect:/externaldispatches/showExternalDispatchInfo/" + externalDispatch.getId();
        }
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable("id") int id, Model model) {
        model.addAttribute("externalDispatch", externalDispatchService.findById(id));
        model.addAttribute("companies", companyService.findAll());
        return "/externaldispatches/externaldispatch-form";
    }

    @GetMapping("/showExternalDispatchInfo/{id}")
    public String showExternalDispatchInfo(@PathVariable("id") int id, Model model) {
        model.addAttribute("externalDispatch", externalDispatchService.findByIdWithDocuments(id));
        return "/externaldispatches/externaldispatch-info";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        externalDispatchService.deleteById(id);
        return "redirect:/externaldispatches/list";
    }

    @PostMapping("/addDocument/{id}")
    public String addDocument(@PathVariable("id") int id,
                              @RequestParam("newDocument") String newDocument,
                              Model model) {

        String errorMessage = null;
        ExternalDispatch externalDispatch = externalDispatchService.findByIdWithDocuments(id);
        if (newDocument == null) {
            errorMessage = "Decimal number must not be empty";
        }
        else {
            try {
                Document document = documentService.findByDecimalNumber(newDocument);
                if (externalDispatch.getDocuments().containsKey(document)) {
                    errorMessage = "Document already added";
                }
                else {
                    externalDispatch.getDocuments().put(document, true);
                    externalDispatchService.update(externalDispatch);
                }

            } catch (NotFoundException e) {
                errorMessage = "Document not found";
            }
        }
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("externalDispatch", externalDispatch);
        return "/externaldispatches/externaldispatch-info";
    }

    @GetMapping("/removeDoc/{id}/{documentId}")
    public String removeDoc(@PathVariable("id") int id,
                            @PathVariable("documentId") int documentId,
                            Model model) {
        String errorDeleteMessage = null;
        ExternalDispatch externalDispatch = externalDispatchService.findByIdWithDocuments(id);

        if (externalDispatch.getDocuments().size() == 1) {
            errorDeleteMessage = "External dispatch " + externalDispatch.getWaybill() + " can not exist without any documents!";
        }
        else {
            Document found = externalDispatch.getDocuments().keySet().stream()
                    .filter(document -> document.getId().equals(documentId))
                    .findFirst().orElse(null);
            externalDispatch.getDocuments().remove(found);
            externalDispatchService.update(externalDispatch);
        }
        model.addAttribute("errorDeleteMessage", errorDeleteMessage);
        model.addAttribute("externalDispatch", externalDispatch);
        return "/externaldispatches/externaldispatch-info";
    }

}
