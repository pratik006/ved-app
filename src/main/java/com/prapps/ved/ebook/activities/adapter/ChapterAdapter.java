package com.prapps.ved.ebook.activities.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prapps.ved.dto.Chapter;
import com.prapps.ved.ebook.R;

import java.util.List;


public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {

    private final List<Chapter> chapters;

    public ChapterAdapter(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.chapter_layout, viewGroup, false);
        return new ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder chapterViewHolder, int position) {
        chapterViewHolder.txtChapter.setText(chapters.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }

    public class ChapterViewHolder extends RecyclerView.ViewHolder {
        TextView txtChapter;

        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            txtChapter = itemView.findViewById(R.id.txtChapter);
        }
    }
}
