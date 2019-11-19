package com.example.ifuturusaction.model;





public class feedbackmodel {

private String feedbackdetails;

    public feedbackmodel() {
    }


    public feedbackmodel(String feedbackdetails) {
        this.feedbackdetails = feedbackdetails;
    }

    public String getFeedbackdetails() {
        return feedbackdetails;
    }

    public void setFeedbackdetails(String feedbackdetails) {
        this.feedbackdetails = feedbackdetails;
    }
}