package com.yusufalicezik.wots.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yusufalicezik.wots.Message.MessagesGenelActivity;
import com.yusufalicezik.wots.R;

@SuppressLint("ValidFragment")
public class Dialog extends DialogFragment {






    private TextView mBtnKapat, mNotEkle;
    private Context context;



    String silinecek, silinen;
    @SuppressLint("ValidFragment")
    public Dialog(String silinecek, String silinen, Context context)
    {
        this.silinecek=silinecek;
        this.silinen=silinen;
        this.context=context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBtnKapat=view.findViewById(R.id.btn_cancel);
        mNotEkle=view.findViewById(R.id.btn_cancel2);



        mBtnKapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();

                DatabaseReference mRef=FirebaseDatabase.getInstance().getReference();
                mRef.child("Messages").child(silinen).child(silinecek).removeValue();
                Intent ii=new Intent(context,MessagesGenelActivity.class);
                ii.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ii.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(ii);


            }
        });

        mNotEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                dismiss();
            }
        });
    }
}