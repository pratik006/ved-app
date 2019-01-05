package com.prapps.ved.ebook.activities.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.prapps.ved.dto.Book;
import com.prapps.ved.ebook.R;

import java.io.InputStream;
import java.util.List;


public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.SutraViewHolder> {

    private List<Book> books;

    public BooksAdapter(List<Book> books) {
        this.books = books;
    }

    @NonNull
    @Override
    public SutraViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.books_layout, viewGroup, false);
       /* view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("BooksAdapter", "clicked "+view.get);
            }
        });*/
        return new SutraViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SutraViewHolder sutraViewHolder, int position) {
        Book book = books.get(position);
        sutraViewHolder.txtSutra.setText(book.getName());
        Log.d("BooksAdapter", "image url: "+book.getPreviewUrl());
        new DownloadImageTask(sutraViewHolder.imgIcon)
                .execute(book.getPreviewUrl());
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class SutraViewHolder extends RecyclerView.ViewHolder {

        ImageView imgIcon;
        TextView txtSutra;

        public SutraViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.imgSutra);
            txtSutra = itemView.findViewById(R.id.txtSutra);
        }

        public  String getSutra() {
            return txtSutra.getText().toString();
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
