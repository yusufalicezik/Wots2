package com.yusufalicezik.wots.Profile;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yusufalicezik.wots.Login.AcilisActivity;
import com.yusufalicezik.wots.Model.Posts;
import com.yusufalicezik.wots.Model.Users;
import com.yusufalicezik.wots.R;
import com.yusufalicezik.wots.Utils.EventBusDataEvents;
import com.yusufalicezik.wots.Utils.UniversalImagLoader;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileEditActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private EditText isimSoyisim;
    private EditText bio;
    private EditText sifre;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;

    CircleImageView profilResim;

    Users kullanici;

    private int PICK_IMAGE_REQUEST = 1;


    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference mRef;
    private StorageReference mStorageRef;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);


        mAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        mRef=database.getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        profilResim=findViewById(R.id.profDuzenleFoto);
        bio=findViewById(R.id.profilDuzenleBio);
        isimSoyisim=findViewById(R.id.profilDuzenleIsimSoyisim);
        sifre=findViewById(R.id.profilDuzenleSifre);
        progressBar=findViewById(R.id.profileDuzenleProgress);


        progressDialog=new ProgressDialog(this);

        mToolbar=findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        UniversalInit();





    }


    private void kullaniciVerileriniGetir() {


        if(kullanici!=null) {

            //Toast.makeText(this, "ddd"+kullanici.getIsim_soyisim(), Toast.LENGTH_SHORT).show();
            bio.setText(kullanici.getUser_details().getBiography());
            isimSoyisim.setText(kullanici.getIsim_soyisim());
            sifre.setText(kullanici.getPassword());
            UniversalImagLoader.setImage(kullanici.getUser_details().getProfile_picture(), profilResim, progressBar, "");
        }
        else {
            Intent intent=new Intent(this,ProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
        }

    }



    public void yeniVerileriKaydet(View view)
    {
        verileriYaz();

    }

    private void verileriYaz() {
        kullanici.setIsim_soyisim(isimSoyisim.getText().toString());
        kullanici.setPassword(sifre.getText().toString());
        kullanici.getUser_details().setBiography(bio.getText().toString());



        mRef.child("users").child(mAuth.getCurrentUser().getUid().toString()).setValue(kullanici).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {   bagliTümPostlardakiFotolariDegistir();
                    Toast.makeText(ProfileEditActivity.this, "Bilgiler başarıyla kaydedildi", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),ProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }


    public void izinIste(View view)
    {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                        //eğer izin verilmişse..
                        //galeriyiAc();
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Fotograf Sec"),PICK_IMAGE_REQUEST);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(ProfileEditActivity.this, "Lütfen ayarlardan manuel olarak izin verin.", Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                        Toast.makeText(ProfileEditActivity.this, "Lütfen ayarlardan manuel olarak izin verin.", Toast.LENGTH_SHORT).show();





                    }

                }).check();
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data.getData()!=null)
        {
            Uri profileUri=data.getData();

            //

            progressDialog.setTitle("Fotoğraf yükleniyor");
            progressDialog.setMessage("Fotoğraf boyutuna göre bu işlem biraz zaman alabilir, lütfen bekleyin");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mStorageRef.child("users").child(mAuth.getCurrentUser().getUid()).child(profileUri.getLastPathSegment()).putFile(profileUri)
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if(task.isSuccessful())
                                {

                                    kullanici.getUser_details().setProfile_picture(task.getResult().getDownloadUrl().toString());

                                  //baglı oldugu tüm postları bul.
                                    //
                                    //
                                    //
                                    // DatabaseReference mref2=FirebaseDatabase.getInstance().getReference().child("");

                                    bagliTümPostlardakiFotolariDegistir();

                                    UniversalImagLoader.setImage(kullanici.getUser_details().getProfile_picture(),profilResim,progressBar,"");

                                    progressDialog.cancel();
                                }
                            }
                        });





            //


        }
    }

    private void bagliTümPostlardakiFotolariDegistir() {

        final DatabaseReference mref2=FirebaseDatabase.getInstance().getReference().child("posts");


        mref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null) {
                    for (DataSnapshot user : dataSnapshot.getChildren()) {
                        Posts degisecekPost = user.getValue(Posts.class);
                        if (degisecekPost.getUser_id().equals(mAuth.getCurrentUser().getUid())) {

                        degisecekPost.setPost_yukleyen_profil_resmi(kullanici.getUser_details().getProfile_picture());
                        degisecekPost.setPost_yukleyen_isim_soyisim(kullanici.getIsim_soyisim());

                        mref2.child(degisecekPost.getPost_id()).setValue(degisecekPost);



                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void UniversalInit() {
        UniversalImagLoader universalImageLoader = new UniversalImagLoader(getApplicationContext());
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

        kullaniciVerileriniGetir(); //bilerek burda çağırdım. önce register edip sonra verileri alabiliyoruz. null pointer yememek için.

    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe (sticky = true)
    public void onKisiEvent(EventBusDataEvents.kullaniciBilgileriGonder event)
    {
        kullanici=event.getKullanici();
        Log.e("ss",kullanici.getIsim_soyisim());

    }

    public void cikisYapTik(View view)
    {
        final AlertDialog.Builder alert=new AlertDialog.Builder(this)
                .setTitle("Çıkış Yapılacak")
                .setCancelable(false)
                .setMessage("Bu hesaptan çıkış yapmak istiyor musunuz?");

        alert.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth.signOut();
                Intent intent=new Intent(getApplicationContext(), AcilisActivity.class);
                startActivity(intent);
                finish();
            }
        });
        alert.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();

    }

}
