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
import org.springframework.data.domain.*;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;
import java.time.LocalDate;
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
    public String showAll(@RequestParam(name = "name", required = false) String name,
                          @RequestParam(name = "developer", required = false) String developer,
                          @RequestParam(name = "changeCode", required = false) String changeCode,
                          @RequestParam(name = "after", required = false) String after,
                          @RequestParam(name = "before", required = false) String before,
                          @SortDefault(value = "issueDate", direction = Sort.Direction.DESC) Pageable pageable,
                          Model model) {
        name = "".equals(name) ? null : name;
        developer = "".equals(developer) ? null : developer;
        changeCode = "".equals(changeCode) ? null : changeCode;
        LocalDate afterDate = (after == null || "".equals(after)) ? LocalDate.of(1900, 1, 1) : LocalDate.parse(after);
        LocalDate beforeDate = (before == null || "".equals(before)) ? LocalDate.of(2050, 1, 1) : LocalDate.parse(before);

        model.addAttribute("developers", developerService.findAll());
        model.addAttribute("developer", developer);

        Page<ChangeNotice> changeNotices = null;

        Integer changeCodeInt = null;
        try {
            changeCodeInt = changeCode == null ? null : Integer.valueOf(changeCode);
        } catch (NumberFormatException e) {
            changeCodeInt = - 1;
        }

        if (checkParamsOnNull(name, developer, changeCode)) {
            changeNotices = changeNoticeRepository.findByIssueDateGreaterThanEqualAndIssueDateLessThanEqual(afterDate, beforeDate, pageable);
        }
        else if (checkParamsOnNull(name, developer)) {
            changeNotices = changeNoticeRepository.findByChangeCodeAndIssueDateGreaterThanEqualAndIssueDateLessThanEqual(changeCodeInt, afterDate, beforeDate, pageable);
        } else if (checkParamsOnNull(name, changeCode)) {
            changeNotices = changeNoticeRepository.findByDeveloper_NameAndIssueDateGreaterThanEqualAndIssueDateLessThanEqual(developer, afterDate, beforeDate, pageable);
        } else if (checkParamsOnNull(name)) {
            changeNotices = changeNoticeRepository.findByDeveloper_NameAndChangeCodeAndIssueDateGreaterThanEqualAndIssueDateLessThanEqual(developer, changeCodeInt, afterDate, beforeDate, pageable);
        }
        else {
            ChangeNotice changeNotice = new ChangeNotice();
            changeNotice.setName(name);
            changeNotice.setDeveloper(developerService.findByName(developer));
            changeNotice.setChangeCode(changeCodeInt);
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withMatcher("name", ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING).ignoreCase());
            Example<ChangeNotice> example = Example.of(changeNotice, matcher);
            changeNotices = changeNoticeRepository.findAll(example, pageable);
            if (after != null || !"".equals(after) || before != null || !"".equals(before)) {
                List<ChangeNotice> collect = changeNotices.get()
                        .filter(change -> (change.getIssueDate().compareTo(afterDate) >= 0) && (change.getIssueDate().compareTo(beforeDate) <= 0))
                        .collect(Collectors.toList());
                changeNotices = new PageImpl<>(collect, pageable, pageable.getOffset());
            }
        }
        model.addAttribute("changeNotices", changeNotices);
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

    @PostMapping("/addDoc/{id}")
    public String addDoc(@PathVariable("id") int id,
                         @RequestParam(value = "newDocument") String newDocument,
                         @RequestParam(value = "newDocumentChangeNumber") String newDocumentChangeNumber,
                         Model model) {

        String docErrorMessage = null;
        String numberErrorMessage = null;

        ChangeNoticeTo changeNoticeTo = convertToToById(id);
        if (newDocument == null || newDocument.trim().isEmpty()) {
            docErrorMessage = "Decimal number must not be empty";
            if (newDocumentChangeNumber == null || newDocumentChangeNumber.trim().isEmpty()) {
                numberErrorMessage = "Change number must not be empty";
            }
        }
        else if (newDocumentChangeNumber == null || newDocumentChangeNumber.trim().isEmpty()) {
            numberErrorMessage = "Change number must not be empty";
            if (newDocument == null || newDocument.trim().isEmpty()) {
                docErrorMessage = "Decimal number must not be empty";
            }
        }
        else {
            Document document = documentService.findByDecimalNumber(newDocument);
            if (document == null) {
                docErrorMessage = "Document does not exist";
            }
            else if (changeNoticeTo.getDocuments().containsKey(document)) {
                docErrorMessage = "This change notice already has this document";
            }
            else {
                Integer changeNumber = null;
                try {
                    changeNumber = Integer.valueOf(newDocumentChangeNumber);
                    if (changeNumber < 1) {
                        numberErrorMessage = "Change number must be greater than 0";
                    }
                    else if (document.getChangeNotices().containsKey(changeNumber)){
                        numberErrorMessage = "Document already has this change number";
                    }
                    else {
                        changeNoticeTo.getDocuments().put(document, changeNumber);
                        changeNoticeService.update(convertFromTo(changeNoticeTo));
                    }
                } catch (NumberFormatException e) {
                    numberErrorMessage = "Invalid change number";
                }
            }
        }

        model.addAttribute("changeNoticeTo", changeNoticeTo);
        model.addAttribute("docErrorMessage", docErrorMessage);
        model.addAttribute("numberErrorMessage", numberErrorMessage);
        return "/changenotices/changeNoticeTo-docs-list";
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


    private boolean checkParamsOnNull(String... params) {
        return Arrays.stream(params)
                .allMatch(Objects::isNull);
    }

}
