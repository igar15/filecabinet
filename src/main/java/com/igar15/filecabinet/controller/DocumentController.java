package com.igar15.filecabinet.controller;

import com.igar15.filecabinet.dto.DocumentTo;
import com.igar15.filecabinet.entity.AbstractNamedEntity;
import com.igar15.filecabinet.entity.Developer;
import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.service.DeveloperService;
import com.igar15.filecabinet.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private DeveloperService developerService;

    @GetMapping("/list")
    public String showAll(Model model) {
        model.addAttribute("documents", documentService.findAll());
        return "/documents/list-documents";
    }

    @GetMapping("/showAddForm")
    public String showAddForm(Model model) {
        model.addAttribute("documentTo", new DocumentTo(getDeveloperNames()));
        return "/documents/documentTo-form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("documentTo") DocumentTo documentTo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            documentTo.setDeveloperNames(getDeveloperNames());
            return "/documents/documentTo-form";
        }
        Document savedDocument = convertFromTo(documentTo);
        if (savedDocument.getId() == null) {
            documentService.create(savedDocument);
        } else {
            documentService.update(savedDocument);
        }
        return "redirect:/documents/list";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("documentId") int id, Model model) {
        DocumentTo documentTo = convertToToById(id);
        model.addAttribute("documentTo", documentTo);
        return "/documents/documentTo-form";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("documentId") int id) {
        documentService.deleteById(id);
        return "redirect:/documents/list";
    }

    @GetMapping("/filter")
    public String filter(@RequestParam("decimalNum") String decimalNumber, Model model) {
        model.addAttribute("documents", documentService.findByDecimalNumber(decimalNumber));
        return "/documents/list-documents";
    }

    @GetMapping("/showDocumentInfo")
    public String showDocumentInfo(@RequestParam("documentId") int id, Model model) {
        model.addAttribute("documentTo", convertToToById(id));
        return "/documents/documentToInfo";
    }

    private Document convertFromTo(DocumentTo documentTo) {
        Developer documentDeveloper = (documentTo.getDeveloperName() == null) ? null : developerService.findByName(documentTo.getDeveloperName());
        return new Document(documentTo.getId(), documentTo.getName(), documentTo.getDecimalNumber(),
                documentTo.getInventoryNumber(), documentTo.getStage(), documentDeveloper);
    }

    private DocumentTo convertToToById(int id) {
        Document found = documentService.findById(id);
        Developer developer = found.getDeveloper();
        String developerName = (developer == null) ? null : developer.getName();
        return new DocumentTo(found.getId(), found.getName(), found.getDecimalNumber(),
                found.getInventoryNumber(), found.getStage(), developerName, getDeveloperNames());
    }

    private String[] getDeveloperNames() {
        return developerService.findAll().stream()
                .map(AbstractNamedEntity::getName).toArray(String[]::new);
    }

}
