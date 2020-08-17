package com.tapan.grocydelivery.models;

public class FaqModel {

    private String faqQuestion, faqAnswer;

    public FaqModel() {
    }

    public FaqModel(String faqQuestion, String faqAnswer) {
        this.faqQuestion = faqQuestion;
        this.faqAnswer = faqAnswer;
    }

    public String getFaqQuestion() {
        return faqQuestion;
    }

    public String getFaqAnswer() {
        return faqAnswer;
    }
}
