package com.prapps.ved.ebook.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.prapps.ved.dto.ScriptType;

import java.util.Collections;
import java.util.Set;

public abstract class BaseActivity extends AppCompatActivity {

    public static final String SHARED_PREF = "sharedPrefs";
    public static final String PRIMARY_SCRIPT = "primaryLang";
    public static final String COMMENTATORS = "commentators";

    protected ScriptType primaryScript;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(getTag(), "starting "+getTag());
        super.onCreate(savedInstanceState);
    }

    protected String getSharedPref(String code, String key) {
        SharedPreferences pref = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        String value = pref.getString(code + "/" + key, null);
        return value;
    }

    protected Set<String> getSharedPrefSet(String code, String key) {
        SharedPreferences pref = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        Set<String> value = pref.getStringSet(code + "/" + key, Collections.emptySet());
        Log.d("BaseActivity", "Retrieve - "+code + "/" + key + " : " + value);
        return value;
    }

    protected void saveSharedPref(String code, String key, String value) {
        SharedPreferences pref = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor =  pref.edit();
        editor.putString(code + "/" + key, value);
        Log.d("BaseActivity", "Save : "+code + "/" + key + " : " + value);
        editor.apply();
    }

    protected void saveSharedPrefSet(String code, String key, Set<String> value) {
        SharedPreferences pref = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor =  pref.edit();
        editor.putStringSet(code + "/" + key, value);
        Log.d("BaseActivity", "Save : "+code + "/" + key + " : " + value);
        editor.apply();
    }

    protected String getTag() {
        return getClass().getSimpleName();
    }

    protected void debug(String msg) {
        Log.d(getTag(), msg);
    }
}
