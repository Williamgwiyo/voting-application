package com.gwiyo.voting.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gwiyo.voting.Models.UsersModel;
import com.gwiyo.voting.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class verifiedAdapter extends RecyclerView.Adapter<verifiedAdapter.HolderApprove> {

    private Context context;
    private ArrayList<UsersModel> userList;
    FirebaseAuth auth;
    String currentUserId;

    public verifiedAdapter(Context context, ArrayList<UsersModel> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public HolderApprove onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.verified_item,parent,false);
        return new HolderApprove(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderApprove holder, int position) {
        UsersModel model = userList.get(position);
        String username = model.getName();
        String userID = model.getVoterID();


        holder.verifiedName.setText(username);

        try {
            Picasso.get().load(userID).placeholder(R.drawable.person).into(holder.verifiedImage);
        }
        catch (Exception e){
            holder.verifiedImage.setImageResource(R.drawable.person);
        }

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class HolderApprove extends RecyclerView.ViewHolder{
      ImageView verifiedImage;
      TextView verifiedName;
      public HolderApprove(@NonNull View itemView) {
          super(itemView);
          verifiedImage = itemView.findViewById(R.id.verifiedImage);
          verifiedName = itemView.findViewById(R.id.verifiedName);
      }
  }
}
