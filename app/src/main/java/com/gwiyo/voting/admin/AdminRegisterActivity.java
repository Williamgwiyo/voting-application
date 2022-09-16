package com.gwiyo.voting.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.gwiyo.voting.databinding.ActivityAdminRegisterBinding;
import com.gwiyo.voting.databinding.ActivityAdminloginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AdminRegisterActivity extends AppCompatActivity {
    ActivityAdminRegisterBinding binding;
    FirebaseAuth auth;
    private DatabaseReference userRef;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivityAdminRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        progressDialog = new ProgressDialog(this);

        binding.haveaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(AdminRegisterActivity.this, login.class);
                startActivity(login);
            }
        });
        binding.RegisterInbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterAdmin();
            }
        });
    }

    private void RegisterAdmin() {
        String email = binding.adminsignUpEmail.getText().toString();
        String password = binding.adminsignUpPassword.getText().toString();
        String confirmPassword = binding.adminConfirmPassword.getText().toString();

        if (email.isEmpty()) {
            binding.adminsignUpEmail.setError("Email cannot be empty");
            return;
        }
        else if (password.isEmpty()) {
            binding.adminsignUpPassword.setError("Password cannot be empty");
            return;
        }
        else if (confirmPassword.isEmpty()) {
            binding.adminConfirmPassword.setError("Confirm password");
            return;
        }

        else if (!password.equals(confirmPassword))
        {
            Toast.makeText(this, "Password do not Match", Toast.LENGTH_SHORT).show();
        }
        else{
            progressDialog.setTitle("Creating New Account");
            progressDialog.setMessage("Please wait.....");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {
                        HashMap userMap = new HashMap();
                        userMap.put("email", email);
                        userMap.put("accountType", "admin");
                        userMap.put("uid", auth.getUid());
                        userRef.child(auth.getUid()).setValue(userMap).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful())
                            {
                                sendEmailVerificationMessage();
                                progressDialog.dismiss();
                            }
                            else{

                                String message = task1.getException().getMessage();
                                Toast.makeText(AdminRegisterActivity.this, "error " +message, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });
                    }).addOnFailureListener(e -> {

                Toast.makeText(AdminRegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            });
        }
    }

    private void sendEmailVerificationMessage(){
        FirebaseUser user =auth.getCurrentUser();
        if (user !=null)
        {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(AdminRegisterActivity.this, "Please check your email and verify your account", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AdminRegisterActivity.this, login.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        auth.signOut();
                    }
                    else{
                        String error = task.getException().getMessage();
                        Toast.makeText(AdminRegisterActivity.this, "Error" +error, Toast.LENGTH_SHORT).show();
                        auth.signOut();
                    }
                }
            });
        }
    }
}