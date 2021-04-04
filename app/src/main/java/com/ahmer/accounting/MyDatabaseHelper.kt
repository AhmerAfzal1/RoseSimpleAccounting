package com.ahmer.accounting

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.ahmer.accounting.Constants.Companion.LOG_TAG
import com.ahmer.accounting.model.CustomerProfile

class MyDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME: String = "SimpleAccounting.db"
        private const val DATABASE_VERSION: Int = 1
        private const val TABLE_NAME: String = "Accounting"
        private const val CUSTOMER_ID: String = "ID"
        private const val CUSTOMER_NAME: String = "Name"
        private const val CUSTOMER_GENDER: String = "Gender"
        private const val CUSTOMER_ADDRESS: String = "Address"
        private const val CUSTOMER_CITY: String = "City"
        private const val CUSTOMER_PHONE1: String = "Phone1"
        private const val CUSTOMER_PHONE2: String = "Phone2"
        private const val CUSTOMER_PHONE3: String = "Phone3"
        private const val CUSTOMER_COMMENTS: String = "Comments"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        try {
            val createTable = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                    "$CUSTOMER_ID INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL, " +
                    "$CUSTOMER_NAME TEXT NOT NULL, " +
                    "$CUSTOMER_GENDER VARCHAR(7) NOT NULL, " +
                    "$CUSTOMER_ADDRESS TEXT, " +
                    "$CUSTOMER_CITY TEXT, " +
                    "$CUSTOMER_PHONE1 TEXT, " +
                    "$CUSTOMER_PHONE2 TEXT, " +
                    "$CUSTOMER_PHONE3 TEXT, " +
                    "$CUSTOMER_COMMENTS TEXT, " +
                    "CHECK($CUSTOMER_GENDER IN ('Male', 'Female', 'Unknown'))" +
                    ")"
            Log.v(LOG_TAG, createTable)
            db?.execSQL(createTable)
        } catch (e: SQLiteException) {
            Log.v(LOG_TAG, e.printStackTrace().toString())
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun insertCustomerProfileData(
        name: String, gender: String, address: String?, city: String?, phone1: String?,
        phone2: String?, phone3: String?, comments: String?
    ) {
        val dataValues = ContentValues().apply {
            put(CUSTOMER_NAME, name)
            put(CUSTOMER_GENDER, gender)
            put(CUSTOMER_ADDRESS, address)
            put(CUSTOMER_CITY, city)
            put(CUSTOMER_PHONE1, phone1)
            put(CUSTOMER_PHONE2, phone2)
            put(CUSTOMER_PHONE3, phone3)
            put(CUSTOMER_COMMENTS, comments)
        }
        val writeDatabase: SQLiteDatabase = this.writableDatabase
        writeDatabase.insert(TABLE_NAME, null, dataValues)
        writeDatabase.close()
    }

    fun getCustomerProfileData(): ArrayList<CustomerProfile> {
        val readDatabase: SQLiteDatabase = this.readableDatabase
        val customersList = ArrayList<CustomerProfile>()
        val queryContent = arrayOf<String>(
            CUSTOMER_ID,
            CUSTOMER_NAME,
            CUSTOMER_GENDER,
            CUSTOMER_ADDRESS,
            CUSTOMER_CITY,
            CUSTOMER_PHONE1,
            CUSTOMER_PHONE2,
            CUSTOMER_PHONE3,
            CUSTOMER_COMMENTS
        )
        val cursor: Cursor =
            readDatabase.query(TABLE_NAME, queryContent, null, null, null, null, null)
        try {
            while (cursor.moveToNext()) {
                val customerProfile = CustomerProfile()
                customerProfile.id = cursor.getInt(cursor.getColumnIndexOrThrow(CUSTOMER_ID))
                customerProfile.name = cursor.getString(cursor.getColumnIndexOrThrow(CUSTOMER_NAME))
                customerProfile.gender =
                    cursor.getString(cursor.getColumnIndexOrThrow(CUSTOMER_GENDER))
                customerProfile.address =
                    cursor.getString(cursor.getColumnIndexOrThrow(CUSTOMER_ADDRESS))
                customerProfile.city =
                    cursor.getString(cursor.getColumnIndexOrThrow(CUSTOMER_CITY))
                customerProfile.phone1 =
                    cursor.getString(cursor.getColumnIndexOrThrow(CUSTOMER_PHONE1))
                customerProfile.phone2 =
                    cursor.getString(cursor.getColumnIndexOrThrow(CUSTOMER_PHONE2))
                customerProfile.phone3 =
                    cursor.getString(cursor.getColumnIndexOrThrow(CUSTOMER_PHONE3))
                customerProfile.comment =
                    cursor.getString(cursor.getColumnIndexOrThrow(CUSTOMER_COMMENTS))
                customersList.add(customerProfile)
                val stringBuilder = StringBuilder()
                stringBuilder.append("ID: ")
                    .append(cursor.getInt(cursor.getColumnIndexOrThrow(CUSTOMER_ID)))
                stringBuilder.append("\nName: ")
                    .append(cursor.getString(cursor.getColumnIndexOrThrow(CUSTOMER_NAME)))
                stringBuilder.append("\nGender: ")
                    .append(cursor.getString(cursor.getColumnIndexOrThrow(CUSTOMER_GENDER)))
                stringBuilder.append("\nAddress: ")
                    .append(cursor.getString(cursor.getColumnIndexOrThrow(CUSTOMER_ADDRESS)))
                stringBuilder.append("\nCity: ")
                    .append(cursor.getString(cursor.getColumnIndexOrThrow(CUSTOMER_CITY)))
                stringBuilder.append("\nPhone1: ")
                    .append(cursor.getString(cursor.getColumnIndexOrThrow(CUSTOMER_PHONE1)))
                stringBuilder.append("\nPhone2: ")
                    .append(cursor.getString(cursor.getColumnIndexOrThrow(CUSTOMER_PHONE2)))
                stringBuilder.append("\nPhone3: ")
                    .append(cursor.getString(cursor.getColumnIndexOrThrow(CUSTOMER_PHONE3)))
                stringBuilder.append("\nComments: ")
                    .append(cursor.getString(cursor.getColumnIndexOrThrow(CUSTOMER_COMMENTS)))
                Log.v(LOG_TAG, stringBuilder.toString())
            }
        } catch (e: Exception) {
            Log.v(LOG_TAG, e.printStackTrace().toString())
        } finally {
            cursor.close()
        }

        return customersList
    }
}