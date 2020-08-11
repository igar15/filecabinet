package com.igar15.filecabinet.controller;

import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.entity.InternalDispatch;
import com.igar15.filecabinet.repository.InternalDispatchRepository;
import com.igar15.filecabinet.service.DepartmentService;
import com.igar15.filecabinet.service.DocumentService;
import com.igar15.filecabinet.service.InternalDispatchService;
import com.igar15.filecabinet.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/internaldispatches")
public class InternalDispatchController {

    @Autowired
    private InternalDispatchService internalDispatchService;

    @Autowired
    private InternalDispatchRepository internalDispatchRepository;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DocumentService documentService;

    @GetMapping("/list")
    public String showAll(@SortDefault(value = "dispatchDate", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        model.addAttribute("internalDispatches", internalDispatchRepository.findAll(pageable));
        return "/internaldispatches/internaldispatch-list";
    }

    @GetMapping("/showAddForm")
    public String showAddForm(Model model) {
        model.addAttribute("internalDispatch", new InternalDispatch());
        model.addAttribute("departments", departmentService.findAllByCanTakeAlbums(true));
        return "/internaldispatches/internaldispatch-form";
    }

    @PostMapping("/save")
    public String save(@Valid InternalDispatch internalDispatch, BindingResult bindingResult, Model model) {

        List<FieldError> errorsToKeep = bindingResult.getFieldErrors().stream()
                .filter(fer -> !fer.getField().equals("internalHandlerName")
                        && !fer.getField().equals("internalHandlerPhoneNumber") && !fer.getField().equals("receivedInternalDate"))
                .collect(Collectors.toList());
        bindingResult = new BeanPropertyBindingResult(internalDispatch, "internalDispatch");
        for (FieldError fieldError : errorsToKeep) {
            bindingResult.addError(fieldError);
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentService.findAllByCanTakeAlbums(true));
            return "/internaldispatches/internaldispatch-form";
        }
        else {
            if (internalDispatch.isNew()) {
                if (internalDispatch.getIsAlbum()) {
                    Document document = documentService.findByDecimalNumber(internalDispatch.getAlbumName());
                    internalDispatch.setDocuments(new HashMap<>());
                    internalDispatch.getDocuments().put(document, true);
                    internalDispatch.setInternalHandlerName("Kochetova T.");
                    internalDispatch.setReceivedInternalDate(internalDispatch.getDispatchDate());
                    internalDispatch.setInternalHandlerPhoneNumber("1-34-15");
                }
                internalDispatchService.create(internalDispatch);
            }
            else {
                internalDispatch.setDocuments(internalDispatchService.findById(internalDispatch.getId()).getDocuments());
                internalDispatchService.update(internalDispatch);
            }
            model.addAttribute("internalDispatch", internalDispatch);
            return "/internaldispatches/internaldispatch-info";
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
        model.addAttribute("internalDispatch", internalDispatchService.findById(id));
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

        String errorMessage = null;
        InternalDispatch internalDispatch = internalDispatchService.findById(id);
        if (newDocument == null || newDocument.trim().isEmpty()) {
            errorMessage = "Decimal number must not be empty";
        }
        else {
            try {
                Document document = documentService.findByDecimalNumber(newDocument);
                if (internalDispatch.getDocuments().containsKey(document)) {
                    errorMessage = "Document already added";
                }
                else {
                    internalDispatch.getDocuments().put(document, true);
                    internalDispatchService.update(internalDispatch);
                }

            } catch (NotFoundException e) {
                errorMessage = "Document not found";
            }
        }
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("internalDispatch", internalDispatch);
        return "/internaldispatches/internaldispatch-info";
    }


    @GetMapping("/removeDoc/{id}/{documentId}")
    public String removeDoc(@PathVariable("id") int id,
                            @PathVariable("documentId") int documentId,
                            Model model) {
        String errorDeleteMessage = null;
        InternalDispatch found = internalDispatchService.findById(id);

        if (found.getDocuments().size() < 2) {
            errorDeleteMessage = "Internal dispatch " + found.getWaybill() + " can not exist without any documents!";
        }
        else {
            Document document = documentService.findById(documentId);
            found.getDocuments().remove(document);
            internalDispatchService.update(found);
        }
        model.addAttribute("errorDeleteMessage", errorDeleteMessage);
        model.addAttribute("internalDispatch", found);
        return "/internaldispatches/internaldispatch-info";
    }

    @GetMapping("/list/albums")
    public String showAlbums(@RequestParam(value = "albumName", required = false) String albumName,
                             @SortDefault("albumName") Pageable pageable,
                             Model model) {
        if (albumName != null) {
            albumName = albumName.trim();
        }
        albumName = "".equals(albumName) ? null : albumName;
        Page<InternalDispatch> internalDispatches = null;
        if (albumName != null) {
            internalDispatches = internalDispatchRepository.findByAlbumNameContainsIgnoreCaseAndIsActive(albumName, true, pageable);
        }
        else {
            internalDispatches = internalDispatchService.findByIsAlbumAndIsActive(true, true, pageable);
        }
        model.addAttribute("internalDispatches", internalDispatches);
        return "/internaldispatches/internaldispatch-albums";
    }

    @GetMapping("/showAlbumInfo/{id}")
    public String showAlbumInfo(@PathVariable("id") int id, Model model) {
        model.addAttribute("internalDispatch", internalDispatchService.findById(id));
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
        internalDispatch.setDocuments(internalDispatchService.findById(internalDispatch.getId()).getDocuments());
        internalDispatchService.update(internalDispatch);
        return "redirect:/internaldispatches/list/albums";
    }

}
