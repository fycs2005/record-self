package com.grace.recordself.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.grace.recordself.database.content.*;

/**
 * Created by fengyi on 16/2/9.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final int MIN_DB_VERSION = 5;
    private static final int DB_VERSION = 5;
    private static final String DB_NAME = "record.db";
    private Context mContext;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        dropTables(db);
        bootstrapDB(db);

    }

    private void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Account.TABLE_NAME + " IF NOT EXIST ()");
    }

    private void dropTables(SQLiteDatabase db) {
        db.execSQL("DROP TABLE " + Account.TABLE_NAME + " IF EXIST");
        db.execSQL("DROP TABLE " + Folder.TABLE_NAME + " IF EXIST");
        db.execSQL("DROP TABLE " + Body.TABLE_NAME + " IF EXIST");
        db.execSQL("DROP TABLE " + Message.TABLE_NAME + " IF EXIST");
        db.execSQL("DROP TABLE " + Score.TABLE_NAME + " IF EXIST");

    }

    private void createScoreTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Score.TABLE_NAME + " ("
                + BaseContent.COLUMN_ID + " LONG, "
                + Score.COLUMN_SERVER_ID + " TEXT PRIMARY KEY, "
                + Score.COLUMN_CURRENT + " LONG DEFAULT 0, "
                + Score.COLUMN_TOTAL + " LONG DEFAULT 0,  "
                + Score.COLUMN_LEVEL_TYPE + " INTEGER DEFAULT 0, "
                + BaseContent.COLUMN_CREATE_TIME + " LONG DEFAULT 0, "
                + BaseContent.COLUMN_MODIFY_TIME + " LONG DEFAULT 0);");
    }

    private void createHeadPicTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + HeadPic.TABLE_NAME + " ("
                + BaseContent.COLUMN_ID + " LONG PRIMARY KEY, "
                + HeadPic.COLUMN_ACCOUNT_ID + " LONG, "
                + HeadPic.COLUMN_PIC_URL + " TEXT, "
                + HeadPic.COLUMN_TIME_STAMP + " LONG, "
                + BaseContent.COLUMN_CREATE_TIME + " LONG DEFAULT 0, "
                + BaseContent.COLUMN_MODIFY_TIME + " LONG DEFAULT 0);");
    }

    private void createAccountTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Account.TABLE_NAME + " ("
                + BaseContent.COLUMN_ID + " LONG, "
                + Account.COLUMN_SERVER_ID + " TEXT PRIMARY KEY, "
                + Account.COLUMN_DISPLAY_NAME + " TEXT, "
                + Account.COLUMN_LOGIN_NAME + " TEXT, "
                + Account.COLUMN_PASSWORD + " TEXT, "
                + Account.COLUMN_GENDER + " INTEGER, "
                + Account.COLUMN_BIRTHDAY + " LONG DEFAULT 0, "
                + Account.COLUMN_SIGN + " TEXT, "
                + Account.COLUMN_HEAD_PIC_ID + " LONG DEFAULT 0, "
                + Account.COLUMN_SCORE_ID + " LONG DEFAULT 0, "
                + Account.COLUMN_EMAIL_ADDRESS + " TEXT, "
                + Account.COLUMN_PHONE + " TEXT, "
                + Account.COLUMN_LOCAL_ID + " TEXT, "
                + Account.COLUMN_COUNTRY + " TEXT, "
                + Account.COLUMN_PROVINCE + " TEXT, "
                + Account.COLUMN_CITY + " TEXT, "
                + BaseContent.COLUMN_CREATE_TIME + " LONG DEFAULT 0, "
                + BaseContent.COLUMN_MODIFY_TIME + " LONG DEFAULT 0);");
    }

    private void createMessageTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Message.TABLE_NAME + " ("
                + BaseContent.COLUMN_ID + " LONG, "
                + Message.COLUMN_SERVER_ID + " TEXT PRIMARY KEY, "
                + Message.COLUMN_BODY_ID + " LONG DEFAULT 0, "
                + Message.COLUMN_PIC_TAG + " INTEGER DEFAULT 0, "
                + Message.COLUMN_READ + " INTEGER DEFAULT 0, "
                + Message.COLUMN_FAVORITE + " INTEGER DEFAULT 0, "
                + Message.COLUMN_LOCKED + " INTEGER DEFAULT 0, "
                + Message.COLUMN_SUMMARY + " TEXT, "
                + Message.COLUMN_ORDER + " TEXT, "
                + Message.COLUMN_DATE + " LONG DEFAULT 0, "
                + BaseContent.COLUMN_CREATE_TIME + " LONG DEFAULT 0, "
                + BaseContent.COLUMN_MODIFY_TIME + " LONG DEFAULT 0);");
    }

    private void createBodyTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Body.TABLE_NAME + " ("
                + BaseContent.COLUMN_ID + " LONG, "
                + Body.COLUMN_SOURCE_ID + " LONG DEFAULT 0, "
                + Body.COLUMN_SERVER_ID + " TEXT PRIMARY KEY, "
                + Body.COLUMN_TITLE + " TEXT, "
                + Body.COLUMN_CONTENT + " TEXT, "
                + Body.COLUMN_DATE + " LONG DEFAULT 0, "
                + BaseContent.COLUMN_CREATE_TIME + " LONG DEFAULT 0, "
                + BaseContent.COLUMN_MODIFY_TIME + " LONG DEFAULT 0);");
    }

    private void createFolderTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Folder.TABLE_NAME + " ("
                + BaseContent.COLUMN_ID + " LONG, "
                + Folder.COLUMN_SERVER_ID + " TEXT PRIMARY KEY, "
                + Folder.COLUMN_PARENT_ID + " LONG DEFAULT 0, "
                + Folder.COLUMN_PATH + " TEXT, "
                + Folder.COLUMN_SERVER_NAME + " TEXT, "
                + Folder.COLUMN_DISPLAY_NAME + " TEXT, "
                + Folder.COLUMN_TOTAL_COUNT + " INTEGER DEFAULT 0, "
                + Folder.COLUMN_UNREAD_COUNT + " INTEGER DEFAULT 0, "
                + Folder.COLUMN_ABILITIES + " INTEGER DEFAULT 0, "
                + BaseContent.COLUMN_CREATE_TIME + " LONG DEFAULT 0, "
                + BaseContent.COLUMN_MODIFY_TIME + " LONG DEFAULT 0);");
    }

    private void bootstrapDB(SQLiteDatabase db) {
        createScoreTable(db);

        createHeadPicTable(db);

        createAccountTable(db);

        createMessageTable(db);

        createBodyTable(db);

        createFolderTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < MIN_DB_VERSION) {
            dropTables(db);
            bootstrapDB(db);
            return;
        }
    }
}
