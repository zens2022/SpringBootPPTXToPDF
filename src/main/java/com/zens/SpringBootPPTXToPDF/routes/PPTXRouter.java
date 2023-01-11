package com.zens.SpringBootPPTXToPDF.routes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zens.SpringBootPPTXToPDF.controllers.PPTXController;

@RequestMapping("pptx")
@RestController
@SpringBootApplication
public class PPTXRouter {

    @Autowired
    private PPTXController pptxController;

    @PostMapping("toPDF")
    public ResponseEntity<byte[]> toPDF(
        @RequestParam("file") MultipartFile file
    ) {
        return pptxController.toPDF(file);
    }

}
