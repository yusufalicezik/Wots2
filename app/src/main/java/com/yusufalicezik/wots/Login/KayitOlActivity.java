package com.yusufalicezik.wots.Login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.shashank.sony.fancytoastlib.FancyToast;
import com.yusufalicezik.wots.Model.Users;
import com.yusufalicezik.wots.R;
import com.yusufalicezik.wots.Utils.EventBusDataEvents;

import org.greenrobot.eventbus.EventBus;

public class KayitOlActivity extends AppCompatActivity {

    private Users kayitOlanKullanici;
    EditText email;
    EditText sifre;
    EditText sifreConfirm;
    EditText telefon;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_kayit_ol);


         email=findViewById(R.id.etEmailKayit);
         sifre=findViewById(R.id.etSifre1Kayit);
         sifreConfirm=findViewById(R.id.etSifreConfirm2);
         telefon=findViewById(R.id.etTelefon);
         btn=findViewById(R.id.ileriButton);


        TextWatcher watcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.length()>=6)
                {
                    btn.setEnabled(true);
                }
                else
                {
                    btn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

      sifre.addTextChangedListener(watcher);
        sifreConfirm.addTextChangedListener(watcher);



        btn.setEnabled(true);
        fullScreenYap();
        kayitOlanKullanici=new Users();







    }


    private void fullScreenYap()
    {
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);


    }

    public void ileriButtonTik(View view)
    {

        boolean verilerDogruMu=girilenleriKontrolEt();
        if(verilerDogruMu) {
            Intent intent = new Intent(this, KayitOlSetupActivity.class);

            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

    }

    private boolean girilenleriKontrolEt() {



        if(email.getText().toString().isEmpty()|| sifre.getText().toString().isEmpty() ||
                sifreConfirm.getText().toString().isEmpty() || telefon.getText().toString().isEmpty())
        {

            FancyToast.makeText(this,"Tüm alanların doldurulması gerekiyor",FancyToast.LENGTH_LONG,FancyToast.INFO,false).show();
            return false;
        }

        if(!sifre.getText().toString().equals(sifreConfirm.getText().toString()))
        {
            FancyToast.makeText(this,"Şifreler eşleşmiyor",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
            return false;
        }

       if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            FancyToast.makeText(this,"Lütfen e mail formatını doğru girdiğinizden emin olun",FancyToast.LENGTH_SHORT,FancyToast.WARNING,false).show();
            return false;
        }


        if(!Patterns.PHONE.matcher(telefon.getText().toString()).matches()){
            FancyToast.makeText(this,"Lütfen telefon formatını doğru girdiğinizden emin olun",FancyToast.LENGTH_SHORT,FancyToast.WARNING,false).show();
            return false;
        }


        kayitOlanKullanici.setEmail(email.getText().toString());
        kayitOlanKullanici.setPassword(sifre.getText().toString());
        kayitOlanKullanici.setPhone_number(telefon.getText().toString());

        EventBus.getDefault().postSticky(new EventBusDataEvents.kullaniciBilgileriGonder(kayitOlanKullanici));

        return true;
    }

    public void geriButonTik(View view)
    {
        Intent intent=new Intent(this,AcilisActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }


}
