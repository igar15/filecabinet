package com.igar15.filecabinet.controller;

import com.igar15.filecabinet.entity.ChangeNotice;
import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.repository.ChangeNoticeRepository;
import com.igar15.filecabinet.service.ChangeNoticeService;
import com.igar15.filecabinet.service.DepartmentService;
import com.igar15.filecabinet.service.DocumentService;
import com.igar15.filecabinet.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/changenotices")
public class ChangeNoticeController {


    @Autowired
    private ChangeNoticeService changeNoticeService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private ChangeNoticeRepository changeNoticeRepository;

    @GetMapping("/list")
    public String showAll(@RequestParam(name = "name", required = false) String name,
                          @RequestParam(name = "department", required = false) String department,
                          @RequestParam(name = "changeCode", required = false) String changeCode,
                          @RequestParam(name = "after", required = false) String after,
                          @RequestParam(name = "before", required = false) String before,
                          @SortDefault(value = "issueDate", direction = Sort.Direction.DESC) Pageable pageable,
                          Model model) {
        if (name != null) {
            name = name.trim();
        }
        name = "".equals(name) ? null : name;
        department = "".equals(department) ? null : department;
        changeCode = "".equals(changeCode) ? null : changeCode;
        LocalDate afterDate = (after == null || "".equals(after)) ? LocalDate.of(1900, 1, 1) : LocalDate.parse(after);
        LocalDate beforeDate = (before == null || "".equals(before)) ? LocalDate.of(2050, 1, 1) : LocalDate.parse(before);

        model.addAttribute("departments", departmentService.findAllByIsDeveloper(true));
        model.addAttribute("department", department);

        Page<ChangeNotice> changeNotices = null;

        Integer changeCodeInt = null;
        try {
            changeCodeInt = changeCode == null ? null : Integer.valueOf(changeCode);
        } catch (NumberFormatException e) {
            changeCodeInt = - 1;
        }

        if (checkParamsOnNull(name, department, changeCode)) {
            changeNotices = changeNoticeRepository.findByIssueDateGreaterThanEqualAndIssueDateLessThanEqual(afterDate, beforeDate, pageable);
        }
        else if (checkParamsOnNull(name, department)) {
            changeNotices = changeNoticeRepository.findByChangeCodeAndIssueDateGreaterThanEqualAndIssueDateLessThanEqual(changeCodeInt, afterDate, beforeDate, pageable);
        } else if (checkParamsOnNull(name, changeCode)) {
            changeNotices = changeNoticeRepository.findByDepartment_NameAndIssueDateGreaterThanEqualAndIssueDateLessThanEqual(department, afterDate, beforeDate, pageable);
        } else if (checkParamsOnNull(name)) {
            changeNotices = changeNoticeRepository.findByDepartment_NameAndChangeCodeAndIssueDateGreaterThanEqualAndIssueDateLessThanEqual(department, changeCodeInt, afterDate, beforeDate, pageable);
        }
        else {
            ChangeNotice changeNotice = new ChangeNotice();
            changeNotice.setName(name);
            changeNotice.setDepartment(departmentService.findByName(department));
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
        return "/changenotices/changenotice-list";
    }

    @GetMapping("/showAddForm")
    public String showAddForm(Model model) {
        model.addAttribute("changeNotice", new ChangeNotice());
        model.addAttribute("departments", departmentService.findAllByIsDeveloper(true));
        return "/changenotices/changenotice-form";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable("id") int id, Model model) {
        model.addAttribute("changeNotice", changeNoticeService.findById(id));
        model.addAttribute("departments", departmentService.findAll());
        return "/changenotices/changenotice-form";
    }

    @PostMapping("/save")
    public String save(@Valid ChangeNotice changeNotice, BindingResult bindingResult, Model model) {
        bindingResult = checkNameOnDuplicate(changeNotice, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentService.findAll());
            return "/changenotices/changenotice-form";
        }

        if (changeNotice.isNew()) {
            model.addAttribute("changeNotice", changeNotice);
            return "/changenotices/changenotice-document-list(for new)";
        }
        else {
            changeNotice.setDocuments(changeNoticeService.findById(changeNotice.getId()).getDocuments());
            changeNoticeService.update(changeNotice);
            model.addAttribute("changeNotice", changeNotice);
            return "/changenotices/changenotice-info";
        }
    }

