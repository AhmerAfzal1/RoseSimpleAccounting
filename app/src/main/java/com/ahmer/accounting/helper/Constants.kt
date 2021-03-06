package com.ahmer.accounting.helper

import android.content.ContentResolver
import android.net.Uri
import android.provider.BaseColumns
import com.ahmer.accounting.BuildConfig

class Constants {

    companion object {
        const val LOG_TAG: String = "SimpleAccounting"
        const val APPLICATION_ID = BuildConfig.APPLICATION_ID
        const val DATABASE_NAME: String = "SimpleAccounting.db"
        const val DATABASE_VERSION: Int = 1
        const val DATE_LONG_PATTERN = "dd-MM-yyyy"
        const val DATE_SHORT_PATTERN = "dd-MM-yy"
        const val DATE_TIME_PATTERN = "dd MMM yyyy hh:mm:ss a"
        const val PLAY_STORE_LINK = "https://play.google.com/store/apps/details?id=$APPLICATION_ID"
        const val PREFERENCE_LOCKSCREEN = "LockScreen"
        const val PREFERENCE_LOCKSCREEN_KEY = "LockScreenKey"
        const val PREFERENCE_THEME = "Theme"
        const val PREFERENCE_THEME_KEY = "ThemeKey"
    }

    class UserColumn : BaseColumns {
        companion object {
            const val TABLE_NAME: String = "Customers"
            const val NAME: String = "Name"
            const val GENDER: String = "Gender"
            const val ADDRESS: String = "Address"
            const val CITY: String = "City"
            const val PHONE1: String = "Phone1"
            const val PHONE2: String = "Phone2"
            const val EMAIL: String = "Email"
            const val COMMENTS: String = "Comments"
            const val CREATED_ON: String = "Created"
            const val LAST_MODIFIED: String = "LastModified"

            val USER_TABLE_URI: Uri = Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT)
                .authority(APPLICATION_ID)
                .appendPath(TABLE_NAME)
                .build()
        }
    }

    class TranColumn : BaseColumns {
        companion object {
            const val TABLE_NAME: String = "Transactions"
            const val USER_ID: String = "UserID"
            const val DATE: String = "Date"
            const val DESCRIPTION: String = "Description"
            const val CREDIT: String = "Credit"
            const val DEBIT: String = "Debit"
            const val IS_DEBIT: String = "IsDebit"
            const val CREATED_ON: String = "Created"
            const val LAST_MODIFIED: String = "LastModified"
            const val LAST_MODIFIED_VALUE: String = "ModifiedValue"
            const val LAST_MODIFIED_ACCOUNT_TYPE: String = "ModifiedAccountType"

            val TRANSACTION_TABLE_URI: Uri = Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT)
                .authority(APPLICATION_ID)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}