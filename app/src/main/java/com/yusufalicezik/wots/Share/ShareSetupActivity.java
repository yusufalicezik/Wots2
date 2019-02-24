package com.yusufalicezik.wots.Share;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yusufalicezik.wots.Home.MainActivity;
import com.yusufalicezik.wots.Model.Posts;
import com.yusufalicezik.wots.Model.Users;
import com.yusufalicezik.wots.R;
import com.yusufalicezik.wots.Utils.EventBusDataEvents;
import com.yusufalicezik.wots.Utils.UniversalImagLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShareSetupActivity extends AppCompatActivity {

    private Posts post;

    private CircleImageView setupResim;
    EditText postAciklama;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference mRef;
    private StorageReference mStorageRef;


    private Users postuYukleyenKullanici;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_setup);

        mAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        mRef=database.getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        setupResim=findViewById(R.id.yuklenenResimSetup);
        postAciklama=findViewById(R.id.yuklenenResimTextSetup);

        UniversalInit();



        yukleyenKullaniciBilgileriniGetir();
    }

    private void yukleyenKullaniciBilgileriniGetir() {
        mRef.child("users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null)
                {
                     postuYukleyenKullanici=dataSnapshot.getValue(Users.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void verileriYazPostuPaylas(View view)
    {
        post.setAciklama(postAciklama.getText().toString());
        mRef.child("posts").child(post.getPost_id()).setValue(post);
        mRef.child("posts").child(post.getPost_id()).child("yuklenme_tarih").setValue(ServerValue.TIMESTAMP);
        mRef.child("posts").child(post.getPost_id()).child("post_yukleyen_profil_resmi").setValue(postuYukleyenKullanici.getUser_details().getProfile_picture());
        mRef.child("posts").child(post.getPost_id()).child("post_yukleyen_isim_soyisim").setValue(postuYukleyenKullanici.getIsim_soyisim());


        Toast.makeText(this, "Post paylaşıldı", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

        kullaniciVerileriniGetir(); //bilerek burda çağırdım. önce register edip sonra verileri alabiliyoruz. null pointer yememek için.

    }

    private void kullaniciVerileriniGetir() {


        UniversalImagLoader.setImage(post.getPhoto_url(),setupResim,null,"");



    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true)
    public void onPostEvent(EventBusDataEvents.postBilgileriGonder event)
    {
        post=event.getPost();


    }

    private void UniversalInit() {
        UniversalImagLoader universalImageLoader = new UniversalImagLoader(getApplicationContext());
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();

    }
}
