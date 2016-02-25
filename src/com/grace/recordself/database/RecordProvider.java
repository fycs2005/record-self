package com.grace.recordself.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import com.grace.recordself.RecordConstant;

/**
 * Created by fengyi on 16/2/25.
 */
public class RecordProvider extends SQLiteContentProvider {

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        //sUriMatcher.addURI(RecordConstant.AUTHORITY, "");
        //TODO add urimatch
    }

    @Override
    protected SQLiteOpenHelper getOpenHelper(Context context) {
        return DBHelper.getInstance(context);
    }

    @Override
    protected Uri insertInTransaction(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    protected int updateInTransaction(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    protected int deleteInTransaction(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }
}
