package org.prashant.blog.blogapplicationapi.serviceimpl;

import org.prashant.blog.blogapplicationapi.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadFile(String path, MultipartFile file) throws IOException {
        String fileName = "img_"+UUID.randomUUID().toString()+ file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
        String filePath = path + File.separator + fileName;
        File f = new File(path);
        if(!f.exists()){
            f.mkdir();
        }
        Files.copy(file.getInputStream(), Paths.get(filePath));
        return fileName;
    }

    @Override
    public InputStream getResourceFile(String path, String fileName) throws FileNotFoundException {
        String filePath = path + File.separator + fileName;
        return new FileInputStream(filePath);
    }

    @Override
    public void deleteResource(String path, String fileName) throws IOException {
        String filePath = path + File.separator + fileName;
        Files.deleteIfExists(Paths.get(filePath));
    }
}
