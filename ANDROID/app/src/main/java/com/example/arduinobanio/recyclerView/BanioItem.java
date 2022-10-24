package com.example.arduinobanio.recyclerView;

public class BanioItem {
    private final String name;
    private final String status;

    public BanioItem(String name, String status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }
}
