package com.tapan.grocydelivery.models;

public class VisionModel {

    private String imageVision, textVisionHead, textVisionData;

    public VisionModel(String imageVision, String textVisionHead, String textVisionData) {
        this.imageVision = imageVision;
        this.textVisionHead = textVisionHead;
        this.textVisionData = textVisionData;
    }

    public String getImageVision() {
        return imageVision;
    }

    public String getTextVisionHead() {
        return textVisionHead;
    }

    public String getTextVisionData() {
        return textVisionData;
    }
}
