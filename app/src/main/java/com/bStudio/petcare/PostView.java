package com.bStudio.petcare;

public class PostView {
    private String pet_name;
    private String pet_image;

    public PostView(String pet_name, String pet_image) {
        this.pet_name = pet_name;
        this.pet_image = pet_image;
    }

    public PostView() {
    }

    public String getPet_name() {
        return pet_name;
    }

    public void setPet_name(String pet_name) {
        this.pet_name = pet_name;
    }

    public String getPet_image() {
        return pet_image;
    }

    public void setPet_image(String pet_image) {
        this.pet_image = pet_image;
    }
}
