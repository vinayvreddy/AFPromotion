package com.promotion.afpromotion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class PromotionHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PromotionsDb";
    private static final String TABLE_PROMOTION = "promotions";
    private static final String KEY_ID = "id";
    private static final String TITLE = "title";
    private static final String BUTTON_TARGET = "button_target";
    private static final String BUTTON_TITLE = "button_title";
    private static final String DESCRIPTION = "description";
    private static final String FOOTER = "footer";

    public PromotionHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PROMOTION + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + TITLE + " TEXT,"
                + BUTTON_TARGET + " TEXT," +BUTTON_TITLE + " TEXT,"+DESCRIPTION + " TEXT,"+FOOTER + " TEXT"+ ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROMOTION);
        onCreate(db);
    }

    void addPromotion(Promotions promotion) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TITLE, promotion.getTitle());
        values.put(BUTTON_TARGET, promotion.getButtonTarget());
        values.put(BUTTON_TITLE, promotion.getButtonTitle());
        values.put(DESCRIPTION, promotion.getDescription());
        if(promotion.getFooter()==null)
        values.put(FOOTER, "");
        else
            values.put(FOOTER, promotion.getFooter());
        long i =db.insert(TABLE_PROMOTION, null, values);
        db.close();
    }

    public ArrayList<Promotions> getAllPromotions() {
        ArrayList<Promotions> promotionList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_PROMOTION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Promotions promotion = new Promotions();
                promotion.setTitle(cursor.getString(1));
                promotion.setButtonTarget(cursor.getString(2));
                promotion.setButtonTitle(cursor.getString(3));
                promotion.setDescription(cursor.getString(4));
                promotion.setFooter(cursor.getString(5));
                promotionList.add(promotion);
            } while (cursor.moveToNext());
        }
        return promotionList;
    }
}
