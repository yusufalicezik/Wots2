package com.yusufalicezik.wots.Login;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.yusufalicezik.wots.Home.MainActivity;
import com.yusufalicezik.wots.R;

public class AcilisActivity extends AppCompatActivity {

    private ImageButton girisYapButon;
    private EditText eMail;
    private EditText sifre;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acilis);


        eMail=findViewById(R.id.etEmail);
        sifre=findViewById(R.id.etSifre1);
        progressBar=findViewById(R.id.progressBarGiris);
        progressBar.setVisibility(View.INVISIBLE);
        girisYapButon=findViewById(R.id.imageButton);

        mAuth = FirebaseAuth.getInstance();

       // mAuth.signOut();

        setupAuthListener();

      /*  if(mAuth.getCurrentUser()!=null)
        {

            mainActvityeGonder();
        }
        */
        fullScreenYap();





    }

    private void setupAuthListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    mainActvityeGonder();

                }
                else {

                }

            }
        };
    }

    private void mainActvityeGonder() {
    Intent intent=new Intent(getApplicationContext(), MainActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    startActivity(intent);
    finish();
    }


    public void kayitOlTik(View view)
    {
        Intent intent=new Intent(this,KayitOlActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        finish();
    }

    private void fullScreenYap()
    {
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

    }

    public void girisYapTik(View view)
    {
        girisYapButon.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);


        if(eMail.getText().toString().isEmpty() || sifre.getText().toString().isEmpty())
        {
            FancyToast.makeText(getApplicationContext(),"Tüm alanları dolduranız gerekiyor", Toast.LENGTH_SHORT,FancyToast.INFO,false).show();
            progressBar.setVisibility(View.INVISIBLE);
            girisYapButon.setVisibility(View.VISIBLE);

        }
        else
        {
            mAuth.signInWithEmailAndPassword(eMail.getText().toString(),sifre.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                progressBar.setVisibility(View.INVISIBLE);
                               // girisYapButon.setVisibility(View.VISIBLE);
                                mainActvityeGonder();
                            }
                            else
                            {
                                FancyToast.makeText(getApplicationContext(),"E-posta veya şifre yanlış", Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                                progressBar.setVisibility(View.INVISIBLE);
                                girisYapButon.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener (mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener!=null)
        {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();



                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(a);
                finish();



    }


}
