package com.igar15.filecabinet.controller;

import com.igar15.filecabinet.dto.ExternalDispatchTo;
import com.igar15.filecabinet.dto.InternalDispatchTo;
import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.entity.ExternalDispatch;
import com.igar15.filecabinet.entity.InternalDispatch;
import com.igar15.filecabinet.service.DeveloperService;
import com.igar15.filecabinet.service.DocumentService;
import com.igar15.filecabinet.service.InternalDispatchService;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/internaldispatches")
public class InternalDispatchController {

    @Autowired
    private InternalDispatchService internalDispatchService;

    @Autowired
    private DeveloperService developerService;

    @Autowired
    private DocumentService documentService;

    @GetMapping("/list")
    public String showAll(Model model) {
        model.addAttribute("internalDispatches", internalDispatchService.findAll());
        return "/internaldispatches/list-internaldispatches";
    }

    @GetMapping("/showAddForm")
    public String showAddForm(Model model) {
        model.addAttribute("internalDispatchTo", new InternalDispatchTo());
        model.addAttribute("departments", developerService.findByCanTakeAlbums(true));
        return "/internaldispatches/internalDispatchTo-form";
    }

    @PostMapping("/save")
    public String save(@Valid InternalDispatchTo internalDispatchTo, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", developerService.findByCanTakeAlbums(true));
            return "/internaldispatches/internalDispatchTo-form";
        }
        else {
            if (internalDispatchTo.getId() == null) {
                model.addAttribute("internalDispatchTo", internalDispatchTo);
                return "/internaldispatches/internalDispatchToInfo";
            }
            else {
                internalDispatchService.update(convertFromTo(internalDispatchTo));
                return "redirect:/internaldispatches/showInternalDispatchInfo/" + internalDispatchTo.getId();
            }
        }
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable("id") int id, Model model) {
        model.addAttribute("internalDispatchTo", convertToToById(id));
        model.addAttribute("departments", developerService.findByCanTakeAlbums(true));
        return "/internaldispatches/internalDispatchTo-form";
    }

    @GetMapping("/showInternalDispatchInfo/{id}")
    public String showInternalDispatchInfo(@PathVariable("id") int id, Model model) {
        model.addAttribute("internalDispatchTo", convertToToById(id));
        return "/internaldispatches/internalDispatchToInfo";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
       internalDispatchService.deleteById(id);
        return "redirect:/internaldispatches/list";
    }

    @PostMapping("/addDocument")
    public String addDocument(@Valid InternalDispatchTo internalDispatchTo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/externaldispatches/externalDispatchToInfo";
        }
        Document added = documentService.findByDecimalNumber(internalDispatchTo.getTempDocumentDecimalNumber());
        internalDispatchTo.getDocuments().add(added);
        if (internalDispatchTo.getId() == null) {
            InternalDispatch newInternalDispatch = internalDispatchService.create(convertFromTo(internalDispatchTo));
            internalDispatchTo.setId(newInternalDispatch.getId());
        }
        else {
            internalDispatchService.update(convertFromTo(internalDispatchTo));
        }
        return "redirect:/internaldispatches/showInternalDispatchInfo/" + internalDispatchTo.getId();
    }

    @GetMapping("/removeDoc/{id}/{decimalNumber}")
    public String removeDoc(@PathVariable("id") int id, @PathVariable("decimalNumber") String decimalNumber, Model model) {
        Document removed = documentService.findByDecimalNumber(decimalNumber);
        InternalDispatch found = internalDispatchService.findById(id);
        if (found.getDocuments().size() == 1) {
            model.addAttribute("internalDispatchTo", convertToToById(id));
            String errorMessage = "Internal dispatch " + found.getWaybill() + " can not exist without any documents!";
            model.addAttribute("errorMessage", errorMessage);
            return "/internaldispatches/internalDispatchToInfo";
        }
        found.getDocuments().remove(removed);
        internalDispatchService.update(found);
        return "redirect:/internaldispatches/showInternalDispatchInfo/" + id;
    }

    @GetMapping("/list/albums")
    public String showAlbums(Model model) {
        model.addAttribute("albums", internalDispatchService.findByIsAlbum(true));
        return "/internaldispatches/list-albums";
    }

    @GetMapping("/showAlbumInfo/{id}")
    public String showAlbumInfo(@PathVariable("id") int id, Model model) {
        model.addAttribute("album", internalDispatchService.findById(id));
        return "/internaldispatches/album-info";
    }










    private InternalDispatchTo convertToToById(int id) {
        InternalDispatch found = internalDispatchService.findById(id);
        return new InternalDispatchTo(found.getId(), found.getWaybill(), found.getDispatchDate(), found.getStatus(), found.getRemark(),
                found.getStamp(), found.getDispatchHandler(), found.getReceivedInternalDate(),
                found.getInternalHandlerName(), found.getInternalHandlerPhoneNumber(), found.getIsAlbum(), found.getAlbumName(), found.getDocuments());
    }

    private InternalDispatch convertFromTo(InternalDispatchTo internalDispatchTo) {
        return new InternalDispatch(internalDispatchTo.getId(), internalDispatchTo.getWaybill(), internalDispatchTo.getDispatchDate(),
                internalDispatchTo.getStatus(), internalDispatchTo.getRemark(), internalDispatchTo.getStamp(), internalDispatchTo.getDispatchHandler(),
                internalDispatchTo.getDocuments(), internalDispatchTo.getReceivedInternalDate(), internalDispatchTo.getInternalHandlerName(),
                internalDispatchTo.getInternalHandlerPhoneNumber(), internalDispatchTo.getIsAlbum(), internalDispatchTo.getAlbumName());
    }
}
