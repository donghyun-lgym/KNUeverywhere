package com.dongcompany.knueverywhere.ui.MapInfo;

public class picInfo {
    private String id;
    private String date;
    private String name;
    private Boolean upload;
    public picInfo(String id, String date, Boolean upload, String name) {
        this.id = id;
        this.date = date;
        this.upload = upload;
        this.name = name;
    }
    String getId() {
        return this.id;
    }
    String getDate() {
        return date;
    }
    String getName() {
        return name;
    }
}
