package com.igar15.filecabinet.controller;

import com.igar15.filecabinet.entity.DbFile;
import com.igar15.filecabinet.entity.ElectronicImageDocument;
import com.igar15.filecabinet.service.ElectronicImageDocumentService;
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
            redirectAttributes.addFlashAttribute("emptyFileError", "File should not be empty!");
            return "redirect:/documents/showDocumentInfo/" + documentId;
        }
        else if (!file.getContentType().endsWith("/pdf")) {
            redirectAttributes.addFlashAttribute("typeFileError", "File should have pdf type!");
            return "redirect:/documents/showDocumentInfo/" + documentId;
        }
        electronicImageDocumentService.create(file, changeNumber, documentId);
        return "redirect:/documents/showDocumentInfo/" + documentId;
    }

    @GetMapping("/delete/{documentId}/{id}")
    public String delete(@PathVariable("documentId") int documentId,
                         @PathVariable("id") int id) {
        electronicImageDocumentService.delete(documentId, id);
        return "redirect:/documents/showDocumentInfo/" + documentId;
    }

    @GetMapping("/showFile/{documentId}/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("documentId") int documentId,
                                                 @PathVariable("id") int id) {
        // Load file from database
        ElectronicImageDocument electronicImageDocument = electronicImageDocumentService.findByIdAndDocumentIdWithElectronicImageData(documentId, id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(electronicImageDocument.getFileType()))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
                .body(new ByteArrayResource(electronicImageDocument.getElectronicImageData().getData()));
    }

}
