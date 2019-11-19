package com.example.ifuturusaction

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ifuturusaction.model.feedbackmodel
import com.example.ifuturusaction.model.lodgereportmodel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_feedback.*
import kotlinx.android.synthetic.main.activity_recyclerview_feedback.view.*
import kotlinx.android.synthetic.main.activity_report_list.*
import kotlinx.android.synthetic.main.activity_report_list_row.view.*

class FeedbackActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        feedback_list_view.layoutManager = LinearLayoutManager(this)

        fetchFeedbackList()
    }

    private fun fetchFeedbackList() {
        val ref = FirebaseDatabase.getInstance().getReference("userfeedback")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                p0.children.forEach {
                    Log.d("Feedback Activity List", "Inside DataSnapshot: $it")

                    // Get Chat Report List
                    val feedbackList = it.getValue(feedbackmodel::class.java)

                    // If Firebase have value
                    if (feedbackList != null) {
                        adapter.add(ReportItem(feedbackList))
                    }
                }

                feedback_list_view.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    class ReportItem(val feedbackList: feedbackmodel) : Item<ViewHolder>() {

        override fun bind(viewHolder: ViewHolder, position: Int) {
            // Will be Called in our list for each Report Object
            viewHolder.itemView.feedBackContent.text =
                "\n${feedbackList.feedbackdetails}"

        }

        override fun getLayout(): Int {
            return R.layout.activity_recyclerview_feedback
        }

    }
}

