package com.igar15.filecabinet.controller;

import com.igar15.filecabinet.entity.ElectronicImageChangeNotice;
import com.igar15.filecabinet.entity.ElectronicImageDocument;
import com.igar15.filecabinet.service.ElectronicImageChangeNoticeService;
import com.igar15.filecabinet.util.exception.ChangeNumberDuplicateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Controller
@RequestMapping("/electronicImageChangeNotices")
public class ElectronicImageChangeNoticeController {

    @Autowired
    private ElectronicImageChangeNoticeService electronicImageChangeNoticeService;

    @PostMapping("/save/{changeNoticeId}")
    public String save(@PathVariable("changeNoticeId") int changeNoticeId,
                       @RequestParam("file") MultipartFile file,
                       RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("addFileError", "File should not be empty!");
            return "redirect:/changenotices/showChangeNoticeInfo/" + changeNoticeId;
        }
        else if (!file.getContentType().endsWith("/pdf")) {
            redirectAttributes.addFlashAttribute("addFileError", "File should have pdf type!");
            return "redirect:/changenotices/showChangeNoticeInfo/" + changeNoticeId;
        }
        electronicImageChangeNoticeService.create(file, changeNoticeId);
        return "redirect:/changenotices/showChangeNoticeInfo/" + changeNoticeId;
    }

    @GetMapping("/delete/{changeNoticeId}/{id}")
    public String delete(@PathVariable("changeNoticeId") int changeNoticeId,
                         @PathVariable("id") int id) {
        electronicImageChangeNoticeService.delete(changeNoticeId, id);
        return "redirect:/changenotices/showChangeNoticeInfo/" + changeNoticeId;
    }

    @GetMapping("/showFile/{changeNoticeId}")
    public void downloadFile(@PathVariable("changeNoticeId") int changeNoticeId,
                             HttpServletResponse response) throws IOException {
        // Load file from database
        ElectronicImageChangeNotice electronicImageChangeNotice = electronicImageChangeNoticeService.findByChangeNoticeIdWithElectronicImageData(changeNoticeId);

        response.setContentType("application/pdf");

        InputStream inputStream = new ByteArrayInputStream(electronicImageChangeNotice.getElectronicImageData().getData());
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(inputStream.readAllBytes());
    }



}
