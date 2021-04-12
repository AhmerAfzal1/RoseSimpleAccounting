package com.ahmer.accounting

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ahmer.accounting.helper.Constants.Companion.LOG_TAG
import com.ahmer.accounting.transactions.AddTransactions
import com.ahmer.accounting.transactions.UserIdTransactions
import com.ahmer.accounting.user.AddUserProfileData
import com.ahmer.accounting.user.GetUserData
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.title = getString(R.string.app_name)

        val addRecord = findViewById<MaterialButton>(R.id.addUserProfile)
        val getRecord = findViewById<MaterialButton>(R.id.getUserRecord)
        val addTransaction = findViewById<MaterialButton>(R.id.addTransaction)
        val getTransaction = findViewById<MaterialButton>(R.id.getTransaction)

        addRecord.setOnClickListener {
            val intent = Intent(this, AddUserProfileData::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N ||
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
                ) {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            }
            Log.v(LOG_TAG, "Add record activity opened")
            startActivity(intent)
        }

        getRecord.setOnClickListener {
            val intent = Intent(this, GetUserData::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N ||
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
                ) {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            }
            startActivity(intent)
        }

        addTransaction.setOnClickListener {
            val intent = Intent(this, AddTransactions::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N ||
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
                ) {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            }
            startActivity(intent)
        }

        getTransaction.setOnClickListener {
            val intent = Intent(this, UserIdTransactions::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N ||
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
                ) {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            }
            startActivity(intent)
        }
    }
}