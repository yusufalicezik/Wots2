package com.yusufalicezik.wots.Search;

import android.content.Intent;
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
import android.view.WindowManager;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;

import com.yusufalicezik.wots.Model.FindFriends;
import com.yusufalicezik.wots.R;
import com.yusufalicezik.wots.Utils.BottomNavigationViewHelper;
import com.yusufalicezik.wots.Utils.UniversalImagLoader;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchActivity extends AppCompatActivity {
    private int ACTIVITY_NO=1;


    private Button searchButton;
    private EditText searchInputText;
    private RecyclerView searchResultList;


    private DatabaseReference allUsersDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        BottomMenuyuAyarla();
        UniversalInit();


        allUsersDatabaseRef= FirebaseDatabase.getInstance().getReference().child("users");


        searchResultList=findViewById(R.id.searchRecyclerListe);
        searchResultList.setHasFixedSize(true);
        searchResultList.setLayoutManager(new LinearLayoutManager(SearchActivity.this));


        searchButton=findViewById(R.id.searchButton);
        searchInputText=findViewById(R.id.searchText);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String searchBoxInput=searchInputText.getText().toString().toUpperCase();
                searchPeopleAndFriends(searchBoxInput);
            }
        });


    }

    private void searchPeopleAndFriends(String searchBoxInput) {

        Toast.makeText(this, "Searching...", Toast.LENGTH_LONG).show();

        Query searchPeopleandFriendsQuery = allUsersDatabaseRef.orderByChild("isim_soyisim")
                .startAt(searchBoxInput).endAt(searchBoxInput + "\uf8ff");


        FirebaseRecyclerOptions<FindFriends> options=new FirebaseRecyclerOptions.Builder<FindFriends>()
                .setQuery(searchPeopleandFriendsQuery, FindFriends.class)
                .build();



        FirebaseRecyclerAdapter<FindFriends, FindFriendsViewOrder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<FindFriends, FindFriendsViewOrder>(options) {
                    @NonNull
                    @Override
                    public FindFriendsViewOrder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_search_recycler_liste, parent, false);
                        FindFriendsViewOrder viewHolder=new FindFriendsViewOrder(view);
                        return viewHolder;
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull FindFriendsViewOrder holder, final int position, @NonNull FindFriends model) {

                        holder.myName.setText(model.getIsim_soyisim());

                        //Toast.makeText(SearchActivity.this, "d"+model.getUser_details().getProfile_picture(), Toast.LENGTH_SHORT).show();
                        UniversalImagLoader.setImage(model.getUser_details().getProfile_picture(),holder.myImage,null,"");
                      //  Picasso.with().load(model.getBiography()).placeholder(R.drawable.ic_person2).into(holder.myImage);


                        holder.myName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String id=getRef(position).getKey();
                                //Toast.makeText(SearchActivity.this, "id: "+id, Toast.LENGTH_SHORT).show();

                                Intent intent=new Intent(SearchActivity.this, SearchTikProfileActivity.class);
                                intent.putExtra("user_id",id);
                                startActivity(intent);
                            }
                        });


                    }
                };
        searchResultList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public static class FindFriendsViewOrder extends RecyclerView.ViewHolder
    {
        CircleImageView myImage;
        TextView myName;


        public FindFriendsViewOrder(@NonNull View itemView)
        {
            super(itemView);
            myImage=itemView.findViewById(R.id.searchResultPicture);
            myName=itemView.findViewById(R.id.searchResultNameSurname);

        }



    }

    private void UniversalInit() {
        UniversalImagLoader universalImageLoader = new UniversalImagLoader(SearchActivity.this);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }



    private void BottomMenuyuAyarla() {


        BottomNavigationViewEx bottomNavigationViewEx=findViewById(R.id.bottomNavigationView);

        BottomNavigationViewHelper bottomNavigationViewHepler=new BottomNavigationViewHelper(bottomNavigationViewEx);
        bottomNavigationViewHepler.setupNavigation(SearchActivity.this,bottomNavigationViewEx);

        Menu menu=bottomNavigationViewEx.getMenu();
        MenuItem menuItem=menu.getItem(ACTIVITY_NO);
        menuItem.setChecked(true);


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
