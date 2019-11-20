package com.example.ifuturusaction

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ifuturusaction.adapter.viewallreportadapter
import com.example.ifuturusaction.model.lodgereportmodel
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_report_list.*
import kotlinx.android.synthetic.main.activity_chat_report_row_list.view.*
import kotlinx.android.synthetic.main.activity_report_edit.*

import kotlinx.android.synthetic.main.activity_report_list.*
import kotlinx.android.synthetic.main.activity_report_list_row.view.*

class ReportListActivity : AppCompatActivity() {


    // Get Reference to the Database
    lateinit var reference: DatabaseReference


    // Store Firebase Database data to an Array List
    lateinit var viewAllReportList : ArrayList<lodgereportmodel>

    // Initialize variables
    lateinit var mrecyclerview : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_list)

//        reference = FirebaseDatabase.getInstance().reference.child("lodgereport")

        mrecyclerview = findViewById(R.id.report_list_view)
        mrecyclerview.setHasFixedSize(true)
        mrecyclerview.layoutManager = LinearLayoutManager(this)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.searchreportstatus,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        report_spinner?.adapter = adapter

        filter.setOnClickListener({fetchReportList()})

            fetchReportList()
        }

    private fun fetchReportList() {
        // Store value to Array List
        reference = FirebaseDatabase.getInstance().getReference("lodgereport")
        reference.addListenerForSingleValueEvent(object: ValueEventListener  {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    viewAllReportList = ArrayList()

                    for(ds in p0.children) {
                 if(report_spinner.selectedItem == "default"){
                        viewAllReportList.add(ds.getValue(lodgereportmodel::class.java)!!)
                    }
                 else if(report_spinner.selectedItem == "pending") {
                     if (ds.getValue(lodgereportmodel::class.java)!!.complaintStatus == "pending") {
                         viewAllReportList.add(ds.getValue(lodgereportmodel::class.java)!!)
                     }
                 }
                 else if(report_spinner.selectedItem == "processing") {
                     if (ds.getValue(lodgereportmodel::class.java)!!.complaintStatus == "processing") {
                         viewAllReportList.add(ds.getValue(lodgereportmodel::class.java)!!)
                     }
                 }
                 else if(report_spinner.selectedItem == "completed") {
                     if (ds.getValue(lodgereportmodel::class.java)!!.complaintStatus == "completed") {
                         viewAllReportList.add(ds.getValue(lodgereportmodel::class.java)!!)
                     }
                 }


             else {

                  mrecyclerview.adapter = null
                   }
                    }
                    val userReportAdapter = viewallreportadapter(viewAllReportList)
                    mrecyclerview.adapter = userReportAdapter
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                // Handle Error
            }
        })
    }





    }






