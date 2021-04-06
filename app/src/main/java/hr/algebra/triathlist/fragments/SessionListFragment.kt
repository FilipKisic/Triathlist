package hr.algebra.triathlist.fragments

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import hr.algebra.triathlist.MenuActivity
import hr.algebra.triathlist.R
import hr.algebra.triathlist.components.SwimInfoAdapter
import hr.algebra.triathlist.dal.TRIATHLIST_PROVIDER_CONTENT_URI
import hr.algebra.triathlist.framework.startActivity
import hr.algebra.triathlist.model.SwimInfo
import kotlinx.android.synthetic.main.fragment_session_list.*

class SessionListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_session_list, container, false)
    }

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

    private fun collectSessionsFromDatabase(): MutableList<SwimInfo> {
        val list = ArrayList<SwimInfo>()
        val cursor = context?.contentResolver?.query(TRIATHLIST_PROVIDER_CONTENT_URI, null, null, null, "${SwimInfo::_id.name} DESC")
        while (cursor != null && cursor.moveToNext()) {
            list.add(
                SwimInfo(
                    cursor.getInt(cursor.getColumnIndex("_id")).toLong(),
                    cursor.getString(cursor.getColumnIndex("dayOfWeek")),
                    cursor.getInt(cursor.getColumnIndex("laps")),
                    cursor.getString(cursor.getColumnIndex("totalTime")),
                    cursor.getInt(cursor.getColumnIndex("distance"))
                )
            )
        }
        if (list.isEmpty()) {
            showEmptyListMessage()
        }
        return list
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