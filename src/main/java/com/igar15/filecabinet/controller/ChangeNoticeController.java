package com.igar15.filecabinet.controller;

import com.igar15.filecabinet.dto.ChangeNoticeTo;
import com.igar15.filecabinet.entity.ChangeNotice;
import com.igar15.filecabinet.entity.Developer;
import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.service.ChangeNoticeService;
import com.igar15.filecabinet.service.DeveloperService;
import com.igar15.filecabinet.service.DocumentService;
import com.igar15.filecabinet.util.ControllersHelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/changenotices")
public class ChangeNoticeController {


    @Autowired
    private ChangeNoticeService changeNoticeService;

    @Autowired
    private DeveloperService developerService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    ControllersHelperUtil controllersHelperUtil;

    @GetMapping("/list")
    public String showAll(Model model) {
        model.addAttribute("changeNotices", changeNoticeService.findAll());
        return "/changenotices/list-changenotices";
    }

    @GetMapping("/showAddForm")
    public String showAddForm(Model model) {
        model.addAttribute("changeNoticeTo", new ChangeNoticeTo());
        model.addAttribute("developers", developerService.findAll());
        return "/changenotices/changenoticeTo-form";
    }

    @PostMapping("/showFormForDocsAdd")
    public String showFormForDocsAdd(@Valid ChangeNoticeTo changeNoticeTo, BindingResult bindingResult, Model model) {
        List<FieldError> errorsToKeep = bindingResult.getFieldErrors().stream()
                .filter(fer -> !fer.getField().equals("tempDocumentDecimalNumber") && !fer.getField().equals("tempDocumentChangeNumber") && !fer.getField().equals("documents"))
                .collect(Collectors.toList());
        bindingResult = new BeanPropertyBindingResult(changeNoticeTo, "changeNoticeTo");
        for (FieldError fieldError : errorsToKeep) {
            bindingResult.addError(fieldError);
        }
        if (changeNoticeTo.getDocuments() == null || changeNoticeTo.getDocuments().size() == 0) {
            changeNoticeTo.setDocuments(null);
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("developers", developerService.findAll());
            return "/changenotices/changenoticeTo-form";
        }

        else {
            if (changeNoticeTo.getId() == null) {
                model.addAttribute("changeNoticeTo", changeNoticeTo);
                return "/changenotices/changenoticeTo-docs-add-form";
            }
            else {
                ChangeNotice changeNoticeForSaving = convertFromTo(changeNoticeTo);
                changeNoticeService.update(changeNoticeForSaving);
                return "redirect:/changenotices/showChangeNoticeInfo/" + changeNoticeForSaving.getId();
            }
        }
    }

    @PostMapping("/addDocument")
    public String addDocument(@Valid ChangeNoticeTo changeNoticeTo, BindingResult bindingResult, Model model) {
        List<FieldError> errorsToKeep = bindingResult.getFieldErrors().stream()
                .filter(fer -> !fer.getField().equals("documents"))
                .collect(Collectors.toList());
//        List<ObjectError> globalError = bindingResult.getGlobalErrors();
        bindingResult = new BeanPropertyBindingResult(changeNoticeTo, "changeNoticeTo");
        for (FieldError fieldError : errorsToKeep) {
            bindingResult.addError(fieldError);
        }
//        for (ObjectError objectError: globalError) {
//            bindingResult.addError(objectError);
//        }

        if (bindingResult.hasErrors()) {
            return "/changenotices/changenoticeTo-docs-add-form";
        }
        else {
            if (changeNoticeTo.getDocuments() == null) {
//                changeNoticeTo.setDocuments(new HashSet<>());
                changeNoticeTo.setDocuments(new TreeMap<>());
            }

//            changeNoticeTo.getDocuments().add(changeNoticeTo.getTempDocumentDecimalNumber() + ": ch. " + changeNoticeTo.getTempDocumentChangeNumber());
            changeNoticeTo.setTempDocumentDecimalNumber(null);
            changeNoticeTo.setTempDocumentChangeNumber(null);
            model.addAttribute("changeNoticeTo", changeNoticeTo);
            return "/changenotices/changenoticeTo-docs-add-form";
        }
    }

    @PostMapping("/addDocumentForNotNew")
    public String addDocumentForNotNew(@Valid ChangeNoticeTo changeNoticeTo, BindingResult bindingResult, Model model) {
        List<FieldError> errorsToKeep = bindingResult.getFieldErrors().stream()
                .filter(fer -> !fer.getField().equals("tempDocumentDecimalNumber") && !fer.getField().equals("tempDocumentChangeNumber")
                        && !fer.getField().equals("documents"))
                .collect(Collectors.toList());
        bindingResult = new BeanPropertyBindingResult(changeNoticeTo, "changeNoticeTo");
        for (FieldError fieldError : errorsToKeep) {
            bindingResult.addError(fieldError);
        }

        if (bindingResult.hasErrors()) {
            return "/changenotices/changenoticeToInfo";
        }
        else {
            if (changeNoticeTo.getDocuments().size() == 0) changeNoticeTo.setDocuments(null);
            model.addAttribute("changeNoticeTo", changeNoticeTo);
            return "/changenotices/changenoticeTo-docs-add-form";
        }
    }



