package com.example.file_upload_and_download.services;


import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${finalDestination}")
    private String destination;


    public String upload(MultipartFile file) throws IOException {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String newFileName = UUID.randomUUID().toString();
        String completeFileName = newFileName +  "." + extension;

        File destinationFolder = new File(destination);
        if (!destinationFolder.exists()) throw new IOException("Folder does not exists");
        if (!destinationFolder.isDirectory()) throw new IOException("Folder does not directory");

        File finalDestination = new File(destinationFolder + "\\" + completeFileName);
        if (finalDestination.exists()) throw new IOException("Conflict!");

        file.transferTo(finalDestination);
        return "The file: " + completeFileName + " was successfully sent to the folder with the following path: " + destinationFolder;
    }

    public byte[] download(String fileName) throws IOException {
        File fileFromRepository = new File(destination + "\\" + fileName);
        if (!fileFromRepository.exists()) throw new IOException("File does not exists!");
        return IOUtils.toByteArray(new FileInputStream(fileFromRepository));
    }
}
