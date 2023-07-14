package com.example.dietapp.data;

import android.content.Context;
import android.os.Debug;
import android.util.Log;

import com.example.dietapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Controller {

    SqlData db;

    public Controller(Context context) {
        String dbPath = context.getApplicationInfo().dataDir + "/food.db";
        checkForDatabase(context, dbPath);
        db = SqlData.getInstance(dbPath);
    }

    public Ingredient[] searchIngredients(String searchText, int limit) {
        return db.searchIngredients(searchText, limit);
    }

    private void checkForDatabase(Context context, String dbPath) {
        File file = new File(dbPath);
        if(!file.exists()) {
            Log.i("DATABASE", "Move Database to app directory");
            InputStream in = context.getResources().openRawResource(R.raw.food);
            FileOutputStream out = null;

            try {
                out = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int read;
                while((read = in.read(buffer)) != -1){
                    out.write(buffer, 0, read);
                }
                out.close();
            } catch(IOException e) {
                Log.e("tag", "Failed to copy asset file: food.db", e);

            }
        }
    }
}
