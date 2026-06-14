package com.intern.todoapp.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.security.SecureRandom;

/** Simple per-device session — stores a random session token + the userId. */
public class SessionManager {
    private static final String PREF = "auth_session";
    private static final String K_USER_ID = "uid";
    private static final String K_EMAIL = "email";
    private static final String K_TOKEN = "token";

    private final SharedPreferences sp;
    public SessionManager(Context c) {
        sp = c.getApplicationContext().getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    public void login(long uid, String email) {
        byte[] tok = new byte[24];
        new SecureRandom().nextBytes(tok);
        StringBuilder sb = new StringBuilder();
        for (byte b : tok) sb.append(String.format("%02x", b));
        sp.edit()
                .putLong(K_USER_ID, uid)
                .putString(K_EMAIL, email)
                .putString(K_TOKEN, sb.toString())
                .apply();
    }

    public void logout() { sp.edit().clear().apply(); }
    public boolean isLoggedIn() { return sp.contains(K_USER_ID) && sp.contains(K_TOKEN); }
    public long userId() { return sp.getLong(K_USER_ID, -1); }
    public String email() { return sp.getString(K_EMAIL, ""); }
}
