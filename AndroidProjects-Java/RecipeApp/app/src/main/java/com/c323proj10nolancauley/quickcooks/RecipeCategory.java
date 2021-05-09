package com.c323proj10nolancauley.quickcooks;

public class RecipeCategory {
    //variables that we will get and set
    String id, name, imagePath;
    public RecipeCategory(String catId, String title, String catImgLink) {
        id = catId;
        name = title;
        imagePath = catImgLink;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
