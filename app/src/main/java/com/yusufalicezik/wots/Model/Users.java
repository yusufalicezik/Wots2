package com.yusufalicezik.wots.Model;

public class Users {

    private String isim_soyisim;
    private String email;
    private String phone_number;
    private String user_name;
    private String password;
    private String user_id;
    private UserDetails user_details;

    public Users() {
    }

    public Users(String isim_soyisim, String email, String phone_number, String user_name, String password, String user_id, UserDetails user_details) {
        this.isim_soyisim = isim_soyisim;
        this.email = email;
        this.phone_number = phone_number;
        this.user_name = user_name;
        this.password = password;
        this.user_id = user_id;
        this.user_details = user_details;
    }


    public String getIsim_soyisim() {
        return isim_soyisim;
    }

    public void setIsim_soyisim(String isim_soyisim) {
        this.isim_soyisim = isim_soyisim;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public UserDetails getUser_details() {
        return user_details;
    }

    public void setUser_details(UserDetails user_details) {
        this.user_details = user_details;
    }
}
