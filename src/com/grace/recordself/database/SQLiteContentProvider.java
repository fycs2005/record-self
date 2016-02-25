package com.grace.recordself.database;

import android.content.*;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteTransactionListener;
import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by fengyi on 16/2/23.
 */
public abstract class SQLiteContentProvider extends ContentProvider implements SQLiteTransactionListener{

    private static final String TAG = "SQLiteContentProvider";

    private static final int SLEEP_AFTER_YIELD_DELAY = 4000;

    private SQLiteOpenHelper mOpenHelper;
    protected SQLiteDatabase mDb;

    private final ThreadLocal<Boolean> mApplyingBatch = new ThreadLocal<Boolean>();

    @Override
    public boolean onCreate() {
        mOpenHelper = getOpenHelper(getContext());
        return true;
    }

    protected abstract SQLiteOpenHelper getOpenHelper(Context context);

    /**
     * The equivalent of the {@link #insert} method, but invoked within a transaction.
     */
    protected abstract Uri insertInTransaction(Uri uri, ContentValues values);

    /**
     * The equivalent of the {@link #update} method, but invoked within a transaction.
     */
    protected abstract int updateInTransaction(Uri uri, ContentValues values, String selection,
                                               String[] selectionArgs);

    /**
     * The equivalent of the {@link #delete} method, but invoked within a transaction.
     */
    protected abstract int deleteInTransaction(Uri uri, String selection, String[] selectionArgs);

    private boolean applyingBatch() {
        return mApplyingBatch.get() != null && mApplyingBatch.get();
    }

    @Override
    public void onBegin() {
        onBeginTransaction();
    }

    @Override
    public void onCommit() {
        beforeTransactionCommit();
    }

    @Override
    public void onRollback() {

    }

    protected void onBeginTransaction() {
    }

    protected void beforeTransactionCommit() {
    }

    protected void onEndTransaction() {

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri result = null;
        boolean applyingBatch = applyingBatch();
        if (!applyingBatch) {
            mDb = mOpenHelper.getWritableDatabase();
            mDb.beginTransactionWithListener(this);
            try {
                result = insertInTransaction(uri, values);
                if (result != null) {
                    //TODO notifyChange
                }
                mDb.setTransactionSuccessful();
            } finally {
                mDb.endTransaction();
            }

            onEndTransaction();
        } else {
            result = insertInTransaction(uri, values);
            if (result != null) {
                //TODO notifyChange
            }
        }
        return result;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        boolean applyingBatch = applyingBatch();
        if (!applyingBatch) {
            mDb = mOpenHelper.getWritableDatabase();
            mDb.beginTransactionWithListener(this);
            try {
                count = updateInTransaction(uri, values, selection, selectionArgs);
                if (count > 0) {
                    //TODO notifyChange
                }
                mDb.setTransactionSuccessful();
            } finally {
                mDb.endTransaction();
            }

            onEndTransaction();
        } else {
            count = updateInTransaction(uri, values, selection, selectionArgs);
            if (count > 0) {
                //TODO notifyChange
            }
        }

        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        boolean applyingBatch = applyingBatch();
        if (!applyingBatch) {
            mDb = mOpenHelper.getWritableDatabase();
            mDb.beginTransactionWithListener(this);
            try {
                count = deleteInTransaction(uri, selection, selectionArgs);
                if (count > 0) {
                    //TODO notifyChange
                }
                mDb.setTransactionSuccessful();
            } finally {
                mDb.endTransaction();
            }

            onEndTransaction();
        } else {
            count = deleteInTransaction(uri, selection, selectionArgs);
            if (count > 0) {
                //TODO notifyChange
            }
        }
        return count;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        int numValues = values.length;
        mDb = mOpenHelper.getWritableDatabase();
        mDb.beginTransactionWithListener(this);
        try {
            for (int i = 0; i < numValues; i++) {
                Uri result = insertInTransaction(uri, values[i]);
                if (result != null) {
                    //TODO notifyChange
                }
                mDb.yieldIfContendedSafely();
            }
            mDb.setTransactionSuccessful();
        } finally {
            mDb.endTransaction();
        }

        onEndTransaction();
        return numValues;
    }

    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
            final int numOperations = operations.size();
            if (numOperations == 0) {
                return new ContentProviderResult[0];
            }
            mDb = mOpenHelper.getWritableDatabase();
            mDb.beginTransactionWithListener(this);
            try {
                mApplyingBatch.set(true);
                final ContentProviderResult[] results = new ContentProviderResult[numOperations];
                for (int i = 0; i < numOperations; i++) {
                    final ContentProviderOperation operation = operations.get(i);
                    if (i > 0 && operation.isYieldAllowed()) {
                        mDb.yieldIfContendedSafely(SLEEP_AFTER_YIELD_DELAY);
                    }
                    results[i] = operation.apply(this, results, i);
                }
                mDb.setTransactionSuccessful();
                return results;
            } finally {
                mApplyingBatch.set(false);
                mDb.endTransaction();
                onEndTransaction();
            }
    }
}
