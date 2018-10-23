package com.yusufalicezik.wots.Utils;


    public class TimeAgo {
        private static final int SECOND_MILLIS = 1000;
        private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


        public static String getTimeAgo(long time) {
            if (time < 1000000000000L) {
                // if timestamp given in seconds, convert to millis
                time *= 1000;
            }

            long now = System.currentTimeMillis();
            if (time > now || time <= 0) {
                return "Şimdi";
            }

            // TODO: localize
            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "Az önce";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "1 dakika önce";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " dakika önce";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "1 saat önce";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " saat önce";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "dün";
            } else {
                return diff / DAY_MILLIS + " gün önce";
            } //if eklenebilri ek olarak. mesea diff/daymilis>30 dan yani 30 günden sonrasını normal tarih olarak göstersin işte 01.01.2016 gbi.
        }
    }

