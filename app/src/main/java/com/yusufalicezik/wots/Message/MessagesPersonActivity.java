package com.yusufalicezik.wots.Message;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.yusufalicezik.wots.Model.Messages;
import com.yusufalicezik.wots.Model.Users;
import com.yusufalicezik.wots.R;
import com.yusufalicezik.wots.Utils.EventBusDataEvents;
import com.yusufalicezik.wots.Utils.MessagesAdapter;
import com.yusufalicezik.wots.Utils.UniversalImagLoader;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesPersonActivity extends AppCompatActivity {

    private CircleImageView profilResim;
    private TextView isimSoyisim;

    private EditText mesajText;

    private String mesajGonderilenID;
    private String mesajGonderilenProfilResmi;
    private String mesajGonderilenIsimSoyisim;

    ///
    private FirebaseDatabase database;
    private DatabaseReference mRefMesajGonderen,mRefMesajAlan,mRef;
    private FirebaseAuth mAuth;
    private RecyclerView liste;

    ///

    String currentProfileImage, currentIsim;

    ////
    private final List<Messages> messagesList=new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessagesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_person);

        database=FirebaseDatabase.getInstance();
        mRefMesajGonderen=database.getReference().child("Messages");
        mRefMesajAlan=database.getReference().child("Messages");
        mRef=database.getReference();
        mAuth=FirebaseAuth.getInstance();

        profilResim=findViewById(R.id.circleImageViewMessagesPerson);
        isimSoyisim=findViewById(R.id.tvPersonMessagesIsimSoyisim);
        mesajText=findViewById(R.id.et_personMessagesText);


        UniversalInit();

        mesajGonderilenID=getIntent().getStringExtra("id");
        mesajGonderilenProfilResmi=getIntent().getStringExtra("profilresmi");
        mesajGonderilenIsimSoyisim=getIntent().getStringExtra("isim");

        isimSoyisim.setText(mesajGonderilenIsimSoyisim);
        UniversalImagLoader.setImage(mesajGonderilenProfilResmi, profilResim, null, "");






        adapter=new MessagesAdapter(messagesList);
        liste=findViewById(R.id.messageListe);
        linearLayoutManager=new LinearLayoutManager(MessagesPersonActivity.this);
        linearLayoutManager.setStackFromEnd(true);
        liste.setHasFixedSize(true);
        liste.setLayoutManager(linearLayoutManager);
        liste.setAdapter(adapter);
        fetchMessage();




    }

    private void currentKullaniciVerileriniGetir()
    {mRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.getValue() != null) {

                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    Users okunacakKullaniciVerileri = user.getValue(Users.class);
                    if (okunacakKullaniciVerileri.getUser_id().equals(mAuth.getCurrentUser().getUid())) {


                         currentProfileImage = okunacakKullaniciVerileri.getUser_details().getProfile_picture();
                         currentIsim=okunacakKullaniciVerileri.getIsim_soyisim();


                         ///

                        Toast.makeText(MessagesPersonActivity.this, currentIsim, Toast.LENGTH_SHORT).show();


                        ///



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


    public void mesajGonderTic(View view)
    {

        String mesajIcerik=mesajText.getText().toString();

        if(!mesajIcerik.equals("") || !mesajIcerik.isEmpty())
        {
            String mesajID=mRefMesajGonderen.push().getKey();

/*

        mRefMesajGonderen.child(mAuth.getCurrentUser().getUid()).child(mesajGonderilenID).child(mesajID).child("tarih").setValue(ServerValue.TIMESTAMP);
        mRefMesajGonderen.child(mAuth.getCurrentUser().getUid()).child(mesajGonderilenID).child(mesajID).child("icerik").setValue(mesajIcerik);
        mRefMesajGonderen.child(mAuth.getCurrentUser().getUid()).child(mesajGonderilenID).child(mesajID).child("from").setValue(mAuth.getCurrentUser().getUid());

        mRefMesajAlan.child(mesajGonderilenID).child(mAuth.getCurrentUser().getUid()).child(mesajID).child("tarih").setValue(ServerValue.TIMESTAMP);
        mRefMesajAlan.child(mesajGonderilenID).child(mAuth.getCurrentUser().getUid()).child(mesajID).child("icerik").setValue(mesajIcerik);
        mRefMesajAlan.child(mesajGonderilenID).child(mAuth.getCurrentUser().getUid()).child(mesajID).child("from").setValue(mAuth.getCurrentUser().getUid());

*/


            //  String message=mChatMessageView.getText().toString();



            DatabaseReference user_message_push = mRefMesajAlan.child(mAuth.getCurrentUser().getUid()).child(mesajGonderilenID).push();
            String push_id = user_message_push.getKey();

            Map messageMap = new HashMap();
            messageMap.put("icerik", mesajIcerik);


            messageMap.put("tarih", ServerValue.TIMESTAMP);
            messageMap.put("from", mAuth.getCurrentUser().getUid());





            String current_user_ref=mAuth.getCurrentUser().getUid()+ "/" +mesajGonderilenID;
            String chat_user_Ref=mesajGonderilenID+"/"+mAuth.getCurrentUser().getUid();


            Map messageUserMap = new HashMap();
            messageUserMap.put(current_user_ref+ "/" + push_id, messageMap);
            messageUserMap.put(chat_user_Ref + "/" + push_id, messageMap);

            mesajText.setText("");

            mRefMesajAlan.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {

                        //  Log.d("CHAT LOG", databaseError.getMessage().toString());
                    }
                }
            });
        }
        else
        {
            FancyToast.makeText(this,"Lütfen bir mesaj yazın",FancyToast.LENGTH_LONG,FancyToast.INFO,false).show();

        }





    }


    private void fetchMessage() {

    mRefMesajAlan.child(mAuth.getCurrentUser().getUid()).child(mesajGonderilenID).addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            if(dataSnapshot.exists())
            {
                Messages messages=dataSnapshot.getValue(Messages.class);

                messagesList.add(messages);
                adapter.notifyDataSetChanged();
                liste.smoothScrollToPosition(liste.getAdapter().getItemCount());

            }

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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

    public void geriTic(View view)
    {
        onBackPressed();
    }
}
