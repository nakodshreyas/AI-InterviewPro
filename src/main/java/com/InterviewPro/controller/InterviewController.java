package com.InterviewPro.controller;

import com.InterviewPro.model.InterviewSession;
import com.InterviewPro.service.AiService;
import com.InterviewPro.service.ResumeService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/interview")
public class InterviewController {

    private final ResumeService resumeService;
    private final AiService aiService;

    private final Map<String, InterviewSession> sessions = new ConcurrentHashMap<>();

    public InterviewController(ResumeService resumeService, AiService aiService){
        this.resumeService = resumeService;
        this.aiService = aiService;
    }

//    @PostMapping("/upload")
//    public String uploadResume(@RequestBody String resumeText){
//        return aiService.generateQuestion(resumeText);
//    }

    @PostMapping("/upload")
    public String uploadResume(@RequestParam("file")MultipartFile file) throws IOException {
        String text = resumeService.extractDocuments(file);
        var question = aiService.generateQuestions(text);

        InterviewSession session = new InterviewSession();
        session.getQuestions().addAll(question);

        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, session);

        return sessionId;
    }

    @GetMapping("/next/{sessionId}")
    public String nextQuestion(@PathVariable String sessionId) {

        InterviewSession session = sessions.get(sessionId);

        if (session == null) return "Invalid session";

        int index = session.getCurrentIndex();

        if (index >= session.getQuestions().size()) {
            return "Interview completed. Call /report";
        }

        return session.getQuestions().get(index);
    }

    @PostMapping("/answer/{sessionId}")
    public String submitAnswer(@PathVariable String sessionId,
                               @RequestBody String answer) {

        InterviewSession session = sessions.get(sessionId);

        if (session == null) return "Invalid session";

        session.getAnswers().add(answer);
        session.incrementIndex();

        return "Answer recorded";
    }

    @GetMapping("/report/{sessionId}")
    public String getReport(@PathVariable String sessionId) {

        InterviewSession session = sessions.get(sessionId);

        if (session == null) return "Invalid session";

        return aiService.generateFeedback(
                session.getQuestions(),
                session.getAnswers()
        );
    }
}
