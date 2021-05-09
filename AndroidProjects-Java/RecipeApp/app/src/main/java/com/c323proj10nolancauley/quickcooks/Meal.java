package com.c323proj10nolancauley.quickcooks;

public class Meal {
    String id, name, path;
    public Meal(String mealID, String mealName, String mealIconLink) {
        id = mealID;
        name = mealName;
        path = mealIconLink;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
