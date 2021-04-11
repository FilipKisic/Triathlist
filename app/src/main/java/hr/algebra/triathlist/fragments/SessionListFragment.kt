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
import hr.algebra.triathlist.MenuActivity
import hr.algebra.triathlist.R
import hr.algebra.triathlist.components.SwimInfoAdapter
import hr.algebra.triathlist.database.TriathlistDatabase
import hr.algebra.triathlist.framework.startActivity
import hr.algebra.triathlist.model.Activity
import kotlinx.android.synthetic.main.fragment_session_list.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SessionListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_session_list, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()

        val cards = collectSessionsFromDatabase()
        rvSwimInfoList.adapter = SwimInfoAdapter(cards, requireContext())
        rvSwimInfoList.layoutManager = LinearLayoutManager(requireContext())
        rvSwimInfoList.setHasFixedSize(true)
    }

    private fun initListeners() {
        ivNewSession.setOnClickListener {
            startActivity<MenuActivity>()
        }
    }

    //ASK
    private fun collectSessionsFromDatabase(): MutableList<Activity> = runBlocking {
        var list: MutableList<Activity>? = null
        val db = TriathlistDatabase.getDatabase(requireActivity().applicationContext)
        val job = GlobalScope.launch {
            list = db.activityDao().getAllActivities()
        }
        job.join()
        return@runBlocking if (list.isNullOrEmpty()) {
            showEmptyListMessage()
            ArrayList()
        } else list!!
    }

    private fun showEmptyListMessage() {
        val tvMessage = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
            text = getString(R.string.startMessage)
            textSize = 20f
            gravity = Gravity.CENTER_VERTICAL
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE)
        }
        session_list.addView(tvMessage)
    }
}