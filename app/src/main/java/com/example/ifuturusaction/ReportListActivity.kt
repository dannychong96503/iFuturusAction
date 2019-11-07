package com.example.ifuturusaction

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ifuturusaction.model.lodgereportmodel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_report_list.*
import kotlinx.android.synthetic.main.activity_chat_report_row_list.view.*

import kotlinx.android.synthetic.main.activity_report_list.*
import kotlinx.android.synthetic.main.activity_report_list_row.view.*

class ReportListActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_list)

        report_list_view.layoutManager = LinearLayoutManager(this)


        fetchReportList()
    }
    companion object {
        val REPORT_ID_KEY = "REPORT_ID_KEY"
    }


    private fun fetchReportList() {
        val ref = FirebaseDatabase.getInstance().getReference("lodgereport")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                p0.children.forEach {
                    Log.d("Report Activity List", "Inside DataSnapshot: $it")

                    // Get Chat Report List
                    val reportList = it.getValue(lodgereportmodel::class.java)

                    // If Firebase have value
                    if (reportList != null) {
                        adapter.add(ReportItem(reportList))
                    }
                }

                adapter.setOnItemClickListener { item, view ->

                    // Get Report Id
                    val reportItem = item as ReportItem

                    // Start Chat Log Activity
                    val intent = Intent(view.context, ReportActivity::class.java)
                    intent.putExtra(REPORT_ID_KEY, reportItem.reportList.complaintId)
                    Log.d(
                        "Report List Activity",
                        "Report ID: ${reportItem.reportList.complaintId}"
                    )
                    startActivity(intent)
                }


                report_list_view.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    class ReportItem(val reportList: lodgereportmodel) : Item<ViewHolder>() {
        override fun bind(viewHolder: ViewHolder, position: Int) {
            // Will be Called in our list for each Report Object
            viewHolder.itemView.reportTitle.text =
                "Complaint Details: \n${reportList.complaintDetails}"
            viewHolder.itemView.reportStatus.text=
            "Complaint Status: \n${reportList.complaintStatus}"
            viewHolder.itemView.reportDestination.text=
                "Complaint Location: \n${reportList.complaintLocation}"
            Picasso.get().load(reportList.photoUrl).into(viewHolder.itemView.reportPicture)
        }

        override fun getLayout(): Int {
            return R.layout.activity_report_list_row
        }

    }
}


