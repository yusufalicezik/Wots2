package com.yusufalicezik.wots.Model;

public class UserDetails {

    private String follower;
    private String following;
    private String post;
    private String profile_picture;
    private String biography;

    public UserDetails(String follower, String following, String post, String profile_picture, String biography) {
        this.follower = follower;
        this.following = following;
        this.post = post;
        this.profile_picture = profile_picture;
        this.biography = biography;
    }

    public UserDetails() {
    }

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }
}
