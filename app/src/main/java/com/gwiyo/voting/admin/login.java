package com.gwiyo.voting.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.gwiyo.voting.candidates.Candidates;
import com.gwiyo.voting.databinding.ActivityAdminloginBinding;
import com.gwiyo.voting.voters.EmailRegistration;
import com.gwiyo.voting.voters.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {
ActivityAdminloginBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference userRef;
    private ProgressDialog progressDialog;
    private Boolean emailAddresschecker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivityAdminloginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //////status bar start////
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //////status bar start////

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        binding.adminLoginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
        binding.noaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(login.this, EmailRegistration.class);
                startActivity(register);
            }
        });
    }

    private void Login() {
        progressDialog.setTitle("We are checking your credentials");
        progressDialog.setMessage("Please wait.....");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        String email = binding.admintxtEmail.getText().toString();
        String password = binding.admintxtPassword.getText().toString();

        if (email.isEmpty()) {
            binding.admintxtEmail.setError("Email cannot be empty");
            return;
        }
        else if (password.isEmpty()) {
            binding.admintxtPassword.setError("Password cannot be empty");
            return;
        }

        else {

            //login
            auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            verifyEmailAddress();
                            progressDialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(login.this, "error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void verifyEmailAddress() {
        FirebaseUser user = auth.getCurrentUser();
        emailAddresschecker = user.isEmailVerified();
        //if email is verified
        if (emailAddresschecker){
            //send user to home Activity
            checkUserType();
            progressDialog.dismiss();
        }
        //email not verified
        else
        {

            Toast.makeText(this, "Please verify the email", Toast.LENGTH_SHORT).show();
            auth.signOut();
            progressDialog.dismiss();
        }
    }

    private void checkUserType() {
        //check if user is admin or voter
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("uid").equalTo(auth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot s: snapshot.getChildren()){
                            String accountType = ""+s.child("accountType").getValue();
                            if (accountType.equals("voters"))
                            {

                                //if user is admin then go to the admin dashboard
                                Intent admin = new Intent(login.this, MainActivity.class);
                                startActivity(admin);
                                finish();
                                progressDialog.dismiss();


                            }
                            else if(accountType.equals("admin")) {
                                //if user is voter then go to the voters dashboard
                                Intent voters = new Intent(login.this, Candidates.class);
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