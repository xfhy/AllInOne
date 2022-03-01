package com.xfhy.allinone.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.xfhy.library.ext.log

/**
 * @author : xfhy
 * Create time : 2022/2/28 9:29 下午
 * Description :
 */
class BookDatabaseHelper(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?, version: Int) :
    SQLiteOpenHelper(context, name, factory, version) {

    companion object {
        const val CREATE_BOOK = """
            create table Book(id integer primary key autoincrement, author text, price real, pages integer, name text)
        """
        const val CREATE_CATEGORY = """
            create table Category(id integer primary key autoincrement,category_name text,category_code integer)
        """
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_BOOK)
        db?.execSQL(CREATE_CATEGORY)
        log("Create succeeded")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        log("onCreate onUpgrade")
        db?.execSQL("drop table if exists book")
        db?.execSQL("drop table if exists category")
        onCreate(db)
    }
}