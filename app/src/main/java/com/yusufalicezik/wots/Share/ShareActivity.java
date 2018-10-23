package com.yusufalicezik.wots.Share;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yusufalicezik.wots.Model.Posts;
import com.yusufalicezik.wots.Profile.ProfileEditActivity;
import com.yusufalicezik.wots.R;
import com.yusufalicezik.wots.Utils.BottomNavigationViewHelper;
import com.yusufalicezik.wots.Utils.EventBusDataEvents;
import com.yusufalicezik.wots.Utils.UniversalImagLoader;

import org.greenrobot.eventbus.EventBus;

public class ShareActivity extends AppCompatActivity {
    private int ACTIVITY_NO=2;
    private int PICK_IMAGE_REQUEST=2;
    private ProgressDialog progressDialog;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference mRef;
    private StorageReference mStorageRef;

    private ImageView secilenResim;

    Posts post=new Posts();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);


        mAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        mRef=database.getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        secilenResim=findViewById(R.id.searchImageView);

        progressDialog=new ProgressDialog(this);
        progressBar=findViewById(R.id.shareProgress);

        BottomMenuyuAyarla();
        fullScreenYap();
        UniversalInit();
        izinIste();


    }

    private void BottomMenuyuAyarla() {


        BottomNavigationViewEx bottomNavigationViewEx=findViewById(R.id.bottomNavigationView);

        BottomNavigationViewHelper bottomNavigationViewHepler=new BottomNavigationViewHelper(bottomNavigationViewEx);
        bottomNavigationViewHepler.setupNavigation(getApplicationContext(),bottomNavigationViewEx);

        Menu menu=bottomNavigationViewEx.getMenu();
        MenuItem menuItem=menu.getItem(ACTIVITY_NO);
        menuItem.setChecked(true);


    }


    private void fullScreenYap()
    {
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

    }

    public void izinIste()
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
                        Toast.makeText(ShareActivity.this, "Lütfen ayarlardan manuel olarak izin verin.", Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                        Toast.makeText(ShareActivity.this, "Lütfen ayarlardan manuel olarak izin verin.", Toast.LENGTH_SHORT).show();





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


                                String postID=mRef.child("posts").push().getKey();



                                post.setUser_id(mAuth.getCurrentUser().getUid());
                               post.setPhoto_url(task.getResult().getDownloadUrl().toString());
                                post.setPost_id(postID);

                                int sayi=0;
                                post.setPost_begeni_sayisi(Long.valueOf(sayi));



                                EventBus.getDefault().postSticky(new EventBusDataEvents.postBilgileriGonder(post));



                               // kullanici.getUser_details().setBiography();

                              UniversalImagLoader.setImage(task.getResult().getDownloadUrl().toString(),secilenResim,progressBar,"");

                                progressDialog.cancel();

                                Intent intent=new Intent(getApplicationContext(),ShareSetupActivity.class);
                                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                                startActivity(intent);

                            }
                        }
                    });





            //


        }
    }


    private void UniversalInit() {
        UniversalImagLoader universalImageLoader = new UniversalImagLoader(getApplicationContext());
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    @Override
    protected void onResume() {
        BottomMenuyuAyarla();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //burda alert dialog ile sorulur, çıkmak istiyor musunuz? belki..
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
