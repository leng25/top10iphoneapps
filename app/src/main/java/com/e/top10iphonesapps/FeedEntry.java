package com.e.top10iphonesapps;

public class FeedEntry {

    private String name;
    private String artist;
    private String realeasedate;
    private String summary;
    private String imageUrl;


    @Override
    public String toString() {
        return "name=" + name + '\n' +
                ", artist=" + artist + '\n' +
                ", realeasedate=" + realeasedate + '\n' +
                ", imageUrl=" + imageUrl + '\n' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getRealeasedate() {
        return realeasedate;
    }

    public void setRealeasedate(String realeasedate) {
        this.realeasedate = realeasedate;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
