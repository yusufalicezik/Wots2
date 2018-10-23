package com.yusufalicezik.wots.Login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.yusufalicezik.wots.Home.MainActivity;
import com.yusufalicezik.wots.Model.UserDetails;
import com.yusufalicezik.wots.Model.Users;
import com.yusufalicezik.wots.R;
import com.yusufalicezik.wots.Utils.EventBusDataEvents;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;




public class KayitOlSetupActivity extends AppCompatActivity {

    private Users kullanci;

    EditText isim;
    EditText soyisim;
    EditText username;
    EditText biografi;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference mRef;

    boolean varMi=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ol_setup);

        isim=findViewById(R.id.etIsim);
        soyisim=findViewById(R.id.etSoyisim);
        username=findViewById(R.id.etKullaniciAdi);
        biografi=findViewById(R.id.etBiografi);

        mAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        mRef=database.getReference();


        fullScreenYap();

    }

    private void fullScreenYap()
    {
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

    }

    public void geriButonTik(View view)
    {
        Intent intent=new Intent(this,KayitOlActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }


    public void ileriButonTik(View view)
    {
        varMi=false;
        verileriKontrolEt();

    }

    private void verileriKontrolEt() {

        //farklı threadler yüzünden ilk önce kullanıcı adlarını çektim, en son diğer koşulları bu olaylar bttikten sonra yaptım.
        mRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue()!=null)
                    {



                        for (DataSnapshot user:dataSnapshot.getChildren())
                        {
                            Users okunacakKullanici=user.getValue(Users.class);
                            if(okunacakKullanici.getUser_name().equals(username.getText().toString()))
                            {
                                varMi=true;
                                break;

                            }

                        }


                    }

                    if(varMi==true)
                    {
                        //eğer kAdi varsa daha önceden, true dönderir ve kayıt olmaz.
                        FancyToast.makeText(getApplicationContext(),"Bu kullanıcı adı zaten var",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                    }

                    else if(isim.getText().toString().isEmpty() || soyisim.getText().toString().isEmpty() ||
                            username.getText().toString().isEmpty() || biografi.getText().toString().isEmpty())
                    {
                        FancyToast.makeText(getApplicationContext(),"Tüm alanların doldurulması gerekiyor",FancyToast.LENGTH_LONG,FancyToast.INFO,false).show();

                    }
                    else
                    {
                        //herşey doğruysa eğer veritabanına users bilgilerini yazarız. Sonra da intent ile homeactivity e döneriz.
                        veritabaninaBilgileriYaz(); //kullaniciyi kaydederiz bu metodda..
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


    }

    private void veritabaninaBilgileriYaz()
    {

        UserDetails userDetails=new UserDetails();
        userDetails.setBiography(biografi.getText().toString());
        userDetails.setFollower("0");
        userDetails.setFollowing("0");
        userDetails.setPost("0");
        userDetails.setProfile_picture("");
        kullanci.setUser_details(userDetails);
        kullanci.setIsim_soyisim(isim.getText().toString()+" "+soyisim.getText().toString());
        kullanci.setUser_name(username.getText().toString());


        mAuth.createUserWithEmailAndPassword(kullanci.getEmail().toString(),kullanci.getPassword().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {

                            //eğer auth işlemi başarılı ise veritabanına users tablosuna yazalım verileri. Öncesinde uid yi alalım.
                            kullanci.setUser_id(mAuth.getCurrentUser().getUid().toString());

                            mRef.child("users").child(kullanci.getUser_id().toString()).setValue(kullanci)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(), "Veriler yazildi", Toast.LENGTH_SHORT).show();

                                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                                            }
                                            else
                                            {
                                                Toast.makeText(getApplicationContext(), "Veriler yazilamadi", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });




                        }else
                        {
                            Toast.makeText(getApplicationContext(), "Veriler yazilamadi(kullanici kayit olamadi)", Toast.LENGTH_SHORT).show();
                            Log.e("ee",task.getException().getMessage().toString());

                        }
                    }
                });





    }



    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe (sticky = true)
    public void onTelefonNoEvent(EventBusDataEvents.kullaniciBilgileriGonder event)
    {
     kullanci=event.getKullanici();
        Log.e("ss",kullanci.getEmail());

    }
}
