package com.example.arduinobanio.recyclerView;

public class BanioItem {
    private String name;
    private String status;
    private String photo;

    public BanioItem(String name, String status, String photo) {
        this.name = name;
        this.status = status;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
