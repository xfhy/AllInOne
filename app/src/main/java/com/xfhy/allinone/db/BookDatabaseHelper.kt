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
            create table Book(id integer primary key autoincrement, author text, price real, pages integer, name text, category_id integer)
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

        when (oldVersion) {
            1 -> {
                //用户当前版本是1  升到新版本后创建一个新表
                db?.execSQL(CREATE_CATEGORY)

                db?.execSQL("alter table Book add column category_id integer")
            }
            2 -> {
                //用户当前版本是2   升到新版本后需要加一列
                db?.execSQL("alter table Book add column category_id integer")
            }
            else -> {
            }
        }
    }
}