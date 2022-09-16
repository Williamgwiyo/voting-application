package com.gwiyo.voting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.gwiyo.voting.candidates.Candidates;
import com.gwiyo.voting.databinding.ActivityAdminRegisterBinding;
import com.gwiyo.voting.databinding.ActivitySplashBinding;
import com.gwiyo.voting.voters.EmailRegistration;
import com.gwiyo.voting.voters.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;
    private FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

//////status bar start////
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //////status bar start////

        ////handler for moving to main activity///
        new Handler().postDelayed(() -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user==null){
                Intent intent = new Intent(SplashActivity.this, EmailRegistration.class);
                startActivity(intent);
                finish();
            }else{
                //check if user logged in is admin or voter
                checkUserType();
            }


        },3000);

        ////handler for moving to main activity///
    }

    private void checkUserType() {
        //check if user is admin or voter
        progressDialog.setMessage("Please wait.....");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot s: snapshot.getChildren()){
                            String accountType = ""+s.child("accountType").getValue();
                            if (accountType.equals("voters"))
                            {

                                //if user is admin then go to the admin dashboard
                                Intent admin = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(admin);
                                finish();
                                progressDialog.dismiss();


                            }
                            else if(accountType.equals("admin")) {
                                //if user is voter then go to the voters dashboard
                                Intent voters = new Intent(SplashActivity.this, Candidates.class);
                                startActivity(voters);
                                finish();
                                progressDialog.dismiss();

                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}