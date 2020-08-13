package com.igar15.filecabinet.controller;

import com.igar15.filecabinet.entity.*;
import com.igar15.filecabinet.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private CompanyService companyService;

    @GetMapping("/list")
    public String showAll(@RequestParam(name = "decimalNumber", required = false) String decimalNumber,
                          @RequestParam(name = "name", required = false) String name,
                          @RequestParam(name = "department", required = false) String department,
                          @RequestParam(name = "originalHolder", required = false) String originalHolder,
                          @RequestParam(name = "inventoryNumber", required = false) String inventoryNumber,
                          @RequestParam(name = "status", required = false) String status,
                          @RequestParam(name = "stage", required = false) String stage,
                          @RequestParam(name = "form", required = false) String form,
                          @RequestParam(name = "after", required = false) String after,
                          @RequestParam(name = "before", required = false) String before,
                          @SortDefault("decimalNumber") Pageable pageable,
                          Model model) {
        model.addAttribute("departments", departmentService.findAllByIsDeveloper(true));
        model.addAttribute("department", department);
        model.addAttribute("originalHolders", companyService.findAll());
        model.addAttribute("originalHolder", originalHolder);
        model.addAttribute("status", status);
        model.addAttribute("stage", stage);
        model.addAttribute("form", form);
        Page<Document> documents = documentService.findAll(decimalNumber, name, department, originalHolder, inventoryNumber,
                status, stage, form, after, before, pageable);
        model.addAttribute("documents", documents);
        return "/documents/document-list";
    }

    @GetMapping("/showAddForm")
    public String showAddForm(Model model) {
        model.addAttribute("document", new Document());
        model.addAttribute("departments", departmentService.findAllByIsDeveloper(true));
        model.addAttribute("companies", companyService.findAll());
        return "/documents/document-form";
    }

    @PostMapping("/save")
    public String save(@Validated Document document, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentService.findAllByIsDeveloper(true));
            model.addAttribute("companies", companyService.findAll());
            return "/documents/document-form";
        }
        if (document.isNew()) {
            documentService.create(document);
        } else {
            documentService.updateWithoutChildren(document);
        }
        model.addAttribute("document", document);
        return "/documents/document-info";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable("id") int id, Model model) {
        model.addAttribute("document", documentService.findById(id));
        model.addAttribute("departments", departmentService.findAllByIsDeveloper(true));
        model.addAttribute("companies", companyService.findAll());
        return "/documents/document-form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        documentService.deleteById(id);
        return "redirect:/documents/list";
    }

    @GetMapping("/showDocumentInfo/{id}")
    public String showDocumentInfo(@PathVariable("id") int id, Model model) {
        model.addAttribute("document", documentService.findById(id));
        return "/documents/document-info";
    }

    @GetMapping("/showChanges/{id}")
    public String showChanges(@PathVariable("id") int id, Model model) {
        Document document = documentService.findByIdWithChangeNotices(id);
        model.addAttribute("document", document);
        return "documents/document-changenotices";
    }

    @GetMapping("/removeChange/{id}/{changeId}")
    public String removeChange(@PathVariable("id") int id, @PathVariable("changeId") int changeId, Model model) {
        Document document = documentService.findByIdWithChangeNotices(id);
        String errorMessage = documentService.removeChange(document, changeId);
        model.addAttribute("document", document);
        model.addAttribute("errorMessage", errorMessage);
        return "documents/document-changenotices";
    }

    @GetMapping("/showExternalDispatches/{id}")
    public String showExternalDispatches(@PathVariable("id") int id, Model model) {
        Document document = documentService.findByIdWithExternalDispatches(id);
        model.addAttribute("document", document);
        return "/documents/document-externaldispatches";
    }

    @GetMapping("/deregisterExternal/{id}/{externalId}")
    public String deregisterExternal(@PathVariable("id") int id, @PathVariable("externalId") int externalId, Model model) {
        Document document = documentService.deregisterExternal(id, externalId);
        model.addAttribute("document", document);
        return "/documents/document-externaldispatches";
    }

    @GetMapping("/showInternalDispatches/{id}")
    public String showInternalDispatches(@PathVariable("id") int id, Model model) {
        Document document = documentService.findByIdWithInternalDispatches(id);
        model.addAttribute("document", document);
        return "/documents/document-internaldispatches";
    }

    @GetMapping("/deregisterInternal/{id}/{internalId}")
    public String deregisterInternal(@PathVariable("id") int id, @PathVariable("internalId") int internalId, Model model) {
        Document document = documentService.deregisterInternal(id, internalId);
        model.addAttribute("document", document);
        return "/documents/document-internaldispatches";
    }

    @GetMapping("/deregisterAlbum/{id}/{internalId}")
    public String deregisterAlbum(@PathVariable("id") int id,
                                  @PathVariable("internalId") int internalId) {
        documentService.deregisterAlbum(id, internalId);
        return "redirect:/documents/showInternalDispatches/" + id;
    }

    @GetMapping("/showApplicabilities/{id}")
    public String showApplicabilities(@PathVariable("id") int id, Model model) {
        model.addAttribute("document", documentService.findByIdWithApplicabilities(id));
        return ("/documents/document-applicabilities");
    }

    @GetMapping("/removeApplicability/{id}/{applicabilityId}")
    public String removeApplicability(@PathVariable("id") int id, @PathVariable("applicabilityId") int applicabilityId, Model model) {
        Document document = documentService.removeApplicability(id, applicabilityId);
        model.addAttribute("document", document);
        return "/documents/document-applicabilities";
    }

    @PostMapping("/addApplicability/{id}")
    public String addApplicability(@PathVariable("id") int id,
                                   @RequestParam(name = "newApplicability") String newApplicability,
                                   Model model) {
        Document document = documentService.findByIdWithApplicabilities(id);
        String errorMessage = documentService.addApplicability(document, newApplicability);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("document", document);
        return "/documents/document-applicabilities";
    }

    @GetMapping("/deregisterExternalWithIncomings/{id}/{externalId}")
    public String deregisterExternalWithIncomings(@PathVariable("id") int id,
                                                  @PathVariable("externalId") int externalId,
                                                  Model model) {
        Document document = documentService.deregisterExternalWithIncomings(id, externalId);
        model.addAttribute("document", document);
        return "/documents/document-externaldispatches";
    }

}
