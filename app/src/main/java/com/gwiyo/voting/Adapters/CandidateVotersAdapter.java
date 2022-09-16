package com.gwiyo.voting.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gwiyo.voting.Models.candidatesModel;
import com.gwiyo.voting.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CandidateVotersAdapter extends RecyclerView.Adapter<CandidateVotersAdapter.HolderCandidate> {

    private Context context;
    private ArrayList<candidatesModel> candidatesList;
    FirebaseAuth auth;
    String currentUserId;
    DatabaseReference VotesRef;
    Boolean VoteChecker = false;

    public CandidateVotersAdapter(Context context, ArrayList<candidatesModel> candidatesList) {
        this.context = context;
        this.candidatesList = candidatesList;
        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();
        VotesRef = FirebaseDatabase.getInstance().getReference().child("Votes");
    }

    @NonNull
    @Override
    public HolderCandidate onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.vote_item,parent,false);
        return new HolderCandidate(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCandidate holder, int position) {
        candidatesModel model = candidatesList.get(position);
        String candidateId = model.getCandidateId();
        String candidatename=model.getCandidatename();
        String candidateImage = model.getCandidateimage();
        String candidatemessage = model.getCandidatemessage();


        holder.candidateName.setText(candidatename);
        holder.status.setText(candidatemessage);

        try {
            Picasso.get().load(candidateImage).placeholder(R.drawable.person).into(holder.candidateImage);
        }
        catch (Exception e){
            holder.candidateImage.setImageResource(R.drawable.person);
        }

        holder.votebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //before someone vote we need to check if he is verified or not
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                reference.orderByChild("uid").equalTo(currentUserId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot s: snapshot.getChildren()){
                                    String accountType = ""+s.child("status").getValue();
                                    if (accountType.equals("not verified"))
                                    {
                                        //acount not verified
                                        Toast.makeText(context, "You need to send your ID for verification before you can vote", Toast.LENGTH_LONG).show();

                                    }
                                    else if(accountType.equals("verified")) {
                                        //if voter is verified then lets vote
                                             VoteChecker = true;
                                             VotesRef.addValueEventListener(new ValueEventListener() {
                                                 @Override
                                                 public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                     if (VoteChecker.equals(true))
                                                     {
                                                         if (snapshot.child(candidateId).hasChild(currentUserId))
                                                         {
                                                             //check if vote already exist
                                                             Toast.makeText(context, "You can only vote once", Toast.LENGTH_SHORT).show();
                                                             holder.votebtn.setBackgroundColor(Color.BLUE);
                                                             holder.votebtn.setTextColor(Color.WHITE);
                                                             holder.votebtn.setText("Voted");
                                                         }
                                                         else
                                                         {
                                                             VotesRef.child(candidateId).child(currentUserId).setValue(true);
                                                             VoteChecker=false;
                                                         }
                                                     }
                                                 }

                                                 @Override
                                                 public void onCancelled(@NonNull DatabaseError error) {

                                                 }
                                             });
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });

    }

    @Override
    public int getItemCount() {
        return candidatesList.size();
    }

    class HolderCandidate extends RecyclerView.ViewHolder{
      ImageView candidateImage;
      TextView candidateName,status;
      Button votebtn;
      public HolderCandidate(@NonNull View itemView) {
          super(itemView);
          candidateImage = itemView.findViewById(R.id.candidateImages);
          candidateName = itemView.findViewById(R.id.candidate_name);
          votebtn = itemView.findViewById(R.id.votebtn);
          status = itemView.findViewById(R.id.status);

      }
  }
}
