package com.prapps.ved.ebook.activities.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.prapps.ved.dto.Language;
import com.prapps.ved.ebook.R;

import java.util.List;


public class ScriptsAdapter extends RecyclerView.Adapter<ScriptsAdapter.ScriptsViewHolder> {

    private List<Language> scripts;

    public ScriptsAdapter(List<Language> scripts) {
        this.scripts = scripts;
    }

    @NonNull
    @Override
    public ScriptsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.books_layout, viewGroup, false);
        return new ScriptsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScriptsViewHolder sutraViewHolder, int position) {
        Language script = scripts.get(position);
        //sutraViewHolder.primScript.setText(script.getName());
    }

    @Override
    public int getItemCount() {
        return scripts.size();
    }

    public class ScriptsViewHolder extends RecyclerView.ViewHolder {
        Spinner primScript;

        public ScriptsViewHolder(@NonNull View itemView) {
            super(itemView);
            primScript = itemView.findViewById(R.id.primaryLanguage);
        }
    }
}
