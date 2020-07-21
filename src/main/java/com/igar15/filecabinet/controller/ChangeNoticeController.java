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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        model.addAttribute("changeNoticeTo", new ChangeNoticeTo(controllersHelperUtil.getDeveloperNames()));
        return "/changenotices/changenoticeTo-form";
    }

    @PostMapping("/showFormForDocsAdd")
    public String showFormForDocsAdd(@Valid ChangeNoticeTo changeNoticeTo, BindingResult bindingResult, Model model) {
        List<FieldError> errorsToKeep = bindingResult.getFieldErrors().stream()
                .filter(fer -> !fer.getField().equals("tempDocumentDecimalNumber") && !fer.getField().equals("documentDecimalNumbers"))
                .collect(Collectors.toList());
        bindingResult = new BeanPropertyBindingResult(changeNoticeTo, "changeNoticeTo");
        for (FieldError fieldError : errorsToKeep) {
            bindingResult.addError(fieldError);
        }
        if (changeNoticeTo.getDocumentDecimalNumbers().size() == 0) {
            changeNoticeTo.setDocumentDecimalNumbers(null);
        }

        if (bindingResult.hasErrors()) {
            changeNoticeTo.setDeveloperNames(controllersHelperUtil.getDeveloperNames());
            return "/changenotices/changenoticeTo-form";
        }
        else {
            if (changeNoticeTo.getId() == null) {
                model.addAttribute("changeNoticeTo", changeNoticeTo);
                return "/changenotices/changenoticeTo-docs-add-form";
            }
            else {
                ChangeNotice changeNotice = convertFromTo(changeNoticeTo);
                changeNoticeService.update(changeNotice);
                return "redirect:/changenotices/showChangeNoticeInfo/" + changeNotice.getId();
            }
        }
    }

    @PostMapping("/addDocument")
    public String addDocument(@Valid ChangeNoticeTo changeNoticeTo, BindingResult bindingResult, Model model) {
        List<FieldError> errorsToKeep = bindingResult.getFieldErrors().stream()
                .filter(fer -> !fer.getField().equals("documentDecimalNumbers"))
                .collect(Collectors.toList());
        bindingResult = new BeanPropertyBindingResult(changeNoticeTo, "changeNoticeTo");
        for (FieldError fieldError : errorsToKeep) {
            bindingResult.addError(fieldError);
        }
        if (bindingResult.hasErrors()) {
            return "/changenotices/changenoticeTo-docs-add-form";
        }
        else {
            if (changeNoticeTo.getDocumentDecimalNumbers() == null) {
                changeNoticeTo.setDocumentDecimalNumbers(new HashSet<>());
            }
            changeNoticeTo.getDocumentDecimalNumbers().add(changeNoticeTo.getTempDocumentDecimalNumber());
            changeNoticeTo.setTempDocumentDecimalNumber(null);
            model.addAttribute("changeNoticeTo", changeNoticeTo);
            return "/changenotices/changenoticeTo-docs-add-form";
        }
    }

    @PostMapping("/addDocumentForNotNew")
    public String addDocumentForNotNew(@Valid ChangeNoticeTo changeNoticeTo, BindingResult bindingResult, Model model) {
        List<FieldError> errorsToKeep = bindingResult.getFieldErrors().stream()
                .filter(fer -> !fer.getField().equals("tempDocumentDecimalNumber") && !fer.getField().equals("documentDecimalNumbers"))
                .collect(Collectors.toList());
        bindingResult = new BeanPropertyBindingResult(changeNoticeTo, "changeNoticeTo");
        for (FieldError fieldError : errorsToKeep) {
            bindingResult.addError(fieldError);
        }

        if (bindingResult.hasErrors()) {
            return "/changenotices/changenoticeToInfo";
        }
        else {
            if (changeNoticeTo.getDocumentDecimalNumbers().size() == 0) changeNoticeTo.setDocumentDecimalNumbers(null);
            model.addAttribute("changeNoticeTo", changeNoticeTo);
            return "/changenotices/changenoticeTo-docs-add-form";
        }
    }



    @PostMapping("/save")
    public String save(@Valid ChangeNoticeTo changeNoticeTo, BindingResult bindingResult) {
        Integer id = null;
        List<FieldError> errorsToKeep = bindingResult.getFieldErrors().stream()
                .filter(fer -> !fer.getField().equals("tempDocumentDecimalNumber"))
                .collect(Collectors.toList());
        bindingResult = new BeanPropertyBindingResult(changeNoticeTo, "changeNoticeTo");
        for (FieldError fieldError : errorsToKeep) {
            bindingResult.addError(fieldError);
        }
        if (bindingResult.hasErrors()) {
            changeNoticeTo.setDocumentDecimalNumbers(new HashSet<>());
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
        return "/changenotices/changenoticeTo-form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        changeNoticeService.deleteById(id);
        return "redirect:/changenotices/list";
    }


    private ChangeNotice convertFromTo(ChangeNoticeTo changeNoticeTo) {
        Developer developer = (changeNoticeTo.getDeveloperName() == null) ? null : developerService.findByName(changeNoticeTo.getDeveloperName());
        List<Document> documents = (changeNoticeTo.getDocumentDecimalNumbers() != null && changeNoticeTo.getDocumentDecimalNumbers().size() > 0) ?
                changeNoticeTo.getDocumentDecimalNumbers().stream()
                        .map(docDecimalNumber -> documentService.findByDecimalNumber(docDecimalNumber))
                        .collect(Collectors.toList()) : null;
        ChangeNotice changeNotice = new ChangeNotice(changeNoticeTo.getId(), changeNoticeTo.getName(), changeNoticeTo.getChangeCode(), developer, documents);
        return changeNotice;
    }

    private ChangeNoticeTo convertToToById(int id) {
        ChangeNotice found = changeNoticeService.findById(id);
        String developerName = (found.getDeveloper() != null) ? found.getDeveloper().getName() : null;
        Set<String> documentDecimalNumbers = found.getDocuments().stream()
                .map(Document::getDecimalNumber)
                .collect(Collectors.toSet());
        return new ChangeNoticeTo(found.getId(), found.getName(), found.getChangeCode(), developerName, documentDecimalNumbers, controllersHelperUtil.getDeveloperNames());
    }


}
