package com.igar15.filecabinet.controller;

import com.igar15.filecabinet.dto.DocumentTo;
import com.igar15.filecabinet.entity.ChangeNotice;
import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.entity.ExternalDispatch;
import com.igar15.filecabinet.entity.InternalDispatch;
import com.igar15.filecabinet.service.*;
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

    @Autowired
    private ExternalDispatchService externalDispatchService;

    @Autowired
    private InternalDispatchService internalDispatchService;

    @GetMapping("/list")
    public String showAll(@RequestParam(name = "decimalNum", required = false) String decimalNum, Model model) {
        List<Document> documents = null;
        if (decimalNum == null || decimalNum.isEmpty()) {
            documents = documentService.findAll();
        }
        else {
            documents = List.of(documentService.findByDecimalNumber(decimalNum));
        }
        model.addAttribute("documents", documents);
        return "/documents/list-documents";
    }

//    @GetMapping("/filter")
//    public String filter(@RequestParam("decimalNum") String decimalNumber, Model model) {
//        model.addAttribute("documents", List.of(documentService.findByDecimalNumber(decimalNumber)));
//        return "/documents/list-documents";
//    }

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
//        documentTo.getChangeNotices().add(documentTo.getTempChangeNoticeName() + " : ch. " + documentTo.getTempChangeNoticeNumber());
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

    @GetMapping("/showChanges/{id}")
    public String showChanges(@PathVariable("id") int documentId, Model model) {
        model.addAttribute("documentTo", convertToToById(documentId));
        return "/documents/documentTo-changes-list";
    }

    @PostMapping("/addChange")
    public String addChange(@Valid DocumentTo documentTo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/documents/documentTo-changes-list";
        }
        ChangeNotice newChange = changeNoticeService.findByName(documentTo.getTempChangeNoticeName());
        documentTo.getChangeNotices().put(Integer.parseInt(documentTo.getTempChangeNoticeNumber()), newChange);
        documentService.update(convertFromTo(documentTo));
