package com.example.ifuturusaction

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.ifuturusaction.model.lodgereportmodel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_report.*
import kotlinx.android.synthetic.main.activity_report_edit.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class ReportEditActivity : AppCompatActivity() {

    private var reportID: String? = null
    private var reportLat: String? = null
    private var reportLong: String? = null
    private lateinit var mListener: ValueEventListener

    // Firebase instance variables
    private lateinit var mFirebaseAuth: FirebaseAuth
    private var mFirebaseUser: FirebaseUser? = null
    private var mFirebaseDatabase: FirebaseDatabase? = null
    private lateinit var referenceReportActivity: DatabaseReference

    private var mImageUri: Uri? = null

    // Store Temporary Value
    var mphotoUrl : String? = null


    private var id: String? = null
    private var name: String? = null
    private var email: String? = null
    private var gender: String? = null
    private var complaintDetails: String? = null
    private var complaintNotes: String? = null
    private var complaintLocation: String? = null
    private var complaintCategory: String? = null
    private var complaintStatus: String? = null
    private var complaintId: String? = null
    private var complaintDate: String? = null
    private var complaintTime: String? = null
    private var photoUrl: String? = null
    private var latitude: String? = null
    private var longitude: String? = null


    private var report_status_list: Spinner? = null


    companion object {
        val REPORT_ID_KEY2 = "REPORT_ID_KEY"

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_edit)

        report_status_list = findViewById(R.id.report_status_list1)
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.reportstatus,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        report_status_list?.setAdapter(adapter)


        reportID = intent.getStringExtra(ReportActivity.REPORT_ID_KEY1)
        supportActionBar?.title = "Report ID: $reportID"

        loadReport()

       btnSave1.setOnClickListener({launchUpdate()})
        notifyUser.setOnClickListener({launchEmail()})
    }

    private fun loadReport() {
        val ref = FirebaseDatabase.getInstance().getReference("lodgereport").child("$reportID")

        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                // Load data into the Submitted Report
                val reportDetails = p0.getValue(lodgereportmodel::class.java)
                // Assign Data to the Empty Field
                if (reportDetails != null){
                    Picasso.get().load(reportDetails.photoUrl).into(reportImage1)
                    reportDetailss1.setText("${reportDetails.complaintDetails}")
                    reportNotes1.setText("${reportDetails.complaintNotes}")
                    reportCategory1.setText("${reportDetails.complaintCategory}")
                    reportLocation1.setText("${reportDetails.complaintLocation}")
                    complaintDate1.setText("${reportDetails.complaintDate}, ${reportDetails.complaintTime}")

                    // Assign value to Temporary Variable
                    id = reportDetails.id
                    name = reportDetails.name
                    email = reportDetails.email
                    gender = reportDetails.gender
                    complaintDetails = reportDetails.complaintDetails
                    complaintNotes = reportDetails.complaintNotes
                    complaintLocation = reportDetails.complaintLocation
                    complaintCategory = reportDetails.complaintCategory
                    complaintStatus = reportDetails.complaintStatus
                    complaintId = reportDetails.complaintId
                    complaintDate = reportDetails.complaintDate
                    complaintTime = reportDetails.complaintTime
                    photoUrl = reportDetails.photoUrl
                    latitude = reportDetails.latitude
                    longitude = reportDetails.longitude
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                // Handle Error
            }
        })
    }


    private fun launchUpdate() {
        val newComplaintReportStatus = report_status_list!!.getSelectedItem().toString()
        Log.d("Report Notes Details", newComplaintReportStatus)

//
        // Save the new Report Notes to Firebase Database
        val reference = FirebaseDatabase.getInstance().getReference("lodgereport").child("$reportID")

        val newUpdatedReport = lodgereportmodel(
            id,
            name,
            email,
            gender,
            complaintDetails,
            complaintNotes,
            complaintLocation,
            complaintCategory,
            newComplaintReportStatus,
            complaintId,
            complaintDate,
            complaintTime,
            photoUrl,
            latitude,
            longitude
        )
        if (newComplaintReportStatus=="completed"||newComplaintReportStatus=="processing") {
                notifyUser.visibility = View.VISIBLE
        }
        reference.setValue(newUpdatedReport)
            .addOnSuccessListener {
                Log.d("Update Report", "Successfully saved value to database")
                Toast.makeText(this, "Report Notes has successfully been updated", Toast.LENGTH_LONG).show()
            }



    }


    private fun launchEmail() {

            val recipient = email?.let { arrayOf<String>(it) }

            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.putExtra(Intent.EXTRA_EMAIL, recipient)
            emailIntent.putExtra(
                Intent.EXTRA_SUBJECT,
                "Complaint from " + complaintDate
            )

            emailIntent.putExtra(
                Intent.EXTRA_TEXT,
                "The report you summit on " + complaintDate +" "+ complaintTime + " at location: "+complaintLocation+ " is already "+ complaintStatus + "."

            )


            emailIntent.type = "message/rfc822"
            startActivity(Intent.createChooser(emailIntent, "Choose an email client"))
        }
    }




