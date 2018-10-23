package com.yusufalicezik.wots.Profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yusufalicezik.wots.Login.AcilisActivity;
import com.yusufalicezik.wots.Model.Posts;
import com.yusufalicezik.wots.Model.Users;
import com.yusufalicezik.wots.R;
import com.yusufalicezik.wots.Utils.BottomNavigationViewHelper;
import com.yusufalicezik.wots.Utils.EventBusDataEvents;
import com.yusufalicezik.wots.Utils.GridImageView;
import com.yusufalicezik.wots.Utils.TimeAgo;
import com.yusufalicezik.wots.Utils.UniversalImagLoader;


import org.greenrobot.eventbus.EventBus;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener mAuthListener;



    private int ACTIVITY_NO = 4;
    private ScrollView scrollView;

    private TextView takipci, takip, bio, isim;
    private CircleImageView profilResim;
    private ProgressBar progressBar;


    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference mRef,likesRef, followerRef, followingRef;;
    Users okunacakKullaniciVerileri;


    //postlar için.

    private RecyclerView postListe;
    private DatabaseReference mRefPost;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();

        mRefPost = FirebaseDatabase.getInstance().getReference().child("posts");
        likesRef= FirebaseDatabase.getInstance().getReference().child("likes");
        followerRef= FirebaseDatabase.getInstance().getReference().child("follower");
        followingRef= FirebaseDatabase.getInstance().getReference().child("following");
        postListe = findViewById(R.id.profilRecListe);
        postListe.setHasFixedSize(true);
        // postListe.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager ll = new LinearLayoutManager(this);
        ll.setReverseLayout(true);
        ll.setStackFromEnd(true);
        postListe.setLayoutManager(ll);


        profilResim = findViewById(R.id.profile_image);
        takip = findViewById(R.id.tvProfileTakip);
        takipci = findViewById(R.id.tvProfileTakipci);
        bio = findViewById(R.id.tvProfileBio);
        isim = findViewById(R.id.tvProfileIsimSoyisim);
        progressBar = findViewById(R.id.profileProgressBar);

        setupAuthListener();
        BottomMenuyuAyarla();
        UniversalInit();



    }

    private void UniversalInit() {
        UniversalImagLoader universalImageLoader = new UniversalImagLoader(getApplicationContext());
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    private void kullaniciVerileriniGetir() {


        mRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    for (DataSnapshot user : dataSnapshot.getChildren()) {
                        okunacakKullaniciVerileri = user.getValue(Users.class);
                        if (okunacakKullaniciVerileri.getUser_id().equals(mAuth.getCurrentUser().getUid())) {

                            takip.setText(okunacakKullaniciVerileri.getUser_details().getFollowing());
                            takipci.setText(okunacakKullaniciVerileri.getUser_details().getFollower());
                            bio.setText(okunacakKullaniciVerileri.getUser_details().getBiography());
                            isim.setText(okunacakKullaniciVerileri.getIsim_soyisim());
                            String imgUrl = okunacakKullaniciVerileri.getUser_details().getProfile_picture();

                            UniversalImagLoader.setImage(imgUrl, profilResim, progressBar, "");

                            ///
                            followerRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                    int takipciSayisi=(int)dataSnapshot.child(mAuth.getCurrentUser().getUid()).getChildrenCount();

                                    takipci.setText(String.valueOf(takipciSayisi));

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            followingRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    int takipciSayisi=(int)dataSnapshot.child(mAuth.getCurrentUser().getUid()).getChildrenCount();

                                    takip.setText(String.valueOf(takipciSayisi));
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            ///

                            EventBus.getDefault().postSticky(new EventBusDataEvents.kullaniciBilgileriGonder(okunacakKullaniciVerileri));

                            break;

                        }

                    }


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private void BottomMenuyuAyarla() {


        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigationView);

        BottomNavigationViewHelper bottomNavigationViewHepler = new BottomNavigationViewHelper(bottomNavigationViewEx);
        bottomNavigationViewHepler.setupNavigation(getApplicationContext(), bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NO);
        menuItem.setChecked(true);


    }

    public void moreTik(View view) {


        Intent intent = new Intent(getApplicationContext(), ProfileEditActivity.class);
        startActivity(intent);
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

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener (mAuthListener);
        kullaniciVerileriniGetir();
        gonderilerinTumunuGetir();
    }

    private void gonderilerinTumunuGetir() {
        //  Toast.makeText(this, "Searching...", Toast.LENGTH_LONG).show();
       Query queryim = mRefPost.orderByChild("user_id").equalTo(mAuth.getCurrentUser().getUid());


        FirebaseRecyclerOptions<Posts> options = new FirebaseRecyclerOptions.Builder<Posts>()
                .setQuery(queryim, Posts.class)
                .build();



        FirebaseRecyclerAdapter<Posts, FindPostsViewOrder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Posts, ProfileActivity.FindPostsViewOrder>(options) {

                    @NonNull
                    @Override
                    public ProfileActivity.FindPostsViewOrder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_anasayfa_post_recycler_liste, parent, false);
                        ProfileActivity.FindPostsViewOrder viewHolder = new ProfileActivity.FindPostsViewOrder(view);

                        return viewHolder;
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull final ProfileActivity.FindPostsViewOrder holder, final int position, @NonNull final Posts model) {


                        holder.postPaylasanIsimSoyisim.setText(model.getPost_yukleyen_isim_soyisim());
                        holder.postAciklama.setText(model.getAciklama());
                        holder.postBegeniSayisi.setText(String.valueOf(model.getPost_begeni_sayisi()) + " beğenme");
                        holder.postKacDkOnce.setText(String.valueOf(TimeAgo.getTimeAgo(model.getYuklenme_tarih())));


                        UniversalImagLoader.setImage(model.getPhoto_url(), holder.postImage, null, "");
                        UniversalImagLoader.setImage(model.getPost_yukleyen_profil_resmi(), holder.postPaylasanProfilResmi, null, "");



                        ///
                        final String id=getRef(position).getKey();

                        likesRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(dataSnapshot.child(id).hasChild(mAuth.getCurrentUser().getUid()))
                                {
                                    holder.begeniSayisi=(int)dataSnapshot.child(id).getChildrenCount();
                                    holder.postBegeniSayisi.setText(String.valueOf(holder.begeniSayisi)+" beğeni");
                                    Bitmap likeDoluKalp = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_like_ok);
                                    holder.postLike.setImageBitmap(likeDoluKalp);

                                }
                                else
                                {
                                    holder.begeniSayisi=(int)dataSnapshot.child(id).getChildrenCount();
                                    holder.postBegeniSayisi.setText(String.valueOf(holder.begeniSayisi)+" beğeni");
                                    Bitmap likeBosKalp = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_like);
                                    holder.postLike.setImageBitmap(likeBosKalp);
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });



                        ///

                        ///
                        holder.postLike.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {



                                likesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.child(id).hasChild(mAuth.getCurrentUser().getUid()))
                                        {


                                            likesRef.child(id).child(mAuth.getCurrentUser().getUid()).removeValue();
                                            Bitmap likeBosKalp = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_like);
                                            holder.postLike.setImageBitmap(likeBosKalp);

                                        }

                                        else{

                                            likesRef.child(id).child(mAuth.getCurrentUser().getUid()).setValue(true);
                                            Bitmap likeDoluKalp = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_like_ok);
                                            holder.postLike.setImageBitmap(likeDoluKalp);


                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });



                            }
                        });

                    }
                };
        postListe.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public static class FindPostsViewOrder extends RecyclerView.ViewHolder {
        CircleImageView postPaylasanProfilResmi;
        TextView postPaylasanIsimSoyisim, postBegeniSayisi, postKacDkOnce, postAciklama;
        ImageView postLike, postComment;
        GridImageView postImage;

        int begeniSayisi=0;


        public FindPostsViewOrder(@NonNull View itemView) {
            super(itemView);

            postPaylasanProfilResmi = itemView.findViewById(R.id.post_circle_profil_resmi);
            postPaylasanIsimSoyisim = itemView.findViewById(R.id.post_tvIsimSoyisim);
            postBegeniSayisi = itemView.findViewById(R.id.post_tvBegenmeSayisi);
            postKacDkOnce = itemView.findViewById(R.id.post_tvKacDkOnce);
            postAciklama = itemView.findViewById(R.id.post_tvAciklama);
            postLike = itemView.findViewById(R.id.post_imgLike);
            postComment = itemView.findViewById(R.id.post_imgComment);
            postImage = itemView.findViewById(R.id.post_imgPostFoto);


        }

    }
    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener!=null)
        {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void setupAuthListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Intent intent=new Intent(getApplicationContext(),AcilisActivity.class);
                    startActivity(intent);
                    finish();

                }
                else {

                }

            }
        };
    }

}
