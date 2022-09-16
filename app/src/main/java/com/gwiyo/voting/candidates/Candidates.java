package com.gwiyo.voting.candidates;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.gwiyo.voting.Adapters.CandidateAdapter;
import com.gwiyo.voting.Models.candidatesModel;
import com.gwiyo.voting.R;
import com.gwiyo.voting.WaitingVerificationActivity;
import com.gwiyo.voting.admin.AddCandidate;
import com.gwiyo.voting.admin.VerifiedActivity;
import com.gwiyo.voting.databinding.ActivityAdminRegisterBinding;
import com.gwiyo.voting.databinding.ActivityCandidatesBinding;
import com.gwiyo.voting.voters.EmailRegistration;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Candidates extends AppCompatActivity {
ActivityCandidatesBinding binding;
FirebaseAuth auth;
String currentUserID;
private ArrayList<candidatesModel> candidatesModelsList;
RecyclerView presidentRv;
private CandidateAdapter candidateAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivityCandidatesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        currentUserID = auth.getCurrentUser().getUid();
        presidentRv = findViewById(R.id.presidentRv);
        loadCandidates();

        binding.addCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addcandidate = new Intent(Candidates.this, AddCandidate.class);
                startActivity(addcandidate);
            }
        });

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Toast.makeText(Candidates.this, "User logout", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Candidates.this, EmailRegistration.class);
                startActivity(intent);
            }
        });
        binding.waitingVeriBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Candidates.this, WaitingVerificationActivity.class);
                startActivity(intent);
            }
        });
        binding.VerifiedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Candidates.this, VerifiedActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadCandidates() {
        candidatesModelsList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Candidates");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                candidatesModelsList.clear();
                candidatesModelsList.size();

                for (DataSnapshot ds: snapshot.getChildren()){
                    if (snapshot.exists()){
                        candidatesModel model = ds.getValue(candidatesModel.class);
                        candidatesModelsList.add(model);
                    }
                }
                candidateAdapter = new CandidateAdapter(Candidates.this,candidatesModelsList);
                presidentRv.setAdapter(candidateAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}