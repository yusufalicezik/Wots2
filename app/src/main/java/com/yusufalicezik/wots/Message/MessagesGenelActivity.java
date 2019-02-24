package com.yusufalicezik.wots.Message;

import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yusufalicezik.wots.Model.Messages;
import com.yusufalicezik.wots.Model.Users;
import com.yusufalicezik.wots.R;
import com.yusufalicezik.wots.Utils.AllMessagesAdapter;
import com.yusufalicezik.wots.Utils.BottomNavigationViewHelper;
import com.yusufalicezik.wots.Utils.Dialog;
import com.yusufalicezik.wots.Utils.UniversalImagLoader;

import java.util.ArrayList;
import java.util.List;

public class MessagesGenelActivity extends AppCompatActivity {



    private int ACTIVITY_NO=3;

    public static Context contextim;
    public static android.support.v4.app.FragmentManager ff;
    public static AllMessagesAdapter aa;

    private List<Users> mesajlastigimKullanicilar=new ArrayList<>();

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference mRef;


    RecyclerView liste;
    private LinearLayoutManager linearLayoutManager;
    private AllMessagesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_genel);

        contextim=getApplicationContext();
        ff=getSupportFragmentManager();

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();

        BottomMenuyuAyarla();
        UniversalInit();

        kimlerleMesajlastigimiGetir();


        adapter=new AllMessagesAdapter(mesajlastigimKullanicilar,MessagesGenelActivity.this);
        liste=findViewById(R.id.recyclerView_allmessagesListe);
        linearLayoutManager=new LinearLayoutManager(MessagesGenelActivity.this);
        linearLayoutManager.setStackFromEnd(false);
        liste.setHasFixedSize(true);
        liste.setLayoutManager(linearLayoutManager);
        liste.setAdapter(adapter);



    }

    private void kimlerleMesajlastigimiGetir() {

        mRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null)
                {
                    for(DataSnapshot user:dataSnapshot.getChildren()) {

                        final Users okunanKullanici=user.getValue(Users.class);




                        mRef.child("Messages").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(dataSnapshot.hasChild(okunanKullanici.getUser_id()))
                                {
                                    mesajlastigimKullanicilar.add(okunanKullanici);

                                    adapter.notifyDataSetChanged();
                                    aa=adapter;
                                    //Toast.makeText(MessagesGenelActivity.this, okunanKullanici.getIsim_soyisim(), Toast.LENGTH_SHORT).show();
                                    //mesajlasitigim kişilerin bilgilerini alıp listeye ekledim.
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void BottomMenuyuAyarla() {


        BottomNavigationViewEx bottomNavigationViewEx=findViewById(R.id.bottomNavigationView);

        BottomNavigationViewHelper bottomNavigationViewHepler=new BottomNavigationViewHelper(bottomNavigationViewEx);
        bottomNavigationViewHepler.setupNavigation(getApplicationContext(),bottomNavigationViewEx);

        Menu menu=bottomNavigationViewEx.getMenu();
        MenuItem menuItem=menu.getItem(ACTIVITY_NO);
        menuItem.setChecked(true);


    }

    private void UniversalInit() {
        UniversalImagLoader universalImageLoader = new UniversalImagLoader(getApplicationContext());
        ImageLoader.getInstance().init(universalImageLoader.getConfig());


    }

    public static void ccc(String silinecekID, String silinenID)
    {


        Dialog dialog=new Dialog(silinecekID,silinenID,contextim);
        dialog.show(ff, "DialogYeniNot");
    }
}
