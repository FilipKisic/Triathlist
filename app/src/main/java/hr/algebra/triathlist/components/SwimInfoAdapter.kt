package hr.algebra.triathlist.components

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.triathlist.R
import hr.algebra.triathlist.database.TriathlistDatabase
import hr.algebra.triathlist.model.Activity
import kotlinx.android.synthetic.main.swim_info_card.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SwimInfoAdapter(private val swimInfoList: MutableList<Activity>, private val context: Context) :
    RecyclerView.Adapter<SwimInfoAdapter.SwimInfoViewHolder>() {

    inner class SwimInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayOfWeek: TextView = itemView.tvDayOfWeek
        val lapCount: TextView = itemView.tvLapCount
        val time: TextView = itemView.tvTime
        val distance: TextView = itemView.tvDistance
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwimInfoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.swim_info_card, parent, false)
        return SwimInfoViewHolder(itemView)
    }

    override fun getItemCount(): Int = swimInfoList.size

    override fun onBindViewHolder(holder: SwimInfoViewHolder, position: Int) {
        val currentInfo = swimInfoList[position]
        holder.dayOfWeek.text = currentInfo.dayOfWeek
        holder.lapCount.text = if (currentInfo.laps != 1) "${currentInfo.laps} laps" else "${currentInfo.laps} lap"
        holder.time.text = currentInfo.duration.toString()
        holder.distance.text = "${currentInfo.distance} m"
        holder.itemView.setOnLongClickListener {
            showMessage(position)
        }
    }

    private fun showMessage(position: Int): Boolean {
        AlertDialog.Builder(context).apply {
            setTitle("Delete")
            setMessage("Delete selected session?")
            setNegativeButton("Cancel", null)
            setPositiveButton("OK") { _, _ -> deleteItem(position) }
            show()
        }
        return true
    }

    private fun deleteItem(position: Int) {
        val item = swimInfoList[position]
        val db = TriathlistDatabase.getDatabase(context)
        GlobalScope.launch {
            db.activityDao().deleteById(item.idActivity)
        }
        swimInfoList.removeAt(position)
        notifyDataSetChanged()
    }
}