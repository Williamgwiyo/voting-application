package com.gwiyo.voting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.gwiyo.voting.Adapters.WaitToAproveAdapter;
import com.gwiyo.voting.Models.UsersModel;
import com.gwiyo.voting.databinding.ActivityVerifiedBinding;
import com.gwiyo.voting.databinding.ActivityWaitingVerificationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WaitingVerificationActivity extends AppCompatActivity {
    ActivityWaitingVerificationBinding binding;
    FirebaseAuth auth;
    String currentUserID;
    private ArrayList<UsersModel> userlist;
    RecyclerView waitingRvs;
    private WaitToAproveAdapter waitToAproveAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivityWaitingVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        currentUserID = auth.getCurrentUser().getUid();
        waitingRvs = findViewById(R.id.waitingRvs);

        loadIDs();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            onBackPressed();
            }
        });
    }

    private void loadIDs() {
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
        reference.orderByChild("status").equalTo("not verified")
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
                        waitToAproveAdapter = new WaitToAproveAdapter(WaitingVerificationActivity.this,userlist);
                        waitingRvs.setAdapter(waitToAproveAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(WaitingVerificationActivity.this, ""+ error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }
}