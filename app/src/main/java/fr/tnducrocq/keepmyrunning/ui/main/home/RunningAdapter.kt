package fr.tnducrocq.keepmyrunning.ui.main.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.tnducrocq.keepmyrunning.R
import fr.tnducrocq.keepmyrunning.UnitUtils
import fr.tnducrocq.keepmyrunning.model.Running
import kotlinx.android.synthetic.main.item_running.view.*
import java.text.SimpleDateFormat


class RunningAdapter(val items: ArrayList<Running>, val context: Context) : RecyclerView.Adapter<RunningAdapter.RunningViewHolder>() {

    var runningAdapterListener: RunningAdapterListener? = null

    fun setRunningAdapterListener(listener: (View, Running) -> Unit) {
        runningAdapterListener = object : RunningAdapterListener {
            override fun onSelect(itemView: View, running: Running) {
                listener(itemView, running)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunningViewHolder {
        return RunningViewHolder(LayoutInflater.from(context).inflate(R.layout.item_running, parent, false))
    }

    override fun onBindViewHolder(holder: RunningViewHolder, position: Int) {

        val running = items.get(position)
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm")
        holder.date.text = format.format(running.date)

        val distance = running.distance / 1000F
        holder.distance.text = "%.2f".format(distance)

        val seconds = running.time / 1000
        val s = seconds % 60
        val m = seconds / 60 % 60
        val h = seconds / (60 * 60) % 24
        holder.duration.text = if (h == 0L) "%02d:%02d".format(m, s) else "%d:%02d:%02d".format(h, m, s)

        holder.speed.text = UnitUtils.minAtKm(running.time, running.distance.toInt())

        holder.itemView.setOnClickListener {
            runningAdapterListener?.onSelect(it, running)
        }

        /*when (running.mood) {
            Running.Mood.Good -> {
                holder.mood.setImageDrawable(context.resources.getDrawable(R.drawable.ic_mood_happy_sel_48dp, null))
            }
            Running.Mood.Just_fine -> {
                holder.mood.setImageDrawable(context.resources.getDrawable(R.drawable.ic_mood_neutral_sel_48dp))
            }
            Running.Mood.Sad -> {
                holder.mood.setImageDrawable(context.resources.getDrawable(R.drawable.ic_mood_sad_sel_48dp))
            }
        }*/

    }

    fun f(p1: Int, p2: Int) {}

    override fun getItemCount(): Int {
        return items.size
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position)
    }

    fun restoreItem(item: Running, position: Int) {
        items.add(position, item)
        // notify item added by position
        notifyItemInserted(position)
    }

    class RunningViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val date = itemView.date
        val distance = itemView.distance
        val duration = itemView.time
        val speed = itemView.speed
        val viewForeground = itemView.view_foreground
        val viewBackground = itemView.view_background
        //val mood = itemView.mood

    }

    interface RunningAdapterListener {
        fun onSelect(itemView: View, running: Running)
    }
}