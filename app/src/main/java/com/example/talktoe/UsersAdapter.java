package com.example.talktoe;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder>{

    private Context mCtx;
    private List<Users> UsersList;
    DatabaseReference mDatabase;

    TextView id;


    public UsersAdapter(Context mCtx, List<Users> UsersList) {
        this.mCtx = mCtx;
        this.UsersList = UsersList;


    }



//   public interface onItemLongClickListener{
    //       public boolean onItemLongClicked(int position);
    //  }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_layout, null);
        UsersViewHolder holder = new UsersViewHolder(view);
        return holder;

    }





    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder UsersViewHolder, int position) {
        Users Users = UsersList.get(position);

        //
     //   UsersViewHolder.textViewUsersTitle.setText(Users.getUsers());
     //   UsersViewHolder.textViewUsersDate.setText(Users.getDate());
     //   UsersViewHolder.textViewUsersTime.setText(Users.getTime());
     //   UsersViewHolder.textViewUsersID.setText(Users.getID());
    }

    @Override
    public int getItemCount() {
        return UsersList.size();
    }

    class UsersViewHolder extends RecyclerView.ViewHolder {


        TextView textViewUsersTitle, textViewUsersDate, textViewUsersTime, textViewUsersID;

        public UsersViewHolder(View UsersView){
            super(UsersView);

            textViewUsersTitle = itemView.findViewById(R.id.textViewUsersTitle);
            textViewUsersDate = itemView.findViewById(R.id.textViewUsersDate);
            textViewUsersTime = itemView.findViewById(R.id.textViewUsersTime);
            textViewUsersID = itemView.findViewById(R.id.textViewUsersID);

        }


    }

}