    @GetMapping("/showChangeNoticeInfo/{id}")
    public String showChangeNoticeInfo(@PathVariable("id") int id, Model model) {
        model.addAttribute("changeNotice", changeNoticeService.findById(id));
        return "/changenotices/changenotice-info";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        changeNoticeService.deleteById(id);
        return "redirect:/changenotices/list";
    }

    @GetMapping("/showDocuments/{id}")
    public String showDocuments(@PathVariable("id") int id, Model model) {
        model.addAttribute("changeNotice", changeNoticeService.findById(id));
        return "/changenotices/changenotice-document-list";
    }

    @PostMapping("/addDoc/{id}")
    public String addDoc(@PathVariable("id") int id,
                         @RequestParam(value = "newDocument") String newDocument,
                         @RequestParam(value = "newDocumentChangeNumber") String newDocumentChangeNumber,
                         Model model) {

        String docErrorMessage = null;
        String numberErrorMessage = null;

        ChangeNotice changeNotice = changeNoticeService.findById(id);
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
            Document document = null;
            try {
                document = documentService.findByDecimalNumber(newDocument);
            } catch (NotFoundException e) {
            }
            if (document == null) {
                docErrorMessage = "Document does not exist";
            }
            else if (changeNotice.getDocuments().containsKey(document)) {
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
                        changeNotice.getDocuments().put(document, changeNumber);
                        changeNoticeService.update(changeNotice);
                    }
                } catch (NumberFormatException e) {
                    numberErrorMessage = "Invalid change number";
                }
            }
        }

        model.addAttribute("changeNotice", changeNotice);
        model.addAttribute("docErrorMessage", docErrorMessage);
        model.addAttribute("numberErrorMessage", numberErrorMessage);
        return "/changenotices/changenotice-document-list";
    }

    @PostMapping("/addDocForNew")
    public String addDocForNew(@RequestParam(value = "newDocument") String newDocument,
                                @RequestParam(value = "newDocumentChangeNumber") String newDocumentChangeNumber,
                                ChangeNotice changeNotice,
                                Model model) {

        String docErrorMessage = null;
        String numberErrorMessage = null;

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
            Document document = null;
            try {
                document = documentService.findByDecimalNumber(newDocument);
            } catch (NotFoundException e) {
            }
            if (document == null) {
                docErrorMessage = "Document does not exist";
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
                        changeNotice.setDocuments(new HashMap<>());
                        changeNotice.getDocuments().put(document, changeNumber);
                        changeNotice = changeNoticeService.create(changeNotice);
                    }
                } catch (NumberFormatException e) {
                    numberErrorMessage = "Invalid change number";
                }
            }
        }

        model.addAttribute("changeNotice", changeNotice);
        model.addAttribute("docErrorMessage", docErrorMessage);
        model.addAttribute("numberErrorMessage", numberErrorMessage);
        if (docErrorMessage == null && numberErrorMessage == null) {
            return "/changenotices/changenotice-document-list";
        }
        else {
            return "/changenotices/changenotice-document-list(for new)";
        }
    }

    @GetMapping("/removeDoc/{id}/{documentId}")
    public String removeDoc(@PathVariable("id") int id, @PathVariable("documentId") String documentId, Model model) {
        ChangeNotice changeNotice = changeNoticeService.findById(id);
        if (changeNotice.getDocuments().size() == 1) {
            model.addAttribute("changeNotice", changeNotice);
            String errorMessage = "The change notice can not exist without any documents!";
            model.addAttribute("errorMessage", errorMessage);
            return "/changenotices/changenotice-document-list";
        }
        Document document = changeNotice.getDocuments().keySet().stream()
                .filter(doc -> doc.getId().equals(Integer.valueOf(documentId)))
                .findFirst().orElse(null);
        changeNotice.getDocuments().remove(document);
        changeNoticeService.update(changeNotice);
        model.addAttribute("changeNotice", changeNotice);
        return "/changenotices/changenotice-document-list";
    }

    private boolean checkParamsOnNull(String... params) {
        return Arrays.stream(params)
                .allMatch(Objects::isNull);
    }

    private BindingResult checkNameOnDuplicate(ChangeNotice obj, BindingResult bindingResult) {
        boolean isUnique = true;
        ChangeNotice changeNotice = changeNoticeService.findByName(obj.getName());

        if (obj.isNew()) {
            isUnique = changeNotice == null;
        }
        else if (changeNotice != null && !changeNotice.getId().equals(obj.getId())) {
            isUnique = false;
        }
        if (!isUnique) {
            bindingResult.rejectValue("name", "error.changeNotice", "Change notice already exist");
        }
        return bindingResult;
    }

}
