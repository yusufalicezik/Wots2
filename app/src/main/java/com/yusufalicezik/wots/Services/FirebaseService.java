package com.yusufalicezik.wots.Services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yusufalicezik.wots.Message.MessagesPersonActivity;
import com.yusufalicezik.wots.Model.Messages;
import com.yusufalicezik.wots.Model.Users;
import com.yusufalicezik.wots.R;

class FireBaseService extends Service {
    FirebaseAuth mAuth=FirebaseAuth.getInstance();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {


        Log.e("deneme","tetiklendi");

        DatabaseReference mrefBildirim=FirebaseDatabase.getInstance().getReference().child("Messages").child(mAuth.getCurrentUser().getUid());
        mrefBildirim.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String mesajiGonderen;
                final Messages mSon=new Messages();
                if(dataSnapshot.getValue()!=null)
                {


                    int aa=1;
                    mSon.setTarih(Long.valueOf(aa));
                    for(DataSnapshot dd:dataSnapshot.getChildren())
                    {
                        Messages messages=dd.getValue(Messages.class);
                        if(messages.getTarih()>mSon.getTarih()) {
                            mSon.setTarih(messages.getTarih());
                            mSon.setIcerik(messages.getIcerik());
                            mSon.setFrom(messages.getFrom());
                        }
                    }
                }

                // Toast.makeText(MainActivity.this, "a"+(mSon.getTarih().toString()), Toast.LENGTH_SHORT).show();


                DatabaseReference mrefBildirim2=FirebaseDatabase.getInstance().getReference().child("users");

                mrefBildirim2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue()!=null)
                        {
                            for(DataSnapshot userim:dataSnapshot.getChildren())
                            {
                                Users anlikUsers=userim.getValue(Users.class);
                                if(anlikUsers.getUser_id().equals(mSon.getFrom()))
                                {
                                    if(mSon.getFrom().equals(mAuth.getCurrentUser().getUid()))
                                    {

                                    }else
                                        bildirimiCalistir(mSon.getIcerik(), anlikUsers.getIsim_soyisim(),anlikUsers.getUser_id(),anlikUsers.getUser_details().getProfile_picture(),mSon.getTarih());

                                }

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



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

        return null;
    }

    private void bildirimiCalistir(String icerik,String gonderen,String id,String profilresmi,Long tarih)
    {

        Intent pendingIntent=new Intent(this, MessagesPersonActivity.class);
        pendingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        pendingIntent.putExtra("id",id);
        pendingIntent.putExtra("isim",gonderen);
        pendingIntent.putExtra("profilresmi",profilresmi);


        PendingIntent bildirimIntent=PendingIntent.getActivity(this,10,pendingIntent,PendingIntent.FLAG_UPDATE_CURRENT);



        Notification builder=new NotificationCompat.Builder(this,"asd")
                .setSmallIcon(R.drawable.logos)
                .setContentTitle(gonderen)

                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.logos))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(gonderen)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(icerik))
                .setContentText("Okumak için tıklayın")


                // .setColor(R.color.anaYesil).setOnlyAlertOnce(true)
                //.addAction(R.drawable.iconum,"Şimdi Öde !",bildirimIntent)

                .setAutoCancel(true)
                .setContentIntent(bildirimIntent)
                .build();





        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel("asd", "Yeni Ödeme !", NotificationManager.IMPORTANCE_MAX);

            notificationManager.createNotificationChannel(notificationChannel);
        }


        notificationManager.notify(1/* Request Code */, builder);



    }
}