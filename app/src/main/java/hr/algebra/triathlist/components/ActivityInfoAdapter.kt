package hr.algebra.triathlist.components

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.triathlist.R
import hr.algebra.triathlist.database.TriathlistDatabase
import hr.algebra.triathlist.model.Activity
import hr.algebra.triathlist.model.ActivityType
import kotlinx.android.synthetic.main.swim_info_card.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val ACTIVITY = "Activity"
private const val METRIC = "Metric"

class ActivityInfoAdapter(private val swimInfoList: MutableList<Activity>, private val context: Context) :
    RecyclerView.Adapter<ActivityInfoAdapter.ActivityInfoViewHolder>() {

    inner class ActivityInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayOfWeek: TextView = itemView.tvDayOfWeek
        val lapCount: TextView = itemView.tvLapCount
        val time: TextView = itemView.tvTime
        val distance: TextView = itemView.tvDistance
        val activityType: ImageView = itemView.ivActivity
        val metric: ImageView = itemView.ivMetricIcon
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityInfoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.swim_info_card, parent, false)
        return ActivityInfoViewHolder(itemView)
    }

    override fun getItemCount(): Int = swimInfoList.size

    override fun onBindViewHolder(holder: ActivityInfoViewHolder, position: Int) {
        val currentInfo = swimInfoList[position]
        holder.dayOfWeek.text = currentInfo.dayOfWeek
        holder.lapCount.text = processActivity(currentInfo)
        holder.time.text = currentInfo.duration
        holder.distance.text = "${currentInfo.distance} m"
        holder.activityType.setImageResource(chooseIcon(currentInfo, ACTIVITY))
        holder.metric.setImageResource(chooseIcon(currentInfo, METRIC))
        holder.itemView.setOnLongClickListener {
            showMessage(position)
        }
    }

    private fun processActivity(currentInfo: Activity): String {
        return when (currentInfo.activityType) {
            ActivityType.SWIMMING -> if (currentInfo.laps != 1) "${currentInfo.laps} laps" else "${currentInfo.laps} lap"
            ActivityType.RUNNING -> "${currentInfo.steps} steps"
            else -> "${currentInfo.calories} calories"
        }
    }

    private fun chooseIcon(currentInfo: Activity, type: String): Int {
        return if (type == ACTIVITY) {
            when (currentInfo.activityType) {
                ActivityType.SWIMMING -> R.drawable.swimming
                ActivityType.RUNNING -> R.drawable.running
                ActivityType.CYCLING -> R.drawable.cycle
            }
        } else {
            when (currentInfo.activityType) {
                ActivityType.SWIMMING -> R.drawable.lap
                ActivityType.RUNNING -> R.drawable.sneakers
                ActivityType.CYCLING -> R.drawable.calories
            }
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