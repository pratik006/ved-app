package com.prapps.ved.ebook.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.prapps.ved.dto.Book;
import com.prapps.ved.ebook.FileCache;
import com.prapps.ved.ebook.R;
import com.prapps.ved.ebook.activities.adapter.BooksAdapter;
import com.prapps.ved.ebook.exception.VedException;
import com.prapps.ved.ebook.rest.RestConnector;

import java.io.IOException;
import java.util.List;

import us.monoid.json.JSONException;

public class MainActivity extends AbstractRecyclerActivity<Book, BooksAdapter> {

    private TextView mTextMessage;
    public List<Book> books;

    public MainActivity() {
        super(BooksAdapter.class, R.layout.activity_main, R.id.booksView);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /* BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);*/

        RetrieveBooksTask task = new RetrieveBooksTask();
        task.execute();

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                        Book book = books.get(position);
                        Intent intent = new Intent(me, ChaptersActivity.class);
                        intent.putExtra("code", book.getCode());
                        intent.putExtra("name", book.getName());
                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                        Log.d("MainActivity", "clicked "+position);
                    }
                })
        );
    }

    public class RetrieveBooksTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... activities) {
            String fname = "books";
            try {
                books = FileCache.readObject(getBaseContext(), null, fname, List.class);
            } catch (IOException | ClassNotFoundException e) {
                //e.printStackTrace();
                try {
                    books = RestConnector.getBooks();
                    adapter = new BooksAdapter(books);
                    updateUI(books);
                } catch (JSONException | IOException e2) {
                    e.printStackTrace();
                } catch (VedException e1) {
                    e1.printStackTrace();
                }
            }
            return null;
        }
    }

}
