package com.igar15.filecabinet.controller;

import com.igar15.filecabinet.dto.ChangeNoticeTo;
import com.igar15.filecabinet.entity.ChangeNotice;
import com.igar15.filecabinet.entity.Developer;
import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.repository.ChangeNoticeRepository;
import com.igar15.filecabinet.service.ChangeNoticeService;
import com.igar15.filecabinet.service.DeveloperService;
import com.igar15.filecabinet.service.DocumentService;
import com.igar15.filecabinet.util.ControllersHelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
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
    private ChangeNoticeRepository changeNoticeRepository;

    @Autowired
    ControllersHelperUtil controllersHelperUtil;

    @GetMapping("/list")
    public String showAll(@SortDefault(value = "issueDate", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        model.addAttribute("developers", developerService.findAll());
        model.addAttribute("changeNotices", changeNoticeRepository.findAll(pageable));
        return "/changenotices/list-changenotices";
    }

    @GetMapping("/showAddForm")
    public String showAddForm(Model model) {
        model.addAttribute("changeNoticeTo", new ChangeNoticeTo());
        model.addAttribute("developers", developerService.findAll());
        return "/changenotices/changenoticeTo-form";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable("id") int id, Model model) {
        model.addAttribute("changeNoticeTo", convertToToById(id));
        model.addAttribute("developers", developerService.findAll());
        return "/changenotices/changenoticeTo-form";
    }

    @PostMapping("/save")
    public String save(@Valid ChangeNoticeTo changeNoticeTo, BindingResult bindingResult, Model model) {
        List<FieldError> errorsToKeep = bindingResult.getFieldErrors().stream()
                .filter(fer -> !fer.getField().equals("tempDocumentDecimalNumber") && !fer.getField().equals("tempDocumentChangeNumber"))
                .collect(Collectors.toList());
        bindingResult = new BeanPropertyBindingResult(changeNoticeTo, "changeNoticeTo");
        for (FieldError fieldError : errorsToKeep) {
            bindingResult.addError(fieldError);
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("developers", developerService.findAll());
            //changeNoticeTo.setDocuments(new TreeMap<>());
            return "/changenotices/changenoticeTo-form";
        }

        if (changeNoticeTo.getId() == null) {
            model.addAttribute("changeNoticeTo", changeNoticeTo);
            return "/changenotices/changeNoticeTo-docs-list";
        }
        else {
            ChangeNotice changeNotice = convertFromTo(changeNoticeTo);
            changeNoticeService.update(changeNotice);
            return "redirect:/changenotices/showChangeNoticeInfo/" + changeNotice.getId();
        }
    }

    @GetMapping("/showChangeNoticeInfo/{id}")
    public String showChangeNoticeInfo(@PathVariable("id") int id, Model model) {
        ChangeNoticeTo changeNoticeTo = convertToToById(id);
        model.addAttribute("changeNoticeTo", changeNoticeTo);
        return "/changenotices/changenoticeToInfo";
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
        if (changeNoticeTo.getDocuments() == null) {
            changeNoticeTo.setDocuments(new TreeMap<>(Comparator.comparing(Document::getDecimalNumber)));
        }
        changeNoticeTo.getDocuments().put(newDocument, Integer.parseInt(changeNoticeTo.getTempDocumentChangeNumber()));

        if (changeNoticeTo.getId() == null) {
            ChangeNotice newChangeNotice = changeNoticeService.create(convertFromTo(changeNoticeTo));
            changeNoticeTo.setId(newChangeNotice.getId());
        }
        else {
            changeNoticeService.update(convertFromTo(changeNoticeTo));

        }
        return "redirect:/changenotices/showDocuments/" + changeNoticeTo.getId();
    }

    @GetMapping("/removeDoc/{id}/{decimalNumber}")
    public String removeDoc(@PathVariable("id") int changeNoticeId, @PathVariable("decimalNumber") String decimalNumber, Model model) {
        ChangeNotice updated = changeNoticeService.findById(changeNoticeId);
        if (updated.getDocuments().size() == 1) {
            model.addAttribute("changeNoticeTo", convertToToById(changeNoticeId));
            String errorMessage = "The change notice can not exist without any documents!";
            model.addAttribute("errorMessage", errorMessage);
            return "/changenotices/changeNoticeTo-docs-list";
        }
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
