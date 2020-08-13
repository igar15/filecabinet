package com.igar15.filecabinet.controller;

import com.igar15.filecabinet.entity.InternalDispatch;
import com.igar15.filecabinet.service.DepartmentService;
import com.igar15.filecabinet.service.InternalDispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/internaldispatches")
public class InternalDispatchController {

    @Autowired
    private InternalDispatchService internalDispatchService;

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/list")
    public String showAll(@SortDefault(value = "dispatchDate", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        model.addAttribute("internalDispatches", internalDispatchService.findAll(pageable));
        return "/internaldispatches/internaldispatch-list";
    }

    @GetMapping("/showAddForm")
    public String showAddForm(Model model) {
        model.addAttribute("internalDispatch", new InternalDispatch());
        model.addAttribute("departments", departmentService.findAllByCanTakeAlbums(true));
        return "/internaldispatches/internaldispatch-form";
    }

    @PostMapping("/save")
    public String save(@Validated InternalDispatch internalDispatch, BindingResult bindingResult, Model model) {

//        List<FieldError> errorsToKeep = bindingResult.getFieldErrors().stream()
//                .filter(fer -> !fer.getField().equals("internalHandlerName")
//                        && !fer.getField().equals("internalHandlerPhoneNumber") && !fer.getField().equals("receivedInternalDate"))
//                .collect(Collectors.toList());
//        bindingResult = new BeanPropertyBindingResult(internalDispatch, "internalDispatch");
//        for (FieldError fieldError : errorsToKeep) {
//            bindingResult.addError(fieldError);
//        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentService.findAllByCanTakeAlbums(true));
            return "/internaldispatches/internaldispatch-form";
        }
        else {
            if (internalDispatch.isNew()) {
                internalDispatchService.create(internalDispatch);
            }
            else {
                internalDispatchService.updateWithoutChildren(internalDispatch);
            }
            return "redirect:/internaldispatches/showInternalDispatchInfo/" + internalDispatch.getId();
        }
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable("id") int id, Model model) {
        model.addAttribute("internalDispatch", internalDispatchService.findById(id));
        model.addAttribute("departments", departmentService.findAllByCanTakeAlbums(true));
        return "/internaldispatches/internaldispatch-form";
    }

    @GetMapping("/showInternalDispatchInfo/{id}")
    public String showInternalDispatchInfo(@PathVariable("id") int id, Model model) {
        model.addAttribute("internalDispatch", internalDispatchService.findByIdWithDocuments(id));
        return "/internaldispatches/internaldispatch-info";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
       internalDispatchService.deleteById(id);
        return "redirect:/internaldispatches/list";
    }

    @PostMapping("/addDocument/{id}")
    public String addDocument(@PathVariable("id") int id,
                              @RequestParam("newDocument") String newDocument,
                              Model model) {

        InternalDispatch internalDispatch = internalDispatchService.findByIdWithDocuments(id);
        String errorMessage = internalDispatchService.addDocument(internalDispatch, newDocument);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("internalDispatch", internalDispatch);
        return "/internaldispatches/internaldispatch-info";
    }


    @GetMapping("/removeDocument/{id}/{documentId}")
    public String removeDocument(@PathVariable("id") int id,
                            @PathVariable("documentId") int documentId,
                            Model model) {
        InternalDispatch internalDispatch = internalDispatchService.findByIdWithDocuments(id);
        String errorDeleteMessage = internalDispatchService.removeDocument(internalDispatch, documentId);
        model.addAttribute("errorDeleteMessage", errorDeleteMessage);
        model.addAttribute("internalDispatch", internalDispatch);
        return "/internaldispatches/internaldispatch-info";
    }

    @GetMapping("/list/albums")
    public String showAlbums(@RequestParam(value = "albumName", required = false) String albumName,
                             @SortDefault("albumName") Pageable pageable,
                             Model model) {
        Page<InternalDispatch> internalDispatches = null;
        if (albumName != null) {
            internalDispatches = internalDispatchService.findAllByAlbumNameContainsIgnoreCaseAndIsActive(albumName, true, pageable);
        }
        else {
            internalDispatches = internalDispatchService.findAllByIsAlbumAndIsActive(true, true, pageable);
        }
        model.addAttribute("internalDispatches", internalDispatches);
        return "/internaldispatches/internaldispatch-albums";
    }

    @GetMapping("/showAlbumInfo/{id}")
    public String showAlbumInfo(@PathVariable("id") int id, Model model) {
        model.addAttribute("internalDispatch", internalDispatchService.findByIdWithDocuments(id));
        return "/internaldispatches/internaldispatch-album-info";
    }

    @GetMapping("/showChangeHandlerForm/{id}")
    public String showChangeHandlerForm(@PathVariable("id") int id, Model model) {
        model.addAttribute("departments", departmentService.findAllByCanTakeAlbums(true));
        model.addAttribute("internalDispatch", internalDispatchService.findById(id));
        return "/internaldispatches/internaldispatch-album-handler-form";
    }

    @PostMapping("/changeHandler")
    public String changeHandler(@Valid InternalDispatch internalDispatch, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentService.findAllByCanTakeAlbums(true));
            return "/internaldispatches/internaldispatch-album-handler-form";
        }
        internalDispatchService.updateWithoutChildren(internalDispatch);
        return "redirect:/internaldispatches/list/albums";
    }

}
