package com.gwiyo.voting.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gwiyo.voting.Models.UsersModel;
import com.gwiyo.voting.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class WaitToAproveAdapter extends RecyclerView.Adapter<WaitToAproveAdapter.HolderApprove> {

    private Context context;
    private ArrayList<UsersModel> userList;
    FirebaseAuth auth;
    String currentUserId;
    private DatabaseReference userRef;

    public WaitToAproveAdapter(Context context, ArrayList<UsersModel> userList) {
        this.context = context;
        this.userList = userList;
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    @NonNull
    @Override
    public HolderApprove onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.verification_item,parent,false);
        return new HolderApprove(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderApprove holder, int position) {
        UsersModel model = userList.get(position);
        String username = model.getName();
        String userID = model.getVoterID();
        String status = model.getStatus();
        String uid = model.getUid();


        holder.votersName.setText(username);

        try {
            Picasso.get().load(userID).placeholder(R.drawable.person).into(holder.userVerificationID);
        }
        catch (Exception e){
            holder.userVerificationID.setImageResource(R.drawable.person);
        }

        holder.approvebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("wait");
                progressDialog.show();

                HashMap userMap = new HashMap();
                userMap.put("status", "verified");
                userRef.child(uid).updateChildren(userMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(context, "Voter Approved", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            }
        });


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class HolderApprove extends RecyclerView.ViewHolder{
      ImageView userVerificationID;
      TextView votersName;
      Button approvebtn;
      public HolderApprove(@NonNull View itemView) {
          super(itemView);
          userVerificationID = itemView.findViewById(R.id.userVerificationID);
          votersName = itemView.findViewById(R.id.votersnames);
          approvebtn = itemView.findViewById(R.id.approvebtn);
      }
  }
}
