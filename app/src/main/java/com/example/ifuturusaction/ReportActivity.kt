package com.example.ifuturusaction

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import com.example.ifuturusaction.model.lodgereportmodel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_report_list.*
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri
import android.view.View
import android.widget.Button
import com.bumptech.glide.Glide
import com.example.ifuturusaction.model.userprofilemodel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_report.*
import kotlinx.android.synthetic.main.nav_header_main.*

//import com.example.ifuturusaction.ReportListActivity.Companion.REPORT_CATEGORY
//import com.example.ifuturusaction.ReportListActivity.Companion.REPORT_PICTURE


class ReportActivity : AppCompatActivity() {

    private lateinit var referenceReport: DatabaseReference

    internal lateinit var mReportImage: ImageView
    internal lateinit var mReportDate: TextView
    internal lateinit var mReportTime: TextView
    internal lateinit var mReportNotes: TextView
    internal lateinit var mReportCategory: TextView
    internal lateinit var mReportLocation: TextView
    internal lateinit var mReportDetails: TextView
    internal lateinit var mReportStatus: TextView


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


    private var reportID: String? = null
    private lateinit var mListener: ValueEventListener

    private var mbtn_navigation: Button? = null
    private var mbtn_sendHelp: Button? = null
    // Firebase instance variables
    private lateinit var mFirebaseAuth: FirebaseAuth
    private var mFirebaseUser: FirebaseUser? = null
    private var mFirebaseDatabase: FirebaseDatabase? = null
    private lateinit var referenceReportActivity: DatabaseReference

    private var mImageUri: Uri? = null

    // Store Temporary Value
    var mphotoUrl : String? = null



    companion object {
        val REPORT_ID_KEY1 = "REPORT_ID_KEY"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)



//
        reportID = intent.getStringExtra(ReportListActivity.REPORT_ID_KEY)
        supportActionBar?.title = "Report ID: $reportID"


    loadReport()



        mbtn_navigation = findViewById(R.id.btnNavigation)
     btnNavigation.setOnClickListener({LaunchNavigation()})


        mbtn_sendHelp = findViewById(R.id.btnSendHelp)
        btnSendHelp.setOnClickListener({LaunchEmail()})
        btnEdit.setOnClickListener({LaunchEdit()})

    }

    private fun loadReport() {
        val ref = FirebaseDatabase.getInstance().getReference("lodgereport").child("$reportID")

        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                // Load data into the Submitted Report
                val reportDetails = p0.getValue(lodgereportmodel::class.java)
                // Assign Data to the Empty Field
                if (reportDetails != null){
                    Picasso.get().load(reportDetails.photoUrl).into(reportImage)
                    reportDetailss.setText("${reportDetails.complaintDetails}")
                    reportStatus.setText("${reportDetails.complaintStatus}")
                    reportNotes.setText("${reportDetails.complaintNotes}")
                    reportCategory.setText("${reportDetails.complaintCategory}")
                    reportLocation.setText("${reportDetails.complaintLocation}")
                    reportDate.setText("${reportDetails.complaintDate}, ${reportDetails.complaintTime}")

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

    private fun LaunchEmail() {
        val recipient = arrayOf<String>("callcentre@dbkl.gov.my")

        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.putExtra(Intent.EXTRA_EMAIL, recipient)
        emailIntent.putExtra(
            Intent.EXTRA_SUBJECT,
            "Complaint from " + complaintDate
        )
        emailIntent.putExtra(
            Intent.EXTRA_TEXT,
            "We get a complaint from the user who lived in Kuala Lumpur, We hope DBKL can take action from it. " +
                    "The location of the complaint report is : " +complaintLocation+". There is an extra notes from the reporter which is " +complaintNotes+ "."
        )


        emailIntent.type = "message/rfc822"
        startActivity(Intent.createChooser(emailIntent, "Choose an email client"))
    }

    private fun LaunchNavigation() {
        val getAddress = Uri.parse("geo:0,0?q=" + complaintLocation )
        val mapIntent = Intent(Intent.ACTION_VIEW, getAddress)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }
private fun LaunchEdit(){

    val intent = Intent(this, ReportEditActivity::class.java)
    intent.putExtra(REPORT_ID_KEY1, reportID)
    startActivity(intent)

}


}
