package com.ahmer.accounting.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import com.ahmer.accounting.model.Transactions
import com.ahmer.accounting.model.UserProfile
import com.google.firebase.crashlytics.FirebaseCrashlytics

class MyDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION) {

    private val mContext = context

    private fun contentValuesUser(
        userProfile: UserProfile,
        isCreated: Boolean = true
    ): ContentValues {
        return ContentValues().apply {
            put(Constants.UserColumn.NAME, userProfile.name)
            put(Constants.UserColumn.GENDER, userProfile.gender)
            put(Constants.UserColumn.ADDRESS, userProfile.address)
            put(Constants.UserColumn.CITY, userProfile.city)
            put(Constants.UserColumn.PHONE1, userProfile.phone1)
            put(Constants.UserColumn.PHONE2, userProfile.phone2)
            put(Constants.UserColumn.EMAIL, userProfile.email)
            put(Constants.UserColumn.COMMENTS, userProfile.comment)
            if (isCreated) {
                put(Constants.UserColumn.CREATED_ON, userProfile.created)
            } else {
                put(Constants.UserColumn.LAST_MODIFIED, userProfile.modified)
            }
        }
    }

    private fun contentValuesTransaction(
        transactions: Transactions,
        isModified: Boolean = false
    ): ContentValues {
        return ContentValues().apply {
            put(Constants.TranColumn.USER_ID, transactions.userId)
            put(Constants.TranColumn.DATE, transactions.date)
            put(Constants.TranColumn.DESCRIPTION, transactions.description)
            put(Constants.TranColumn.CREDIT, transactions.credit)
            put(Constants.TranColumn.DEBIT, transactions.debit)
            put(Constants.TranColumn.BALANCE, transactions.balance)
            if (isModified) {
                put(Constants.TranColumn.LAST_MODIFIED, transactions.modified)
            } else {
                put(Constants.TranColumn.CREATED_ON, transactions.created)
            }
        }

    }

