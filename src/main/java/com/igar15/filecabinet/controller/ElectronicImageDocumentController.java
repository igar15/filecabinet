package com.igar15.filecabinet.controller;

import com.igar15.filecabinet.entity.ElectronicImageDocument;
import com.igar15.filecabinet.service.ElectronicImageDocumentService;
import com.igar15.filecabinet.util.exception.ChangeNumberDuplicateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/electronicImageDocuments")
public class ElectronicImageDocumentController {

    @Autowired
    private ElectronicImageDocumentService electronicImageDocumentService;

    @PostMapping("/save/{documentId}")
    public String save(@PathVariable("documentId") int documentId,
                       @RequestParam("file") MultipartFile file,
                       @RequestParam("changeNumber") Integer changeNumber,
                       RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("addFileError", "File should not be empty!");
            if (changeNumber == null) {
                redirectAttributes.addFlashAttribute("changeNumberError", "Change number should not be empty!");
            }
            return "redirect:/documents/showDocumentInfo/" + documentId;
        }
        else if (!file.getContentType().endsWith("/pdf")) {
            redirectAttributes.addFlashAttribute("addFileError", "File should have pdf type!");
            if (changeNumber == null) {
                redirectAttributes.addFlashAttribute("changeNumberError", "Change number should not be empty!");
            }
            return "redirect:/documents/showDocumentInfo/" + documentId;
        }
        else if (changeNumber == null) {
            redirectAttributes.addFlashAttribute("changeNumberError", "Change number should not be empty!");
            return "redirect:/documents/showDocumentInfo/" + documentId;
        }
        try {
            electronicImageDocumentService.create(file, changeNumber, documentId);
        } catch (ChangeNumberDuplicateException e) {
            redirectAttributes.addFlashAttribute("changeNumberError", e.getMessage());
            redirectAttributes.addFlashAttribute("changeNumber", changeNumber);
            return "redirect:/documents/showDocumentInfo/" + documentId;
        }
        return "redirect:/documents/showDocumentInfo/" + documentId;
    }

    @GetMapping("/delete/{documentId}/{id}")
    public String delete(@PathVariable("documentId") int documentId,
                         @PathVariable("id") int id) {
        electronicImageDocumentService.delete(documentId, id);
        return "redirect:/documents/showDocumentInfo/" + documentId;
    }

    @GetMapping("/showFile/{documentId}/{id}")
    public void showFile(@PathVariable("documentId") int documentId,
                         @PathVariable("id") int id,
                         HttpServletResponse response) throws IOException {
        // Load file from database
        ElectronicImageDocument electronicImageDocument = electronicImageDocumentService.findByIdAndDocumentIdWithElectronicImageData(documentId, id);

        response.setContentType("application/pdf");

        InputStream inputStream = new ByteArrayInputStream(electronicImageDocument.getElectronicImageData().getData());
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(inputStream.readAllBytes());
    }

    @GetMapping("/showNonAnnulledFile/{documentId}")
    public void showNonAnnulledFile(@PathVariable("documentId") int documentId,
                                    HttpServletResponse response) throws IOException {
        // Load file from database
        ElectronicImageDocument electronicImageDocument = electronicImageDocumentService.findByDocumentIdAndNonAnnulledWithElectronicImageData(documentId);

        response.setContentType("application/pdf");

        InputStream inputStream = new ByteArrayInputStream(electronicImageDocument.getElectronicImageData().getData());
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(inputStream.readAllBytes());
    }

    @GetMapping("/annull/{documentId}/{id}")
    public String annull(@PathVariable("documentId") int documentId,
                         @PathVariable("id") int id) {
        electronicImageDocumentService.annull(documentId, id);
        return "redirect:/documents/showDocumentInfo/" + documentId;
    }

    @GetMapping("/showPreviousVersions/{documentId}/{decimalNumber}")
    public String showPreviousVersions(@PathVariable("documentId") int documentId,
                                       @PathVariable("decimalNumber") String decimalNumber,
                                       Model model) {
        List<ElectronicImageDocument> electronicImageDocuments = electronicImageDocumentService.findAllByNonAnnulledAndDocumentId(false, documentId);
        model.addAttribute("documentId", documentId);
        model.addAttribute("decimalNumber", decimalNumber);
        model.addAttribute("electronicImageDocuments", electronicImageDocuments);
        return "documents/document-previousVersions";
    }

    @GetMapping("/deletePrevious/{documentId}/{id}/{decimalNumber}")
    public String deletePrevious(@PathVariable("documentId") int documentId,
                                 @PathVariable("id") int id,
                                 @PathVariable("decimalNumber") String decimalNumber) {
        electronicImageDocumentService.delete(documentId, id);
        return "redirect:/electronicImageDocuments/showPreviousVersions/" + documentId + "/" + URLEncoder.encode(decimalNumber, StandardCharsets.UTF_8);
    }

}
