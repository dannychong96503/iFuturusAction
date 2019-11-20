package com.example.ifuturusaction.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.ifuturusaction.*
import com.example.ifuturusaction.model.lodgereportmodel
import com.google.android.material.button.MaterialButton
import com.squareup.picasso.Picasso

class viewallreportadapter(var viewAllReportList: ArrayList<lodgereportmodel>) :
        RecyclerView.Adapter<viewallreportadapter.ViewAllReportViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewAllReportViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_report_list_row, parent, false)
        return ViewAllReportViewHolder(itemView)
    }

    companion object {
        val REPORT_ID_KEY = "REPORT_ID_KEY"
    }


    override fun onBindViewHolder(holder: ViewAllReportViewHolder, position: Int) {

        // Set Value into each view of recycler view
        Picasso.get().load(viewAllReportList[position].photoUrl).into(holder.ivReportimage)
        holder.ReportTitle.setText("Compliant Type: \n${viewAllReportList[position].complaintDetails}")
        holder.ReportStatus.setText("Complaint Status: \n${viewAllReportList[position].complaintStatus}")
        holder.ReportLocation.setText("Compliant Location: \n${viewAllReportList[position].complaintLocation}")


        holder.view_report.setOnClickListener {
            val intent = Intent(it.context, ReportActivity::class.java)
            intent.putExtra(REPORT_ID_KEY, viewAllReportList[position].complaintId)
            it.context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return viewAllReportList.size
    }

    class ViewAllReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var ivReportimage: ImageView = itemView.findViewById(R.id.reportPicture)
        internal var ReportTitle: TextView = itemView.findViewById(R.id.reportTitle)
        internal var ReportStatus: TextView = itemView.findViewById(R.id.reportStatus)
        internal var ReportLocation: TextView = itemView.findViewById(R.id.reportDestination)
        internal var view_report: MaterialButton = itemView.findViewById(R.id.view_report)

    }
}