//        documentTo.setTempChangeNoticeName(null);
//        documentTo.setTempChangeNoticeNumber(null);
        return "redirect:/documents/showChanges/" + documentTo.getId();
    }

    @GetMapping("/removeChange/{id}/{changeId}")
    public String removeChange(@PathVariable("id") int documentId, @PathVariable("changeId") int changeId, Model model) {
        ChangeNotice changeNotice = changeNoticeService.findById(changeId);
        if (changeNotice.getDocuments().size() == 1) {
            model.addAttribute("documentTo", convertToToById(documentId));
            String errorMessage = "Change notice " + changeNotice.getName() + " can not exist without any documents!";
            model.addAttribute("errorMessage", errorMessage);
            return "/documents/documentTo-changes-list";
        }
        else {
            Document updated = documentService.findById(documentId);
            Optional<Map.Entry<Integer, ChangeNotice>> found = updated.getChangeNotices().entrySet().stream()
                    .filter(entry -> entry.getValue().equals(changeNotice))
                    .findFirst();
            if (found.isPresent()) {
                updated.getChangeNotices().remove(found.get().getKey());
                documentService.update(updated);
            }
            return "redirect:/documents/showChanges/" + documentId;
        }
    }

    @GetMapping("/showExternalDispatches/{id}")
    public String showExternalDispatches(@PathVariable("id") int id, Model model) {
        List<ExternalDispatch> externalDispatches = externalDispatchService.findAllByDocumentId(id);
        model.addAttribute("externalDispatches", externalDispatches);
        model.addAttribute("documentId", id);
        return "/documents/document-externals-list";
    }

    @GetMapping("/removeExternal/{id}/{externalId}")
    public String removeExternal(@PathVariable("id") int id, @PathVariable("externalId") int externalId, Model model) {
        ExternalDispatch found = externalDispatchService.findById(externalId);
        if (found.getDocuments().size() == 1) {
            List<ExternalDispatch> externalDispatches = externalDispatchService.findAllByDocumentId(id);
            model.addAttribute("externalDispatches", externalDispatches);
            model.addAttribute("documentId", id);
            String errorMessage = "External dispatch " + found.getWaybill() + " can not exist without any documents!";
            model.addAttribute("errorMessage", errorMessage);
            return "/documents/document-externals-list";
        }
        Document docFound = documentService.findById(id);
        found.getDocuments().remove(docFound);
        externalDispatchService.update(found);
        return "redirect:/documents/showExternalDispatches/" + id;
    }

    @GetMapping("/showInternalDispatches/{id}")
    public String showInternalDispatches(@PathVariable("id") int id, Model model) {
        List<InternalDispatch> internalDispatches = internalDispatchService.findAllByDocumentId(id);
        model.addAttribute("internalDispatches", internalDispatches);
        return "/documents/document-internals-list";
    }

    @GetMapping("/removeInternal/{id}/{internalId}")
    public String removeInternal(@PathVariable("id") int id, @PathVariable("internalId") int internalId, Model model) {
        InternalDispatch found = internalDispatchService.findById(internalId);
        Document docFound = documentService.findById(id);
        found.getDocuments().remove(docFound);
        if (found.getDocuments().isEmpty()) {
            internalDispatchService.deleteById(internalId);
        }
        else {
            internalDispatchService.update(found);
        }
        return "redirect:/documents/showInternalDispatches/" + id;
    }

    @GetMapping("/showApplicabilities/{id}")
    public String showApplicabilities(@PathVariable("id") int id, Model model) {
        model.addAttribute("documentTo", convertToToById(id));
        return ("/documents/document-applicabilities-list");
    }

    @GetMapping("/removeApplicability/{id}/{applicabilityId}")
    public String removeApplicability(@PathVariable("id") int id, @PathVariable("applicabilityId") int applicabilityId) {
        Document found = documentService.findById(id);
        Document removeApplicability = documentService.findById(applicabilityId);
        found.getApplicabilities().remove(removeApplicability);
        documentService.update(found);
        return "redirect:/documents/showApplicabilities/" + id;
    }

    @PostMapping("/addApplicability")
    public String addApplicability(@Valid DocumentTo documentTo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/documents/document-applicabilities-list";
        }
        Document newApplicability = documentService.findByDecimalNumber(documentTo.getTempApplicability());
        documentTo.getApplicabilities().add(newApplicability);
        documentService.update(convertFromTo(documentTo));
        return "redirect:/documents/showApplicabilities/" + documentTo.getId();
    }


















    private Document convertFromTo(DocumentTo documentTo) {
//        Map<Integer, ChangeNotice> changeNotices = new HashMap<>();
//        documentTo.getChangeNotices()
//                .forEach(string -> {
//                    String[] split = string.split(" : ch\\. ");
//                    changeNotices.put(Integer.parseInt(split[1]), changeNoticeService.findByName(split[0]));
//                });
        return new Document(documentTo.getId(), documentTo.getName(), documentTo.getDecimalNumber(), documentTo.getInventoryNumber(),
                documentTo.getReceiptDate(), documentTo.getStatus(), documentTo.getApplicabilities(), documentTo.getForm(),
                documentTo.getChangeNumber(), documentTo.getStage(), documentTo.getSheetsAmount(), documentTo.getFormat(),
                documentTo.getA4Amount(), documentTo.getDeveloper(), documentTo.getOriginalHolder(), documentTo.getChangeNotices());
    }

    private DocumentTo convertToToById(int id) {
        Document found = documentService.findByIdWithChangeNotices(id);
//        Set<String> changeNoticesInString = found.getChangeNotices().entrySet()
//                .stream()
//                .map(entry -> entry.getValue().getName() + " : ch. " + entry.getKey())
//                .collect(Collectors.toSet());
//        Set<String> sortedChangeNotices = new TreeSet<>((s1, s2) -> {
//            String first = s1.split("ch\\. ")[1];
//            String second = s2.split("ch\\. ")[1];
//            return first.compareTo(second);
//        });
        Integer changeNumber = found.getChangeNotices().keySet().stream()
                .max(Comparator.comparingInt(i -> i)).orElse(null);
//        sortedChangeNotices.addAll(changeNoticesInString);
        return new DocumentTo(found.getId(), found.getName(), found.getDecimalNumber(), found.getInventoryNumber(),
                found.getReceiptDate(), found.getStatus(), found.getApplicabilities(), found.getForm(), changeNumber,
                found.getStage(), found.getSheetsAmount(), found.getFormat(), found.getA4Amount(), found.getDeveloper(),
                found.getOriginalHolder(), found.getChangeNotices());
    }

}
