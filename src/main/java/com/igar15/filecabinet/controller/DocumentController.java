package com.igar15.filecabinet.controller;

import com.igar15.filecabinet.dto.DocumentTo;
import com.igar15.filecabinet.entity.AbstractNamedEntity;
import com.igar15.filecabinet.entity.ChangeNotice;
import com.igar15.filecabinet.entity.Developer;
import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.service.ChangeNoticeService;
import com.igar15.filecabinet.service.DeveloperService;
import com.igar15.filecabinet.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private DeveloperService developerService;

    @Autowired
    private ChangeNoticeService changeNoticeService;

    @GetMapping("/list")
    public String showAll(Model model) {
        model.addAttribute("documents", documentService.findAll());
        return "/documents/list-documents";
    }

    @GetMapping("/showAddForm")
    public String showAddForm(Model model) {
        model.addAttribute("documentTo", new DocumentTo());
        model.addAttribute("developers", developerService.findAll());
        return "/documents/documentTo-form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("documentTo") DocumentTo documentTo, BindingResult bindingResult, Model model) {
        List<FieldError> errorsToKeep = bindingResult.getFieldErrors().stream()
                .filter(fer -> !fer.getField().equals("tempChangeNoticeName"))
                .collect(Collectors.toList());
        bindingResult = new BeanPropertyBindingResult(documentTo, "documentTo");
        for (FieldError fieldError : errorsToKeep) {
            bindingResult.addError(fieldError);
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("developers", developerService.findAll());
            return "/documents/documentTo-form";
        }
        Document savedDocument = convertFromTo(documentTo);
        if (savedDocument.getId() == null) {
            documentService.create(savedDocument);
        } else {
            documentService.update(savedDocument);
        }
        return "redirect:/documents/showDocumentInfo/" + savedDocument.getId();
//        if (documentTo.getId() == null) {
//            return "redirect:/documents/list";
//        }
//        else {
//            return "redirect:/documents/showDocumentInfo/" + documentTo.getId();
//        }
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("documentId") int id, Model model) {
        model.addAttribute("documentTo", convertToToById(id));
        model.addAttribute("developers", developerService.findAll());
        return "/documents/documentTo-form";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("documentId") int id) {
        documentService.deleteById(id);
        return "redirect:/documents/list";
    }

    @GetMapping("/filter")
    public String filter(@RequestParam("decimalNum") String decimalNumber, Model model) {
        model.addAttribute("documents", List.of(documentService.findByDecimalNumber(decimalNumber)));
        return "/documents/list-documents";
    }

    @GetMapping("/showDocumentInfo/{documentId}")
    public String showDocumentInfo(@PathVariable("documentId") int id, Model model) {
        model.addAttribute("documentTo", convertToToById(id));
        return "/documents/documentToInfo";
    }

    @PostMapping("/showFormForAddChangeNotice")
    public String showFormForAddChangeNotice(DocumentTo documentTo, Model model) {
        model.addAttribute("documentTo", documentTo);
        return "/documents/documentTo-changes-add-form";
    }

    @PostMapping("/addChangeNotice")
    public String addChangeNotice(@Valid DocumentTo documentTo, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "/documents/documentTo-changes-add-form";
        }
        documentTo.getChangeNotices().add(changeNoticeService.findByName(documentTo.getTempChangeNoticeName()));
        return "/documents/documentTo-changes-add-form";
    }


    private Document convertFromTo(DocumentTo documentTo) {
        return new Document(documentTo.getId(), documentTo.getName(), documentTo.getDecimalNumber(),
                documentTo.getInventoryNumber(), documentTo.getStage(), documentTo.getDeveloper(), documentTo.getChangeNotices());
    }

    private DocumentTo convertToToById(int id) {
        Document found = documentService.findByIdWithChangeNotices(id);
        return new DocumentTo(found.getId(), found.getName(), found.getDecimalNumber(),
                found.getInventoryNumber(), found.getStage(), found.getDeveloper(), found.getChangeNotices());
    }

}
