package com.prapps.ved.ebook.activities.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.prapps.ved.ebook.R;

import java.util.List;
import java.util.Set;


public class AvailableCommentatorsAdapter extends RecyclerView.Adapter<AvailableCommentatorsAdapter.AvailableCommentatorView> {

    private final List<String> availableCommentators;
    private Set<Integer> selectedCommentators;

    private static int CHECKED_IMG = R.drawable.ic_check_black_24dp;
    private static int UNCHECKED_IMG = R.drawable.ic_check_box_outline_blank_black_24dp;

    public AvailableCommentatorsAdapter(List<String> availableCommentators, Set<Integer> selectedCommentators) {
        this.availableCommentators = availableCommentators;
        this.selectedCommentators = selectedCommentators;
    }

    public void setSelectedCommentators(Set<Integer> selectedCommentators) {
        this.selectedCommentators = selectedCommentators;
    }

    @NonNull
    @Override
    public AvailableCommentatorView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.available_commentaries_layout, viewGroup, false);
        return new AvailableCommentatorView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvailableCommentatorView availableCommViewHolder, int position) {
        availableCommViewHolder.txtCommentator.setText(availableCommentators.get(position));
        availableCommViewHolder.txtCommentator.setChecked(selectedCommentators.contains(position));
        availableCommViewHolder.txtCommentator.setCheckMarkDrawable(selectedCommentators.contains(position) ? CHECKED_IMG : UNCHECKED_IMG);
    }

    @Override
    public int getItemCount() {
        return availableCommentators.size();
    }

    public class AvailableCommentatorView extends RecyclerView.ViewHolder {
        CheckedTextView txtCommentator;

        public AvailableCommentatorView(@NonNull View itemView) {
            super(itemView);
            txtCommentator = itemView.findViewById(R.id.commentator);
        }
    }
}
