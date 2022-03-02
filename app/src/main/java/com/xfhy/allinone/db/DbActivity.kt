package com.xfhy.allinone.db

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import com.xfhy.allinone.R
import com.xfhy.library.basekit.activity.TitleBarActivity
import com.xfhy.library.ext.log

/**
 * @author : xfhy
 * Create time : 2022年02月28日21:29:27
 * Description : 数据库
 * 所有数据库操作都应该放入子线程,可能会非常耗时
 */
class DbActivity : TitleBarActivity() {

    private var sqLiteDatabase: SQLiteDatabase? = null

    override fun getThisTitle(): CharSequence {
        return "Smali Test"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_db)
    }

    fun createDb(view: View?) {
        val bookDatabaseHelper = BookDatabaseHelper(this, "BookStore.db", null, 2)
        //第一次调用时会创建数据库  onCreate
        sqLiteDatabase = bookDatabaseHelper.readableDatabase
    }

    fun addData(view: View) {
        val contentValues = ContentValues().apply {
            put("name", "三体1")
            put("author", "刘慈欣")
            put("pages", 513)
            put("price", 23.23)
        }
        sqLiteDatabase?.insert("Book", null, contentValues)

        contentValues.clear()
        contentValues.apply {
            put("name", "三体2")
            put("author", "刘慈欣")
            put("pages", 536)
            put("price", 26.25)
        }
        sqLiteDatabase?.insert("Book", null, contentValues)

        //sql插入语句: INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) VALUES (5, 'David', 27, 'Texas', 85000.00 );


        sqLiteDatabase?.execSQL("insert into Book(name,author,pages,price) values('三体3-死神永生','刘慈欣','312','25.6')")
    }

    fun updateData(view: View) {
//        val contentValues = ContentValues().apply {
//            put("price", 10.56)
//        }
//        sqLiteDatabase?.update("Book", contentValues, "name = ?", arrayOf("三体1"))

        sqLiteDatabase?.execSQL("update Book set price = 27.8 where name='三体3-死神永生'")
    }

    fun deleteData(view: View) {
//        sqLiteDatabase?.delete("Book", "name = ?", arrayOf("三体1"))
        sqLiteDatabase?.execSQL("delete from Book where name='三体1'")
    }

    fun queryData(view: View) {
//        val cursor = sqLiteDatabase?.query("Book", null, null, null, null, null, null, null)
//        showData(cursor)
//        cursor?.close()

        val rawQueryCursor = sqLiteDatabase?.rawQuery("select * from Book where name = '三体1'", null)
        showData(rawQueryCursor)
        rawQueryCursor?.close()
    }

    private fun showData(cursor: Cursor?) {
        cursor?.let {
            if (cursor.moveToFirst()) {
                do {
                    val name = cursor.getString(cursor.getColumnIndex("name"))
                    val author = cursor.getString(cursor.getColumnIndex("author"))
                    val pages = cursor.getInt(cursor.getColumnIndex("pages"))
                    val price = cursor.getDouble(cursor.getColumnIndex("price"))
                    log("name = $name author=$author pages = $pages  price=$price")
                } while (cursor.moveToNext())
            }
        }
    }


    fun transaction(view: View) {
        sqLiteDatabase?.beginTransaction()
        try {
            sqLiteDatabase?.delete("Book", null, null)
            if (true) {
                throw NullPointerException()
            }
            sqLiteDatabase?.execSQL("insert into Book(name,author,pages,price) values('三体3-死神永生','刘慈欣','312','25.6')")
            sqLiteDatabase?.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            sqLiteDatabase?.endTransaction()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sqLiteDatabase?.close()
    }
}