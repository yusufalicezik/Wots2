package com.yusufalicezik.wots.Model;

public class Posts {
    String user_id;
    String post_id;
    Long yuklenme_tarih;
    String aciklama;
    String photo_url;
    String post_yukleyen_profil_resmi;
    String post_yukleyen_isim_soyisim;
    Long post_begeni_sayisi;


    public Posts(String user_id, String post_id, Long yuklenme_tarih, String aciklama, String photo_url, String post_yukleyen_profil_resmi, String post_yukleyen_isim_soyisim, Long post_begeni_sayisi) {
        this.user_id = user_id;
        this.post_id = post_id;
        this.yuklenme_tarih = yuklenme_tarih;
        this.aciklama = aciklama;
        this.photo_url = photo_url;
        this.post_yukleyen_profil_resmi = post_yukleyen_profil_resmi;
        this.post_yukleyen_isim_soyisim = post_yukleyen_isim_soyisim;
        this.post_begeni_sayisi = post_begeni_sayisi;
    }

    public String getPost_yukleyen_profil_resmi() {
        return post_yukleyen_profil_resmi;
    }

    public void setPost_yukleyen_profil_resmi(String post_yukleyen_profil_resmi) {
        this.post_yukleyen_profil_resmi = post_yukleyen_profil_resmi;
    }

    public String getPost_yukleyen_isim_soyisim() {
        return post_yukleyen_isim_soyisim;
    }

    public void setPost_yukleyen_isim_soyisim(String post_yukleyen_isim_soyisim) {
        this.post_yukleyen_isim_soyisim = post_yukleyen_isim_soyisim;
    }

    public Long getPost_begeni_sayisi() {
        return post_begeni_sayisi;
    }

    public void setPost_begeni_sayisi(Long post_begeni_sayisi) {
        this.post_begeni_sayisi = post_begeni_sayisi;
    }

    public Posts()
    {}

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public Long getYuklenme_tarih() {
        return yuklenme_tarih;
    }

    public void setYuklenme_tarih(Long yuklenme_tarih) {
        this.yuklenme_tarih = yuklenme_tarih;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }
}
