package com.yusufalicezik.wots.Model;

public class FindFriends {
    public String isim_soyisim;

    public UserDetails user_details;

    public FindFriends()

    {

    }

    public FindFriends(String isim_soyisim, UserDetails user_details) {
        this.isim_soyisim = isim_soyisim;
        this.user_details = user_details;
    }

    public String getIsim_soyisim() {
        return isim_soyisim;
    }

    public void setIsim_soyisim(String isim_soyisim) {
        this.isim_soyisim = isim_soyisim;
    }

    public UserDetails getUser_details() {
        return user_details;
    }

    public void setUser_details(UserDetails user_details) {
        this.user_details = user_details;
    }
}
