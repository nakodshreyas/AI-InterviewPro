package com.InterviewPro.model;

import java.util.ArrayList;
import java.util.List;

public class InterviewSession {
    private List<String> questions = new ArrayList<>();
    private List<String> answers = new ArrayList<>();
    private int currentIndex = 0;

    public List<String> getQuestions(){
        return questions;
    }

    public List<String> getAnswers(){
        return answers;
    }

    public int getCurrentIndex(){
        return currentIndex;
    }

    public void incrementIndex() {
        this.currentIndex++;
    }
}
