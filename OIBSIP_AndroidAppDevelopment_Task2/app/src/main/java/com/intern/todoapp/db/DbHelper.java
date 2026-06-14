package com.intern.todoapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.intern.todoapp.model.Item;
import com.intern.todoapp.util.PasswordHasher;

import java.util.ArrayList;
import java.util.List;

/**
 * SQLite storage.
 *  users(id, email UNIQUE, password_hash)
 *  items(id, user_id, section, title, body, created_at)
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final String DB = "todo.db";
    private static final int V = 1;

    public DbHelper(Context c) { super(c, DB, null, V); }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "email TEXT NOT NULL UNIQUE," +
                "password_hash TEXT NOT NULL)");
        db.execSQL("CREATE TABLE items (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER NOT NULL," +
                "section TEXT NOT NULL," +
                "title TEXT NOT NULL," +
                "body TEXT NOT NULL," +
                "created_at INTEGER NOT NULL," +
                "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE)");
        db.execSQL("CREATE INDEX idx_items_user_section ON items(user_id, section)");
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS items");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    // ---------------- AUTH ----------------

    /** Returns the new userId, or -1 if email already exists. */
    public long signup(String email, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("email", email.trim().toLowerCase());
        v.put("password_hash", PasswordHasher.hash(password));
        try {
            return db.insertOrThrow("users", null, v);
        } catch (Exception e) {
            return -1;
        }
    }

    /** Returns userId on success, -1 if bad credentials. */
    public long login(String email, String password) {
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor c = db.query("users", new String[]{"id", "password_hash"},
                "email = ?", new String[]{email.trim().toLowerCase()},
                null, null, null)) {
            if (c.moveToFirst()) {
                long id = c.getLong(0);
                String hash = c.getString(1);
                if (PasswordHasher.verify(password, hash)) return id;
            }
        }
        return -1;
    }

    // ---------------- ITEMS ----------------

    public long insertItem(long userId, String section, String title, String body) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("user_id", userId);
        v.put("section", section);
        v.put("title", title);
        v.put("body", body);
        v.put("created_at", System.currentTimeMillis());
        return db.insert("items", null, v);
    }

    public void updateItem(long id, String title, String body) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("title", title);
        v.put("body", body);
        db.update("items", v, "id = ?", new String[]{String.valueOf(id)});
    }

    public void deleteItem(long id) {
        getWritableDatabase().delete("items", "id = ?", new String[]{String.valueOf(id)});
    }

    public List<Item> listItems(long userId, String section) {
        List<Item> out = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor c = db.query("items",
                new String[]{"id", "user_id", "section", "title", "body", "created_at"},
                "user_id = ? AND section = ?",
                new String[]{String.valueOf(userId), section},
                null, null, "created_at DESC")) {
            while (c.moveToNext()) {
                out.add(new Item(c.getLong(0), c.getLong(1), c.getString(2),
                        c.getString(3), c.getString(4), c.getLong(5)));
            }
        }
        return out;
    }
}
