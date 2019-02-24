package com.yusufalicezik.wots.Model;

public class Messages {

    private String icerik;
    private String from;
    private Long tarih;

    public Messages(String icerik, String from, Long tarih) {
        this.icerik = icerik;
        this.from = from;
        this.tarih = tarih;
    }

    public Messages() {
    }

    public String getIcerik() {
        return icerik;
    }

    public void setIcerik(String icerik) {
        this.icerik = icerik;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Long getTarih() {
        return tarih;
    }

    public void setTarih(Long tarih) {
        this.tarih = tarih;
    }
}