    override fun onConfigure(db: SQLiteDatabase?) {
        super.onConfigure(db)
        db?.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase?) {
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        try {
            val userTable = "CREATE TABLE IF NOT EXISTS ${Constants.UserColumn.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL, " +
                    "${Constants.UserColumn.NAME} TEXT NOT NULL, " +
                    "${Constants.UserColumn.GENDER} VARCHAR(7) NOT NULL, " +
                    "${Constants.UserColumn.ADDRESS} TEXT, " +
                    "${Constants.UserColumn.CITY} TEXT, " +
                    "${Constants.UserColumn.PHONE1} TEXT, " +
                    "${Constants.UserColumn.PHONE2} TEXT, " +
                    "${Constants.UserColumn.EMAIL} TEXT, " +
                    "${Constants.UserColumn.COMMENTS} TEXT, " +
                    "${Constants.UserColumn.CREATED_ON} TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, " +
                    "${Constants.UserColumn.LAST_MODIFIED} TIMESTAMP DEFAULT \"\", " +
                    "CHECK(${Constants.UserColumn.GENDER} IN ('Male', 'Female', 'Unknown'))" +
                    ");"
            val tranTable = "CREATE TABLE IF NOT EXISTS ${Constants.TranColumn.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL, " +
                    "${Constants.TranColumn.USER_ID} INTEGER, " +
                    "${Constants.TranColumn.DATE} DATE DEFAULT CURRENT_DATE NOT NULL, " +
                    "${Constants.TranColumn.DESCRIPTION} TEXT NOT NULL, " +
                    "${Constants.TranColumn.CREDIT} REAL, " +
                    "${Constants.TranColumn.DEBIT} REAL, " +
                    "${Constants.TranColumn.BALANCE} REAL, " +
                    "${Constants.TranColumn.CREATED_ON} TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, " +
                    "${Constants.TranColumn.LAST_MODIFIED} TIMESTAMP DEFAULT \"\", " +
                    "FOREIGN KEY (${Constants.TranColumn.USER_ID}) REFERENCES ${Constants.UserColumn.TABLE_NAME}(${BaseColumns._ID})" +
                    ");"
            db?.execSQL("PRAGMA foreign_keys = ON;")
            db?.execSQL(userTable)
            Log.v(Constants.LOG_TAG, userTable)
            db?.execSQL(tranTable)
            Log.v(Constants.LOG_TAG, tranTable)
        } catch (e: SQLiteException) {
            Log.e(Constants.LOG_TAG, e.message, e)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${Constants.UserColumn.TABLE_NAME};")
        db?.execSQL("DROP TABLE IF EXISTS ${Constants.TranColumn.TABLE_NAME};")
        onCreate(db)
    }

    fun insertUserProfileData(userProfile: UserProfile): Boolean {
        val database: SQLiteDatabase = this.writableDatabase
        database.beginTransaction()
        var result: Long = 0.toLong()
        try {
            result = database.insert(
                Constants.UserColumn.TABLE_NAME,
                null,
                contentValuesUser(userProfile, true)
            )
        } catch (e: Exception) {
            Log.e(Constants.LOG_TAG, e.message, e)
            FirebaseCrashlytics.getInstance().recordException(e)
        } finally {
            database.setTransactionSuccessful()
            database.endTransaction()
            database.close()
        }
        //mContext.contentResolver.notifyChange(Constants.UserColumn.USER_TABLE_URI, null)
        return result != (-1).toLong() // If -1 return it means not successfully inserted
    }

    fun updateUserProfileData(userProfile: UserProfile, id: Int): Boolean {
        val updateWriteDatabase: SQLiteDatabase = this.writableDatabase
        try {
            updateWriteDatabase.update(
                Constants.UserColumn.TABLE_NAME,
                contentValuesUser(userProfile, false),
                "${BaseColumns._ID} = ?",
                arrayOf(id.toString())
            )
            return true
        } catch (e: Exception) {
            Log.e(Constants.LOG_TAG, e.message, e)
            FirebaseCrashlytics.getInstance().recordException(e)
        } finally {
            updateWriteDatabase.close()
        }
        return false
    }

    fun getAllUserProfileData(): Cursor {
        val database: SQLiteDatabase = this.readableDatabase
        val projection = arrayOf(
            BaseColumns._ID,
            Constants.UserColumn.NAME,
            Constants.UserColumn.GENDER,
            Constants.UserColumn.ADDRESS,
            Constants.UserColumn.CITY,
            Constants.UserColumn.PHONE1,
            Constants.UserColumn.PHONE2,
            Constants.UserColumn.EMAIL,
            Constants.UserColumn.COMMENTS,
            Constants.UserColumn.CREATED_ON,
            Constants.UserColumn.LAST_MODIFIED
        )
        val cursor: Cursor = database.query(
            Constants.UserColumn.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )
        try {
            cursor.moveToNext()
        } catch (e: Exception) {
            Log.e(Constants.LOG_TAG, e.message, e)
            FirebaseCrashlytics.getInstance().recordException(e)
        }

        return cursor
    }

    fun insertTransactions(transactions: Transactions): Boolean {
        val database: SQLiteDatabase = this.writableDatabase
        database.beginTransaction()
        var result: Long = 0.toLong()
        try {
            result = database.insert(
                Constants.TranColumn.TABLE_NAME,
                null,
                contentValuesTransaction(transactions, false)
            )
        } catch (e: Exception) {
            Log.e(Constants.LOG_TAG, e.message, e)
            FirebaseCrashlytics.getInstance().recordException(e)
        } finally {
            database.setTransactionSuccessful()
            database.endTransaction()
            database.close()
        }
        //mContext.contentResolver.notifyChange(Constants.TranColumn.TRANSACTION_TABLE_URI, null)
        return result != (-1).toLong() // If -1 return it means not successfully inserted
    }

    fun getTransactionsByUserId(mUserId: Int): ArrayList<Transactions> {
        val transactions = ArrayList<Transactions>()
        val getFromDatabase: SQLiteDatabase = this.readableDatabase
        val projections = arrayOf(
            BaseColumns._ID,
            Constants.TranColumn.USER_ID,
            Constants.TranColumn.DATE,
            Constants.TranColumn.DESCRIPTION,
            Constants.TranColumn.CREDIT,
            Constants.TranColumn.DEBIT,
            Constants.TranColumn.BALANCE,
            Constants.TranColumn.CREATED_ON,
            Constants.TranColumn.LAST_MODIFIED
        )
        try {
            val cursor: Cursor = getFromDatabase.query(
                Constants.TranColumn.TABLE_NAME,
                projections,
                "${Constants.TranColumn.USER_ID} = ?",
                arrayOf(mUserId.toString()),
                null,
                null,
                null
            )
            try {
                while (cursor.moveToNext()) {
                    val transaction = Transactions().apply {
                        userId =
                            cursor.getInt(cursor.getColumnIndexOrThrow(Constants.TranColumn.USER_ID))
                        date =
                            cursor.getString(cursor.getColumnIndexOrThrow(Constants.TranColumn.DATE))
                        description =
                            cursor.getString(cursor.getColumnIndexOrThrow(Constants.TranColumn.DESCRIPTION))
                        credit =
                            cursor.getDouble(cursor.getColumnIndexOrThrow(Constants.TranColumn.CREDIT))
                        debit =
                            cursor.getDouble(cursor.getColumnIndexOrThrow(Constants.TranColumn.DEBIT))
                        balance =
                            cursor.getDouble(cursor.getColumnIndexOrThrow(Constants.TranColumn.BALANCE))
                        created =
                            cursor.getString(cursor.getColumnIndexOrThrow(Constants.TranColumn.CREATED_ON))
                        modified =
                            cursor.getString(cursor.getColumnIndexOrThrow(Constants.TranColumn.LAST_MODIFIED))
                    }
                    transactions.add(transaction)
                }
            } catch (e: Exception) {
                Log.e(Constants.LOG_TAG, e.message, e)
                FirebaseCrashlytics.getInstance().recordException(e)
            } finally {
                cursor.close()
                getFromDatabase.close()
            }
        } catch (e: Exception) {
            Log.e(Constants.LOG_TAG, e.message, e)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
        return transactions
    }

    fun updateTransactions(id: Int, transactions: Transactions): Boolean {
        val updateTransactions: SQLiteDatabase = this.writableDatabase
        try {
            updateTransactions.update(
                Constants.TranColumn.TABLE_NAME,
                contentValuesTransaction(transactions, true),
                "${BaseColumns._ID} = ?",
                arrayOf(id.toString())
            )
            return true
        } catch (e: Exception) {
            Log.e(Constants.LOG_TAG, e.message, e)
            FirebaseCrashlytics.getInstance().recordException(e)
        } finally {
            updateTransactions.close()
        }
        return false
    }

    fun getPreviousBalanceByUserId(id: Int): Double {
        var previousBalance: Double = 0.toDouble()
        val getBalanceFromDatabase: SQLiteDatabase = this.readableDatabase
        val userID =
            "SELECT * FROM ${Constants.TranColumn.TABLE_NAME} WHERE ${Constants.TranColumn.USER_ID} = $id;"
        val cursor: Cursor = getBalanceFromDatabase.rawQuery(userID, null)
        try {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                if (cursor.count > 0) {
                    previousBalance =
                        cursor.getDouble(cursor.getColumnIndexOrThrow(Constants.TranColumn.BALANCE))
                    cursor.moveToNext()
                }
            }
        } catch (e: Exception) {
            Log.e(Constants.LOG_TAG, e.message, e)
            FirebaseCrashlytics.getInstance().recordException(e)
        } finally {
            cursor.close()
            getBalanceFromDatabase.close()
        }
        return previousBalance
    }

    fun getSumForColumns(id: Int, nameColumn: String): Double {
        var sum: Double = 0.toDouble()
        var sumForColumn = ""
        when (nameColumn) {
            "Credit" -> {
                sumForColumn = Constants.TranColumn.CREDIT
            }
            "Debit" -> {
                sumForColumn = Constants.TranColumn.DEBIT
            }
            "Balance" -> {
                sumForColumn = Constants.TranColumn.BALANCE
            }
        }
        val getSumFromDatabase: SQLiteDatabase = this.readableDatabase
        val userID =
            "SELECT SUM ($sumForColumn) AS TOTAL FROM ${Constants.TranColumn.TABLE_NAME} WHERE ${Constants.TranColumn.USER_ID} = $id ;"
        val cursor: Cursor = getSumFromDatabase.rawQuery(userID, null)
        try {
            if (cursor.moveToFirst()) {
                sum = cursor.getDouble(cursor.getColumnIndexOrThrow("TOTAL"))
            }
        } catch (e: Exception) {
            Log.e(Constants.LOG_TAG, e.message, e)
            FirebaseCrashlytics.getInstance().recordException(e)
        } finally {
            cursor.close()
            getSumFromDatabase.close()
        }
        return sum
    }
}