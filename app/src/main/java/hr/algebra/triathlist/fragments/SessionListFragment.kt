package hr.algebra.triathlist.fragments

import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import hr.algebra.triathlist.MenuActivity
import hr.algebra.triathlist.R
import hr.algebra.triathlist.components.ActivityInfoAdapter
import hr.algebra.triathlist.framework.startActivity
import hr.algebra.triathlist.model.Activity
import kotlinx.android.synthetic.main.fragment_session_list.*

class SessionListFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var itemList: ArrayList<Activity>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_session_list, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        database = FirebaseDatabase.getInstance().getReference("Activities")
        itemList = ArrayList()
        database.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (a in snapshot.children) {
                        val activity = a.getValue(Activity::class.java)
                        itemList.add(activity!!)
                    }
                    rvActivityList.adapter = ActivityInfoAdapter(itemList, requireContext())
                }
            }
        })
        rvActivityList.layoutManager = LinearLayoutManager(requireContext())
        rvActivityList.setHasFixedSize(true)
    }

    private fun initListeners() {
        ivNewSession.setOnClickListener {
            startActivity<MenuActivity>()
        }
    }
}