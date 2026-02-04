package com.InterviewPro.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ResumeService {

    public String extractDocuments(MultipartFile file) throws IOException {
        ByteArrayResource resource =
                new ByteArrayResource(file.getBytes()){
                    public String getFilename(){
                        return file.getOriginalFilename();
                    }
                };

        TikaDocumentReader reader = new TikaDocumentReader(resource);

        List<Document> docs = reader.get();

        return docs.stream()
                .map(Document::getFormattedContent)
                .collect(Collectors.joining("\n"));

    }

    public String generateSessionId(){
        return UUID.randomUUID().toString();
    }
}
