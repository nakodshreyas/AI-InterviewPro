package com.InterviewPro.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class AiService {

    private final ChatClient chatClient;

    public AiService(ChatClient chatClient){
        this.chatClient = chatClient;
    }

//    public String generateQuestion(String resumeText){
//        return """
//        1) Tell me about yourself and your background?
//        2) What are your strongest technical skills?
//        3) Explain your final year project in simple terms.
//        4) What challenges did you face in your project?
//        5) Where do you see yourself in 5 years?
//        """;
//    }

    public List<String> generateQuestions(String resumeText){
        String prompt = """
            Based on this resume, generate exactly 6 interview questions.
            Number them from 1 to 6.

            Resume:
            %s
            """.formatted(resumeText);

        String response =  chatClient.prompt()
                .system("You are an AI interview assistant.")
                .user(prompt)
                .call()
                .content();

        return Arrays.stream(response.split("\n"))
                .filter(s -> s.matches("^[1-6].*"))
                .toList();
    }

    public String generateFeedback(List<String> questions, List<String> answers){
        StringBuilder prompt = new StringBuilder("Review the interview and give feedback.\n\n");

        for(int i = 0; i < questions.size(); i++){
            prompt.append("Q").append(i+1).append(": ").append(questions.get(i)).append("\n");
            prompt.append("Ans: ").append(answers.get(i)).append("\n\n");
        }

        return chatClient.prompt()
                .system("You are an expert interview evaluator.")
                .user(prompt.toString())
                .call()
                .content();
    }
}
