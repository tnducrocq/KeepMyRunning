package fr.tnducrocq.keepmyrunning.ui.running.detail

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import fr.tnducrocq.keepmyrunning.R
import fr.tnducrocq.keepmyrunning.UnitUtils
import fr.tnducrocq.keepmyrunning.model.Running
import kotlinx.android.synthetic.main.running_detail_activity.*
import java.text.SimpleDateFormat

class RunningDetailActivity : AppCompatActivity() {

    lateinit var running: Running

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.running_detail_activity)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        running = if (savedInstanceState == null) intent.getParcelableExtra<Running>("running") else savedInstanceState.getParcelable("running")
        //val running = Running("")//items.get(position)
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm")
        date.text = format.format(running.date)

        val distanceValue = running.distance / 1000F
        distance.text = "%.2f".format(distanceValue)

        val timeValue = running.time / 1000
        val s = timeValue % 60
        val m = timeValue / 60 % 60
        val h = timeValue / (60 * 60) % 24
        time.text = if (h == 0L) "%02d:%02d".format(m, s) else "%d:%02d:%02d".format(h, m, s)

        speed.text = UnitUtils.minAtKm(running.time, running.distance.toInt())

        val speedValue = (running.distance / 1000F) * 3600000L / running.time
        speed_kmh.text = "%.2f".format(speedValue)
        
        when (running.mood) {
            Running.Mood.Good -> {
                mood_good.setImageDrawable(resources.getDrawable(R.drawable.ic_mood_happy_sel_48dp, null))
            }
            Running.Mood.Just_fine -> {
                mood_neutral.setImageDrawable(resources.getDrawable(R.drawable.ic_mood_neutral_sel_48dp))
            }
            Running.Mood.Sad -> {
                mood_sad.setImageDrawable(resources.getDrawable(R.drawable.ic_mood_sad_sel_48dp))
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelable("running", running)
    }
}
