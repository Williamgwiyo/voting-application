package com.gwiyo.voting.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

public class CandidateAdapter extends RecyclerView.Adapter<CandidateAdapter.HolderCandidate> {

    private Context context;
    private ArrayList<candidatesModel> candidatesList;
    FirebaseAuth auth;
    String currentUserId;
    DatabaseReference VotesRef;

    public CandidateAdapter(Context context, ArrayList<candidatesModel> candidatesList) {
        this.context = context;
        this.candidatesList = candidatesList;
        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();
        VotesRef = FirebaseDatabase.getInstance().getReference().child("Votes");
    }

    @NonNull
    @Override
    public HolderCandidate onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.candidates_item,parent,false);
        return new HolderCandidate(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCandidate holder, int position) {
        candidatesModel model = candidatesList.get(position);
        String candidateId = model.getCandidateId();
        String candidatename=model.getCandidatename();
        String candidateImage = model.getCandidateimage();
        String candidatemessage = model.getCandidatemessage();

        //display the no of likes
        VotesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    //count no of likes on a single post and set thumb to blue
                    holder.countvotes = (int) snapshot.child(candidateId).getChildrenCount();
                    // likeBtn.setImageResource(R.drawable.thumb_up_blue);

                    holder.no_of_votes.setText((holder.countvotes +(" Votes")));
                }
                else
                {
                    //count no of likes on a single post
                    holder.countvotes = (int) snapshot.child(candidateId).getChildrenCount();
                    // likeBtn.setDrawa(R.drawable.thumb_up_outline);
                    holder.no_of_votes.setText((holder.countvotes +(" Votes")));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.candidateName.setText(candidatename);

        try {
            Picasso.get().load(candidateImage).placeholder(R.drawable.person).into(holder.candidateImage);
        }
        catch (Exception e){
            holder.candidateImage.setImageResource(R.drawable.person);
        }
    }

    @Override
    public int getItemCount() {
        return candidatesList.size();
    }

    class HolderCandidate extends RecyclerView.ViewHolder{
      ImageView candidateImage;
      TextView candidateName,no_of_votes;
      int countvotes;
      public HolderCandidate(@NonNull View itemView) {
          super(itemView);
          candidateImage = itemView.findViewById(R.id.candidateImage);
          candidateName = itemView.findViewById(R.id.candidateName);
          no_of_votes = itemView.findViewById(R.id.no_of_votes);

      }
  }
}
