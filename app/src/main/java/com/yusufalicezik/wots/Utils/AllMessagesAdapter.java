package com.yusufalicezik.wots.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yusufalicezik.wots.Message.MessagesGenelActivity;
import com.yusufalicezik.wots.Message.MessagesPersonActivity;
import com.yusufalicezik.wots.Model.Messages;
import com.yusufalicezik.wots.Model.Users;
import com.yusufalicezik.wots.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class AllMessagesAdapter extends RecyclerView.Adapter<AllMessagesAdapter.AllMessageViewHolder> {

    private List<Users> userMessagesList;
    Context mContext;

    private FirebaseAuth mAuth;


    private FirebaseDatabase database;
    private DatabaseReference mRef;




    public AllMessagesAdapter(List<Users> userMessagesList,Context context)
    {
        this.userMessagesList=userMessagesList;
        this.mContext=context;
    }

    public class AllMessageViewHolder extends RecyclerView.ViewHolder {


        public TextView allMessagesIsim;
        public CircleImageView allMessagesResim;
        public ConstraintLayout genelLayout;



        public AllMessageViewHolder(View itemView) {

            super(itemView);
            allMessagesIsim=itemView.findViewById(R.id.tv_Isim_allMessages);
            allMessagesResim=itemView.findViewById(R.id.circleImageView_allMessages);
            genelLayout=itemView.findViewById(R.id.genelListeLayout);
        }
    }

    @NonNull
    @Override
    public AllMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_all_messages_list,parent,false);

        mAuth=FirebaseAuth.getInstance();

        return new AllMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllMessageViewHolder holder, final int position) {


        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();
      holder.allMessagesIsim.setText(userMessagesList.get(position).getIsim_soyisim());
      // resim sonra holder..setText(userMessagesList.get(position).getIsim_soyisim());

        UniversalImagLoader.setImage(userMessagesList.get(position).getUser_details().getProfile_picture(),
                holder.allMessagesResim, null, "");



        holder.genelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, MessagesPersonActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("isim",userMessagesList.get(position).getIsim_soyisim());
                intent.putExtra("profilresmi",userMessagesList.get(position).getUser_details().getProfile_picture());
                intent.putExtra("id",userMessagesList.get(position).getUser_id());

                mContext.startActivity(intent);


            }
        });

        holder.genelLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                MessagesGenelActivity.ccc(userMessagesList.get(position).getUser_id(), mAuth.getCurrentUser().getUid());

/*
                Toast.makeText(mContext, userMessagesList.get(position).getIsim_soyisim(), Toast.LENGTH_SHORT).show();

                mRef.child("Messages").child(mAuth.getCurrentUser().getUid()).child(userMessagesList.get(position).getUser_id()).removeValue();

*/


                notifyDataSetChanged();




                return true;
            }
        });







    }

    @Override
    public int getItemCount() {
        return userMessagesList.size();
    }


}
