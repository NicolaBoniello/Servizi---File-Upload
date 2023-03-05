package com.example.file_upload_and_download.controllers;

import com.example.file_upload_and_download.services.FileStorageService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    FileStorageService fileStorageService;

    /**
     *SINGLE UPLOAD
     */
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws Exception{
        return fileStorageService.upload(file);
    }

    /**
     *MULTIPLE UPLOAD
     */

    @PostMapping("/upload/multiple")
    public List<String> multipleUpload(@RequestParam MultipartFile[] files) throws Exception{
        List<String> filesnames = new ArrayList<>();
        for (MultipartFile file:files
        ) {
            String singleUploadedFileName = fileStorageService.upload(file);
            filesnames.add(singleUploadedFileName);
        }
        return filesnames;
    }

    /**
     * DOWNLOAD
     */
    @GetMapping("/download")
    public byte[] download(@RequestParam String fileName, HttpServletResponse response)throws IOException{
        System.out.println("Downloading: " + fileName);
        String extensions = FilenameUtils.getExtension(fileName);

        switch (extensions){

            case "gif":
                response.setContentType(MediaType.IMAGE_GIF_VALUE);
                break;
            case "png":
                response.setContentType(MediaType.IMAGE_PNG_VALUE);
                break;
            case "jpeg":
            case "jpg":
                response.setContentType(MediaType.IMAGE_JPEG_VALUE);
                break;
        }

        response.setHeader("Content-Disposition", "attachment; filename=\""+fileName+"\"");
        return fileStorageService.download(fileName);
    }


}
