package com.igar15.filecabinet.controller;

import com.igar15.filecabinet.dto.DocumentTo;
import com.igar15.filecabinet.entity.AbstractNamedEntity;
import com.igar15.filecabinet.entity.ChangeNotice;
import com.igar15.filecabinet.entity.Developer;
import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.service.ChangeNoticeService;
import com.igar15.filecabinet.service.CompanyService;
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
import java.util.*;
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

    @Autowired
    private CompanyService companyService;

    @GetMapping("/list")
    public String showAll(Model model) {
        model.addAttribute("documents", documentService.findAll());
        return "/documents/list-documents";
    }

    @GetMapping("/showAddForm")
    public String showAddForm(Model model) {
        model.addAttribute("documentTo", new DocumentTo());
        model.addAttribute("developers", developerService.findAll());
        model.addAttribute("companies", companyService.findAll());
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
            model.addAttribute("companies", companyService.findAll());
            return "/documents/documentTo-form";
        }

        Document documentForSaving = convertFromTo(documentTo);

        if (documentForSaving.getId() == null) {
            documentService.create(documentForSaving);
        } else {
            documentService.update(documentForSaving);
        }
        return "redirect:/documents/showDocumentInfo/" + documentForSaving.getId();
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("documentId") int id, Model model) {
        model.addAttribute("documentTo", convertToToById(id));
        model.addAttribute("developers", developerService.findAll());
        model.addAttribute("companies", companyService.findAll());
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
        documentTo.getChangeNotices().add(documentTo.getTempChangeNoticeName() + " : ch. " + documentTo.getTempChangeNoticeNumber());
        documentTo.setTempChangeNoticeName(null);
        documentTo.setTempChangeNoticeNumber(null);
        return "/documents/documentTo-changes-add-form";
    }

    @PostMapping("/showFormForAddSubscribers")
    public String showFormForAddSubscribers(DocumentTo documentTo, Model model) {
        model.addAttribute("documentTo", documentTo);
        model.addAttribute("companies", companyService.findAll());
        return "/documents/documentTo-subscribers-add-form";
    }


    private Document convertFromTo(DocumentTo documentTo) {
        Map<Integer, ChangeNotice> changeNotices = new HashMap<>();
        documentTo.getChangeNotices()
                .forEach(string -> {
                    String[] split = string.split(" : ch\\. ");
                    changeNotices.put(Integer.parseInt(split[1]), changeNoticeService.findByName(split[0]));
                });
        return new Document(documentTo.getId(), documentTo.getName(), documentTo.getDecimalNumber(), documentTo.getInventoryNumber(),
                documentTo.getReceiptDate(), documentTo.getStatus(), documentTo.getApplicability(), documentTo.getForm(),
                documentTo.getChangeNumber(), documentTo.getStage(), documentTo.getSheetsAmount(), documentTo.getFormat(),
                documentTo.getA4Amount(), documentTo.getDeveloper(), documentTo.getOriginalHolder(), changeNotices);
    }

    private DocumentTo convertToToById(int id) {
        Document found = documentService.findByIdWithChangeNotices(id);
        Set<String> changeNoticesInString = found.getChangeNotices().entrySet()
                .stream()
                .map(entry -> entry.getValue().getName() + " : ch. " + entry.getKey())
                .collect(Collectors.toSet());
        Set<String> sortedChangeNotices = new TreeSet<>((s1, s2) -> {
            String first = s1.split("ch\\. ")[1];
            String second = s2.split("ch\\. ")[1];
            return first.compareTo(second);
        });
        Integer changeNumber = found.getChangeNotices().keySet().stream()
                .max(Comparator.comparingInt(i -> i)).orElse(null);
        sortedChangeNotices.addAll(changeNoticesInString);
        return new DocumentTo(found.getId(), found.getName(), found.getDecimalNumber(), found.getInventoryNumber(),
                found.getReceiptDate(), found.getStatus(), found.getApplicability(), found.getForm(), changeNumber,
                found.getStage(), found.getSheetsAmount(), found.getFormat(), found.getA4Amount(), found.getDeveloper(),
                found.getOriginalHolder(), sortedChangeNotices);
    }

}
