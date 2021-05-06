package com.example.quizzer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizzer.Activities.QuestionBank;
import com.example.quizzer.Model.LanguageModel;
import com.example.quizzer.R;

import java.util.ArrayList;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder> {

    Context context;
    ArrayList<LanguageModel> languageModels;

    public LanguageAdapter(Context context, ArrayList<LanguageModel> languageModels) {
        this.context = context;
        this.languageModels = languageModels;
    }

    @NonNull
    @Override
    public LanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_layout, parent, false);
        return new LanguageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final LanguageViewHolder holder, int position) {

        holder.LanguageTextView.setText(languageModels.get(position).getLanguageName());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), QuestionBank.class);
                intent.putExtra("LanguageName", holder.LanguageTextView.getText().toString());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return languageModels.size();
    }

    class LanguageViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView LanguageTextView;

        public LanguageViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            LanguageTextView = itemView.findViewById(R.id.languageText);
        }
    }

}
