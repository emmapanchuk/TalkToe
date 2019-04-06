package com.example.talktoe;

//Resources:
//https://www.simplifiedcoding.net/android-recyclerview-cardview-tutorial/#RecyclerView-Item-Layout-using-CardView

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import java.util.List;


public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder>{

    private Context mCtx;
    private List<Users> usersList;



    public UsersAdapter(Context mCtx, List<Users> usersList) {
        this.mCtx = mCtx;
        this.usersList = usersList;


    }



    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_layout, null);
        UsersViewHolder holder = new UsersViewHolder(view);
        return holder;

    }




    //Each user from usersList will be inserted into their own block using a recycler view
    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder usersViewHolder, int position) {
        Users user = usersList.get(position);


        usersViewHolder.textViewUsersName.setText(user.getName());
        Log.d("INSERT INTO DISPLAY", "USERS NAME: " + user.getName() + " Score: " + user.getScore());
        usersViewHolder.textViewUsersScore.setText(user.getScore());

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    class UsersViewHolder extends RecyclerView.ViewHolder {


        TextView textViewUsersName, textViewUsersScore;

        public UsersViewHolder(View usersView){
            super(usersView);

            textViewUsersName = itemView.findViewById(R.id.textViewUsersName);
            textViewUsersScore = itemView.findViewById(R.id.textViewUsersScore);

        }


    }

}
