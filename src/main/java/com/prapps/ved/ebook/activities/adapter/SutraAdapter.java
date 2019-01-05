package com.prapps.ved.ebook.activities.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prapps.ved.ebook.R;

import java.util.List;


public class SutraAdapter extends RecyclerView.Adapter<SutraAdapter.SutraView> {

    private final List<String> sutras;

    public SutraAdapter(List<String> sutras) {
        this.sutras = sutras;
    }

    @NonNull
    @Override
    public SutraView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.sutra_layout, viewGroup, false);

        return new SutraView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SutraView sutraViewHolder, int position) {
        sutraViewHolder.txtSutra.setText(sutras.get(position));
    }

    @Override
    public int getItemCount() {
        return sutras.size();
    }

    public class SutraView extends RecyclerView.ViewHolder {
        TextView txtSutra;

        public SutraView(@NonNull View itemView) {
            super(itemView);
            txtSutra = itemView.findViewById(R.id.txtSutra);
        }
    }
}
