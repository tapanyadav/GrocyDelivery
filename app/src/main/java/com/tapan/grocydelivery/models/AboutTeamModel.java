package com.tapan.grocydelivery.models;

public class AboutTeamModel {

    private String imageTeam, nameTeam, teamDesignation, imageBack;

    public AboutTeamModel(String imageTeam, String nameTeam, String teamDesignation, String imageBack) {
        this.imageTeam = imageTeam;
        this.nameTeam = nameTeam;
        this.teamDesignation = teamDesignation;
        this.imageBack = imageBack;
    }

    public String getImageTeam() {
        return imageTeam;
    }

    public String getNameTeam() {
        return nameTeam;
    }

    public String getTeamDesignation() {
        return teamDesignation;
    }

    public String getImageBack() {
        return imageBack;
    }
}
