package com.yusufalicezik.wots.Search;

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
import android.widget.TextView;


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
import com.yusufalicezik.wots.Message.MessagesPersonActivity;
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

public class SearchTikProfileActivity extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener mAuthListener;


    String gelenUser_id;
    private int ACTIVITY_NO=1;

    FirebaseDatabase database;
    DatabaseReference mRef,mRefPost,likesRef, followerRef, followingRef;
    FirebaseAuth mAuth;

    private RecyclerView postListe;


    ///

    Users okunacakKullaniciVerileri;
    private TextView takipci, takip, bio, isim, takipEtmeDurumu;
    private CircleImageView profilResim;
    private ProgressBar progressBar;

    ////
    //mesaj için

    private String mesajaGidecekProfilResmi;
    private String mesajaGidecekIsimSoyisim;
    private String mesajaGidecekID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tik_profile);
        gelenUser_id=getIntent().getStringExtra("user_id");
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        mRef=database.getReference().child("users");


        mRefPost = FirebaseDatabase.getInstance().getReference().child("posts");
        likesRef= FirebaseDatabase.getInstance().getReference().child("likes");
        followerRef= FirebaseDatabase.getInstance().getReference().child("follower");
        followingRef= FirebaseDatabase.getInstance().getReference().child("following");



        ///



        profilResim = findViewById(R.id.profile_image);
        takip = findViewById(R.id.tvProfileTakip);
        takipci = findViewById(R.id.tvProfileTakipci);
        bio = findViewById(R.id.tvProfileBio);
        isim = findViewById(R.id.tvProfileIsimSoyisim);
        progressBar = findViewById(R.id.profileProgressBar);
        takipEtmeDurumu=findViewById(R.id.tvProfileTakipEt);


        postListe = findViewById(R.id.profilTikRecListe);
        postListe.setHasFixedSize(true);
        LinearLayoutManager ll = new LinearLayoutManager(this);
        ll.setReverseLayout(true);
        ll.setStackFromEnd(true);
        postListe.setLayoutManager(ll);

        setupAuthListener();
        BottomMenuyuAyarla();
        UniversalInit();





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
        Query queryim = mRefPost.orderByChild("user_id").equalTo(gelenUser_id);


        FirebaseRecyclerOptions<Posts> options = new FirebaseRecyclerOptions.Builder<Posts>()
                .setQuery(queryim, Posts.class)
                .build();



        FirebaseRecyclerAdapter<Posts, FindPostsViewOrder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Posts, SearchTikProfileActivity.FindPostsViewOrder>(options) {

                    @NonNull
                    @Override
                    public SearchTikProfileActivity.FindPostsViewOrder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_anasayfa_post_recycler_liste, parent, false);
                        SearchTikProfileActivity.FindPostsViewOrder viewHolder = new SearchTikProfileActivity.FindPostsViewOrder(view);

                        return viewHolder;
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull final SearchTikProfileActivity.FindPostsViewOrder holder, final int position, @NonNull final Posts model) {



                        holder.postPaylasanIsimSoyisim.setText(model.getPost_yukleyen_isim_soyisim());
                        holder.postAciklama.setText(model.getAciklama());
                        holder.postBegeniSayisi.setText(String.valueOf(model.getPost_begeni_sayisi()) + " beğenme");
                        holder.postKacDkOnce.setText(String.valueOf(TimeAgo.getTimeAgo(model.getYuklenme_tarih())));


                        UniversalImagLoader.setImage(model.getPhoto_url(), holder.postImage, null, "");
                        UniversalImagLoader.setImage(model.getPost_yukleyen_profil_resmi(), holder.postPaylasanProfilResmi, null, "");



                        final String id=getRef(position).getKey();


                        ///


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

    private void kullaniciVerileriniGetir() {


        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    for (DataSnapshot user : dataSnapshot.getChildren()) {
                        okunacakKullaniciVerileri = user.getValue(Users.class);
                        if (okunacakKullaniciVerileri.getUser_id().equals(gelenUser_id)) {

                            takip.setText(okunacakKullaniciVerileri.getUser_details().getFollowing());
                            takipci.setText(okunacakKullaniciVerileri.getUser_details().getFollower());
                            bio.setText(okunacakKullaniciVerileri.getUser_details().getBiography());
                            isim.setText(okunacakKullaniciVerileri.getIsim_soyisim());
                            String imgUrl = okunacakKullaniciVerileri.getUser_details().getProfile_picture();

                            UniversalImagLoader.setImage(imgUrl, profilResim, progressBar, "");


                            /// MESAJ İÇİN
                            mesajaGidecekIsimSoyisim=okunacakKullaniciVerileri.getIsim_soyisim();
                            mesajaGidecekProfilResmi=okunacakKullaniciVerileri.getUser_details().getProfile_picture();
                            mesajaGidecekID=okunacakKullaniciVerileri.getUser_id();

                            ///


                            //EventBus.getDefault().postSticky(new EventBusDataEvents.kullaniciBilgileriGonder(okunacakKullaniciVerileri));


                            followerRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if(dataSnapshot.child(gelenUser_id).hasChild(String.valueOf(mAuth.getCurrentUser().getUid())))
                                    {
                                        takipEtmeDurumu.setText("Takipten Çık");
                                    }else
                                    {
                                        takipEtmeDurumu.setText("Takip Et");
                                    }

                                    int takipciSayisi=(int)dataSnapshot.child(gelenUser_id).getChildrenCount();

                                    takipci.setText(String.valueOf(takipciSayisi));

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            followingRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    int takipciSayisi=(int)dataSnapshot.child(gelenUser_id).getChildrenCount();

                                    takip.setText(String.valueOf(takipciSayisi));
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

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
    private void UniversalInit() {
        UniversalImagLoader universalImageLoader = new UniversalImagLoader(getApplicationContext());
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }
    private void BottomMenuyuAyarla() {


        BottomNavigationViewEx bottomNavigationViewEx=findViewById(R.id.bottomNavigationView);

        BottomNavigationViewHelper bottomNavigationViewHepler=new BottomNavigationViewHelper(bottomNavigationViewEx);
        bottomNavigationViewHepler.setupNavigation(getApplicationContext(),bottomNavigationViewEx);

        Menu menu=bottomNavigationViewEx.getMenu();
        MenuItem menuItem=menu.getItem(ACTIVITY_NO);
        menuItem.setChecked(true);


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

    public void takipEtTik(View view)
    {

        if(takipEtmeDurumu.getText().toString().equals("Takip Et"))
        {
            followerRef.child(gelenUser_id).child(mAuth.getCurrentUser().getUid()).setValue(true);
            followingRef.child(mAuth.getCurrentUser().getUid()).child(gelenUser_id).setValue(true);
            takipEtmeDurumu.setText("Takipten Çık");
        }
        else
        {
            followerRef.child(gelenUser_id).child(mAuth.getCurrentUser().getUid()).removeValue();
            followingRef.child(mAuth.getCurrentUser().getUid()).child(gelenUser_id).removeValue();
            takipEtmeDurumu.setText("Takipten Et");
        }

    }

    public void mesajGonderTik(View view)
    {
        Intent intent=new Intent(SearchTikProfileActivity.this, MessagesPersonActivity.class);
        intent.putExtra("isim",mesajaGidecekIsimSoyisim);
        intent.putExtra("profilresmi",mesajaGidecekProfilResmi);
        intent.putExtra("id",mesajaGidecekID);
        startActivity(intent);

    }
}
