package hr.algebra.triathlist.components

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hr.algebra.triathlist.R
import hr.algebra.triathlist.model.Activity
import hr.algebra.triathlist.model.ActivityType
import kotlinx.android.synthetic.main.swim_info_card.view.*

private const val ACTIVITY = "Activity"
private const val METRIC = "Metric"

class ActivityInfoAdapter(private val activityInfoList: MutableList<Activity>, private val context: Context) :
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

    override fun getItemCount(): Int = activityInfoList.size

    override fun onBindViewHolder(holder: ActivityInfoViewHolder, position: Int) {
        val currentInfo = activityInfoList[position]
        holder.dayOfWeek.text = currentInfo.dayOfWeek
        holder.lapCount.text = processActivity(currentInfo)
        holder.time.text = currentInfo.duration
        holder.distance.text = "${currentInfo.distance} m"
        holder.activityType.setImageResource(chooseIcon(currentInfo, ACTIVITY))
        holder.metric.setImageResource(chooseIcon(currentInfo, METRIC))
    }

    private fun processActivity(currentInfo: Activity): String {
        return when (currentInfo.activityType) {
            ActivityType.SWIMMING.ordinal -> if (currentInfo.laps != 1) "${currentInfo.laps} laps" else "${currentInfo.laps} lap"
            ActivityType.RUNNING.ordinal -> "${currentInfo.steps} steps"
            else -> "${currentInfo.calories} cal"
        }
    }

    private fun chooseIcon(currentInfo: Activity, type: String): Int {
        return if (type == ACTIVITY) {
            when (currentInfo.activityType) {
                ActivityType.SWIMMING.ordinal -> R.drawable.swimming
                ActivityType.RUNNING.ordinal -> R.drawable.running
                ActivityType.CYCLING.ordinal -> R.drawable.cycle
                else -> R.drawable.swimming
            }
        } else {
            when (currentInfo.activityType) {
                ActivityType.SWIMMING.ordinal -> R.drawable.lap
                ActivityType.RUNNING.ordinal -> R.drawable.sneakers
                ActivityType.CYCLING.ordinal -> R.drawable.calories
                else -> R.drawable.lap
            }
        }
    }
}