package com.prapps.ved.ebook;

import android.content.Context;
import android.util.Log;

import com.prapps.ved.ebook.exception.VedException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileCache {
    private static final String TAG = FileCache.class.getName();
    private static final String EXTN = ".dat";

    public static <T> T readObject(Context ctx, String code, String fname , Class<T> clazz) throws VedException {
        File file = new File(ctx.getCacheDir().getAbsolutePath() + "/" + code + "/" + fname + EXTN);
        return read(file, clazz);
    }

    public static void writeObject(Context ctx, String code, String fname , Object object) throws VedException {
        File appRoot = ctx.getCacheDir();
        File bookDir = new File(appRoot.getAbsolutePath() + "/" + code);
        if (!bookDir.exists()) {
            bookDir.mkdir();
        }
        File file = new File(bookDir.getAbsolutePath() + File.separator + fname + EXTN);
        write(file, object);
    }

    private static <T> T read(File file, Class<T> clazz) throws VedException {
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(is);
            T instance = clazz.cast(ois.readObject());
            Log.d(TAG, file+" : content read successfully");
            ois.close();
            return instance;
        } catch (IOException e) {
            e.printStackTrace();
            throw new VedException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new VedException(e);
        }
    }

    private static void write(File file, Object object) throws VedException {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            ObjectOutputStream fOut = new ObjectOutputStream(os);
            fOut.writeObject(object);
            Log.d(TAG, file+" : content written successfully");
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new VedException(e);
        }
    }
}