    @PostMapping("/save")
    public String save(@Valid ChangeNoticeTo changeNoticeTo, BindingResult bindingResult) {
        Integer id = null;
        List<FieldError> errorsToKeep = bindingResult.getFieldErrors().stream()
                .filter(fer -> !fer.getField().equals("tempDocumentDecimalNumber") && !fer.getField().equals("tempDocumentChangeNumber"))
                .collect(Collectors.toList());
        bindingResult = new BeanPropertyBindingResult(changeNoticeTo, "changeNoticeTo");
        for (FieldError fieldError : errorsToKeep) {
            bindingResult.addError(fieldError);
        }
        if (bindingResult.hasErrors()) {
//            changeNoticeTo.setDocuments(new HashSet<>());
            changeNoticeTo.setDocuments(new TreeMap<>());
            return "/changenotices/changenoticeTo-docs-add-form";
        }
        else {
            ChangeNotice changeNotice = convertFromTo(changeNoticeTo);
            if (changeNotice.getId() == null) {
                 id = changeNoticeService.create(changeNotice).getId();
            }
            else {
                changeNoticeService.update(changeNotice);
                id = changeNotice.getId();
            }
        }
        return "redirect:/changenotices/showChangeNoticeInfo/" + id;
    }

    @GetMapping("/showChangeNoticeInfo/{id}")
    public String showChangeNoticeInfo(@PathVariable("id") int id, Model model) {
        ChangeNoticeTo changeNoticeTo = convertToToById(id);
        model.addAttribute("changeNoticeTo", changeNoticeTo);
        return "/changenotices/changenoticeToInfo";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable("id") int id, Model model) {
        model.addAttribute("changeNoticeTo", convertToToById(id));
        model.addAttribute("developers", developerService.findAll());
        return "/changenotices/changenoticeTo-form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        changeNoticeService.deleteById(id);
        return "redirect:/changenotices/list";
    }

    @GetMapping("/showDocuments/{id}")
    public String showDocuments(@PathVariable("id") int changeNoticeId, Model model) {
        model.addAttribute("changeNoticeTo", convertToToById(changeNoticeId));
        return "/changenotices/changeNoticeTo-docs-list";
    }

    @PostMapping("/addDoc")
    public String addDoc(@Valid ChangeNoticeTo changeNoticeTo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/changenotices/changeNoticeTo-docs-list";
        }
        Document newDocument = documentService.findByDecimalNumber(changeNoticeTo.getTempDocumentDecimalNumber());
        changeNoticeTo.getDocuments().put(newDocument, Integer.parseInt(changeNoticeTo.getTempDocumentChangeNumber()));
        changeNoticeService.update(convertFromTo(changeNoticeTo));

        return "redirect:/changenotices//showDocuments/" + changeNoticeTo.getId();
    }

    @GetMapping("/removeDoc/{id}/{decimalNumber}")
    public String removeDoc(@PathVariable("id") int changeNoticeId, @PathVariable("decimalNumber") String decimalNumber) {
        ChangeNotice updated = changeNoticeService.findById(changeNoticeId);
        Document removed = updated.getDocuments().keySet().stream()
                .filter(doc -> doc.getDecimalNumber().equals(decimalNumber))
                .findFirst().orElse(null);
        updated.getDocuments().remove(removed);
        changeNoticeService.update(updated);
        return "redirect:/changenotices/showDocuments/" + changeNoticeId;
    }















    private ChangeNotice convertFromTo(ChangeNoticeTo changeNoticeTo) {
//        Map<Document, Integer> documents = new HashMap<>();
//        if (changeNoticeTo.getDocuments() != null){
//            changeNoticeTo.getDocuments()
//                    .forEach(string -> {
//                        String[] split = string.split(": ch\\. ");
//                        documents.put(documentService.findByDecimalNumber(split[0]), Integer.parseInt(split[1]));
//                    });
//        }
        return new ChangeNotice(changeNoticeTo.getId(), changeNoticeTo.getName(), changeNoticeTo.getChangeCode(),
                changeNoticeTo.getIssueDate(), changeNoticeTo.getDeveloper(), changeNoticeTo.getDocuments());
    }

    private ChangeNoticeTo convertToToById(int id) {
        ChangeNotice found = changeNoticeService.findById(id);
//        Set<String> documentsInString = found.getDocuments().entrySet()
//                .stream()
//                .map(entry -> entry.getKey().getDecimalNumber() + ": ch. " + entry.getValue())
//                .collect(Collectors.toSet());
//        Set<String> sortedDocuments = new TreeSet<>((s1, s2) -> {
//            String first = s1.split("ch\\. ")[0];
//            String second = s2.split("ch\\. ")[0];
//            return first.compareTo(second);
//        });
//        sortedDocuments.addAll(documentsInString);
        TreeMap<Document, Integer> treeMap = new TreeMap<>((s1, s2) -> {
            return s1.getDecimalNumber().compareTo(s2.getDecimalNumber());
        });
        treeMap.putAll(found.getDocuments());
        return new ChangeNoticeTo(found.getId(), found.getName(), found.getChangeCode(), found.getIssueDate(),
                found.getDeveloper(), treeMap);
    }


}
