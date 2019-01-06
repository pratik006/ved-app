package com.prapps.ved.ebook.rest;

import android.util.Log;

import com.prapps.ved.dto.Book;
import com.prapps.ved.dto.Chapter;
import com.prapps.ved.dto.KeyValue;
import com.prapps.ved.ebook.exception.VedException;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import us.monoid.web.JSONResource;
import us.monoid.web.Resty;

public class RestConnector {

    private static final String BASE_URL = "https://vedsangraha-187514.firebaseio.com/ved/";
    private static final String AVAILABLE_SCRIPTS_URL = BASE_URL + "gita/availableLanguages.json";
    private static final String AVAILABLE_COMMENTATORS_URL = BASE_URL + "gita/availableCommentaries.json";
    private static final String BOOKS_URL = BASE_URL + "books.json";
    private static final String SUTRA_URL = BASE_URL + "{0}/sutras/{1}/{2}.json";
    private static final String COMMENTARY_URL = BASE_URL + "{0}/commentaries/{1}/{2}/{3}/{4}.json";
    private static Resty r = new Resty();

    public static List<KeyValue<String>> getScripts(String code) throws VedException {
        List<KeyValue<String>> scripts;
        try {
            JSONArray arr = r.json(AVAILABLE_SCRIPTS_URL).array();
            scripts = new ArrayList<>(arr.length());
            for (int i=0;i<arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                scripts.add(new KeyValue(obj.getString("code"), obj.getString("name")));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new VedException(e);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new VedException(e);
        }

        return scripts;
    }

    public static List<KeyValue<String>> getCommentaries(String code) throws VedException {
        List<KeyValue<String>> commentaries;
        try {
            JSONArray arr = r.json(AVAILABLE_COMMENTATORS_URL).array();
            commentaries = new ArrayList<>(arr.length());
            for (int i=0;i<arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                commentaries.add(new KeyValue(obj.getString("code"), obj.getString("commentator")));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new VedException(e);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new VedException(e);
        }

        return commentaries;
    }

    public static List<Book> getBooks() throws IOException, JSONException {
        List<Book> books;
        JSONResource resource = r.json(BOOKS_URL);
        Log.i("RESTConnector", "resource: "+resource.array().get(0));
        int len = resource.array().length();
        books = new ArrayList<>(len);
        for (int i=0;i<len; i++) {
            JSONObject obj = resource.array().getJSONObject(i);
            Book book = mapBook(obj);
            books.add(book);
        }
        return books;
    }

    public static List<Chapter> getBook(String code) throws VedException {
        List<Chapter> chapters;
        String finalUrl = BASE_URL + code + "/chapter-summary.json";
        Log.d("RestConnector", "finalUrl: " + finalUrl);
        try {
            JSONArray arr = r.json(finalUrl).array();
            chapters = new ArrayList<>(arr.length());
            for (int i=0;i<arr.length(); i++) {
                Chapter ch = new Chapter();
                JSONObject obj = arr.getJSONObject(i);
                Log.d("RestConnector", "obj: "+obj);

                ch.setChapterNo(obj.getInt("chapterNo"));
                ch.setName(obj.getString("name"));
                ch.setContent(obj.getString("content"));
                ch.setHeadline(obj.getString("headline"));
                chapters.add(ch);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new VedException(e);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new VedException(e);
        }


        Log.d("RestConnector", "chapters: " + chapters);
        return chapters;
    }

    public static List<String> getSutras(String code, String lang, int chapterNo) throws VedException {
        String finalUrl = MessageFormat.format(SUTRA_URL, code, lang, chapterNo);
        Log.d("RestConnector", "finalUrl: " + finalUrl);
        JSONArray arr = null;
        List<String> sutras = null;
        try {
            arr = r.json(finalUrl).array();
             sutras = new ArrayList<>(arr.length()-1);
            for (int i=1;i<=arr.length()-1; i++) {
                sutras.add(arr.getString(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new VedException(e);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new VedException(e);
        }

        return sutras;
    }

    public static String getCommentary(String code, String lang, int chapterNo, int sutraNo, String commentator) throws VedException {
        String finalUrl = MessageFormat.format(COMMENTARY_URL, code, commentator, lang, chapterNo, sutraNo);
        try {
            return r.text(finalUrl).toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new VedException(e);
        }
    }

    private static Book mapBook(JSONObject obj) throws JSONException {
        Book book = new Book();
        book.setName(obj.getString("name"));
        book.setAuthorName(obj.getString("authorName"));
        book.setId(obj.getLong("id"));
        book.setCode(obj.getString("code"));
        book.setPreviewUrl("https://vedsangraha-187514.firebaseapp.com/images/"+obj.getString("previewUrl"));
        return book;
    }

    public static void main(String[] args) throws VedException {
        //System.out.println(getCommentary("gita", "dv",1,1,"DrSSankaranarayan"));
        System.out.println(getCommentaries("gita"));
    }
}
