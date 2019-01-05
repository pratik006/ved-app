package com.prapps.ved.ebook.activities.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prapps.ved.dto.KeyValue;
import com.prapps.ved.ebook.R;

import java.util.List;


public class SutraCommentaryAdapter extends RecyclerView.Adapter<SutraCommentaryAdapter.SutraCommentarylView> {

    private final List<KeyValue<String>> commentaries;

    public SutraCommentaryAdapter(List<KeyValue<String>> commentaries) {
        this.commentaries = commentaries;
    }

    @NonNull
    @Override
    public SutraCommentarylView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.sutra_commentaries_layout, viewGroup, false);

        return new SutraCommentarylView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SutraCommentarylView sutraViewHolder, int position) {
        KeyValue<String> kv = commentaries.get(position);
        SpannableString content = new SpannableString(kv.getKey());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        sutraViewHolder.txtName.setText(content);
        sutraViewHolder.txtContent.setText(kv.getValue());
    }

    @Override
    public int getItemCount() {
        return commentaries.size();
    }

    public class SutraCommentarylView extends RecyclerView.ViewHolder {
        TextView txtName;
        TextView txtContent;

        public SutraCommentarylView(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.commentatorName);
            txtContent = itemView.findViewById(R.id.commentary);
        }
    }
}
