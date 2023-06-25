package com.bStudio.petcare;

public class ProjectModel {

    private String name, About, PublishDate, contactNumber, pettype,gender,age,size,breed;
    private String address, latitude, longitude, district;
    private String image1, image2;

    public ProjectModel() {
    }

    public ProjectModel(String name, String about, String publishDate, String contactNumber, String pettype, String image1, String image2, String gender, String age, String size, String breed, String address, String latitude, String longitude, String district) {
        this.name = name;
        About = about;
        PublishDate = publishDate;
        this.gender = gender;
        this.age = age;
        this.size = size;
        this.breed = breed;
        this.contactNumber = contactNumber;
        this.pettype = pettype;
        this.image1 = image1;
        this.image2 = image2;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.district = district;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbout() {
        return About;
    }

    public void setAbout(String about) {
        About = about;
    }

    public String getPublishDate() {
        return PublishDate;
    }

    public void setPublishDate(String publishDate) {
        PublishDate = publishDate;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getPettype() {
        return pettype;
    }

    public void setPettype(String pettype) {
        this.pettype = pettype;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude(String latitude) {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude(String longitude) {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}