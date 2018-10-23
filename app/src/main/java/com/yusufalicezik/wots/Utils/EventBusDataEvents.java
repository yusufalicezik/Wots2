package com.yusufalicezik.wots.Utils;

import com.yusufalicezik.wots.Model.Posts;
import com.yusufalicezik.wots.Model.Users;

public class EventBusDataEvents {

   public static class kullaniciBilgileriGonder{
       private Users kullanici;


       public Users getKullanici() {
           return kullanici;
       }

       public void setKullanici(Users kullanici) {
           this.kullanici = kullanici;
       }

        public kullaniciBilgileriGonder(Users kayitOlanKullanici) {
           this.kullanici=kayitOlanKullanici;
       }
   }


    public static class postBilgileriGonder{
        private Posts post;

        public postBilgileriGonder(Posts post) {
            this.post=post;
        }

        public Posts getPost() {
            return post;
        }

        public void setPost(Posts post) {
            this.post = post;
        }
    }

}
