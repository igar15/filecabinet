package com.igar15.filecabinet.controller;

import com.igar15.filecabinet.entity.DbFile;
import com.igar15.filecabinet.service.DbFileService;
import com.igar15.filecabinet.service.DocumentService;
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
@RequestMapping("/files")
public class DbFileController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private DbFileService dbFileService;

    @GetMapping("/showFiles/{documentId}/{documentDecimalNumber}")
    public String showFiles(@PathVariable("documentId") int documentId,
                            @PathVariable("documentDecimalNumber") String decimalNumber,
                            Model model) {
        List<DbFile> dbFiles = dbFileService.findAllByDocumentId(documentId);
        model.addAttribute("documentId", documentId);
        model.addAttribute("decimalNumber", decimalNumber);
        model.addAttribute("dbFiles", dbFiles);
        return "/documents/document-dbFiles";
    }

    @PostMapping("/save/{documentId}/{decimalNumber}")
    public String save(@PathVariable("documentId") int documentId,
                       @PathVariable("decimalNumber") String decimalNumber,
                       @RequestParam("file") MultipartFile file,
                       RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("emptyFileError", "File should not be empty!");
            return "redirect:/files/showFiles/" + documentId + "/" + URLEncoder.encode(decimalNumber, StandardCharsets.UTF_8);
        }
        else if (!file.getContentType().endsWith("/pdf")) {
            redirectAttributes.addFlashAttribute("typeFileError", "File should have pdf type!");
            return "redirect:/files/showFiles/" + documentId + "/" + URLEncoder.encode(decimalNumber, StandardCharsets.UTF_8);
        }
        dbFileService.create(file, documentId);
        return "redirect:/files/showFiles/" + documentId + "/" + URLEncoder.encode(decimalNumber, StandardCharsets.UTF_8);
    }

    @GetMapping("/delete/{documentId}/{decimalNumber}/{id}")
    public String delete(@PathVariable("documentId") int documentId,
                         @PathVariable("decimalNumber") String decimalNumber,
                         @PathVariable("id") int id) {
        dbFileService.delete(documentId, id);
        return "redirect:/files/showFiles/" + documentId + "/" + URLEncoder.encode(decimalNumber, StandardCharsets.UTF_8);
    }

    @GetMapping("/showFile/{documentId}/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("documentId") int documentId,
                                                 @PathVariable("id") int id) {
        // Load file from database
        DbFile dbFile = dbFileService.findById(documentId, id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getFileType()))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
                .body(new ByteArrayResource(dbFile.getData()));
    }

}
