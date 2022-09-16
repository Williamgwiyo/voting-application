package com.gwiyo.voting.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.gwiyo.voting.Adapters.verifiedAdapter;
import com.gwiyo.voting.Models.UsersModel;
import com.gwiyo.voting.R;
import com.gwiyo.voting.databinding.ActivityVerifiedBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VerifiedActivity extends AppCompatActivity {
ActivityVerifiedBinding binding;
    FirebaseAuth auth;
    String currentUserID;
    private ArrayList<UsersModel> userlist;
    RecyclerView verifiedRecyclerview;
    private verifiedAdapter verifiedAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivityVerifiedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        currentUserID = auth.getCurrentUser().getUid();
        verifiedRecyclerview = findViewById(R.id.verifiedRecyclerview);

        loadVerifiedUsers();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadVerifiedUsers() {
        checkverificationstatus();
    }

    private void checkverificationstatus() {
        //check if user is admin or voter
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait.....");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        userlist = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("status").equalTo("verified")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userlist.clear();
                        userlist.size();
                        for (DataSnapshot s: snapshot.getChildren()){
                            if (snapshot.exists()){
                                //display the ids

                                UsersModel model = s.getValue(UsersModel.class);
                                userlist.add(model);
                                progressDialog.dismiss();
                            }
                        }
                        verifiedAdapter = new verifiedAdapter(VerifiedActivity.this,userlist);
                        verifiedRecyclerview.setAdapter(verifiedAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(VerifiedActivity.this, ""+ error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }
}