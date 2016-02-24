package com.reuworld.reworld.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Arvin.X on 16/2/6.
 */

/**
 *本地数据库操作类，封装必要的数据库操作
 */
public class LocalDBHelper extends SQLiteOpenHelper {



    public LocalDBHelper(Context context,String name,int version){
        super(context,name,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db){

    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldversion, int newVersion){

    }

}
