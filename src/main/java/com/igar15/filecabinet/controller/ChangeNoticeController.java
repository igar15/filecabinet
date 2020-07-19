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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;
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
        if (bindingResult.hasErrors()) {
            changeNoticeTo.setDeveloperNames(controllersHelperUtil.getDeveloperNames());
            return "/changenotices/changenoticeTo-form";
        }
        else {
            model.addAttribute("changeNoticeTo", changeNoticeTo);
            return "/changenotices/changenoticeTo-docs-add-form";
        }
    }

    @PostMapping("/addDocument")
    public String addDocument(@Valid ChangeNoticeTo changeNoticeTo, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "/changenotices/changenoticeTo-docs-add-form";
        }
        else {
            changeNoticeTo.getDocumentDecimalNumbers().add(changeNoticeTo.getTempDocumentDecimalNumber());
            model.addAttribute("changeNoticeTo", changeNoticeTo);
            return "/changenotices/changenoticeTo-docs-add-form";
        }
    }

    @PostMapping("/save")
    public String save(@Valid ChangeNoticeTo changeNoticeTo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/changenotices/changenoticeTo-form";
        }
        else {
            ChangeNotice changeNotice = convertFromTo(changeNoticeTo);
            if (changeNotice.getId() == null) {
                changeNoticeService.create(changeNotice);
            }
            else changeNoticeService.update(changeNotice);
        }
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


}
