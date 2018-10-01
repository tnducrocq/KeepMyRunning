package fr.tnducrocq.keepmyrunning.ui.running

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import fr.tnducrocq.keepmyrunning.R
import fr.tnducrocq.keepmyrunning.model.Running
import fr.tnducrocq.keepmyrunning.provider.Outcome
import kotlinx.android.synthetic.main.mood_fragment.*
import java.text.SimpleDateFormat
import java.util.*


class MoodFragment : Fragment() {

    companion object {
        fun newInstance() = MoodFragment()
    }

    private lateinit var viewModel: MoodViewModel
    private lateinit var running: Running

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.mood_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(MoodViewModel::class.java)

        var tmpRunning = arguments?.getParcelable<Running>("running")
        var userId = FirebaseAuth.getInstance().currentUser!!.uid
        running = if (tmpRunning == null) Running(userId) else tmpRunning!!
        viewModel.init(running)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        time.setOnClickListener {
            val myCalendar = Calendar.getInstance()
            val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val timePicker = TimePickerDialog(context!!, TimePickerDialog.OnTimeSetListener { view, hour, minute ->
                    myCalendar.clear()
                    myCalendar.set(Calendar.YEAR, year)
                    myCalendar.set(Calendar.MONTH, monthOfYear)
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    myCalendar.set(Calendar.HOUR_OF_DAY, hour)
                    myCalendar.set(Calendar.MINUTE, minute)
                    viewModel.setDate(myCalendar.time)
                }, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true)
                timePicker.show()
            }
            val pickerDialog = DatePickerDialog(context!!, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH))
            pickerDialog.show()
        }

        viewModel.date.observe(this, Observer {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
            time.text = sdf.format(it)
        })
        viewModel.setDate(running.date)

        viewModel.mood.observe(this, Observer {
            when (it) {
                null -> {
                    next.visibility = View.GONE
                    mood_good.setImageDrawable(resources.getDrawable(R.drawable.ic_mood_happy_48dp, null))
                    mood_neutral.setImageDrawable(resources.getDrawable(R.drawable.ic_mood_neutral_48dp))
                    mood_sad.setImageDrawable(resources.getDrawable(R.drawable.ic_mood_sad_48dp))
                }
                Running.Mood.Good -> {
                    next.visibility = View.VISIBLE
                    mood_good.setImageDrawable(resources.getDrawable(R.drawable.ic_mood_happy_sel_48dp, null))
                    mood_neutral.setImageDrawable(resources.getDrawable(R.drawable.ic_mood_neutral_48dp))
                    mood_sad.setImageDrawable(resources.getDrawable(R.drawable.ic_mood_sad_48dp))
                }
                Running.Mood.Just_fine -> {
                    next.visibility = View.VISIBLE
                    mood_good.setImageDrawable(resources.getDrawable(R.drawable.ic_mood_happy_48dp, null))
                    mood_neutral.setImageDrawable(resources.getDrawable(R.drawable.ic_mood_neutral_sel_48dp))
                    mood_sad.setImageDrawable(resources.getDrawable(R.drawable.ic_mood_sad_48dp))
                }
                Running.Mood.Sad -> {
                    next.visibility = View.VISIBLE
                    mood_good.setImageDrawable(resources.getDrawable(R.drawable.ic_mood_happy_48dp, null))
                    mood_neutral.setImageDrawable(resources.getDrawable(R.drawable.ic_mood_neutral_48dp))
                    mood_sad.setImageDrawable(resources.getDrawable(R.drawable.ic_mood_sad_sel_48dp))
                }
            }
        })

        mood_good.setOnClickListener {
            viewModel.toogleMood(Running.Mood.Good)
        }
        mood_neutral.setOnClickListener {
            viewModel.toogleMood(Running.Mood.Just_fine)
        }
        mood_sad.setOnClickListener {
            viewModel.toogleMood(Running.Mood.Sad)
        }

        next.setOnClickListener {
            running.apply {
                mood = viewModel.mood.value!!
            }
            //TODO run wait indicator
            viewModel.save()
        }

        viewModel.saveResult.observe(this, Observer {
            when (it) {
                is Outcome.Failure -> {
                    Snackbar.make(view, it.error.message!!, Snackbar.LENGTH_LONG).show()
                }
                is Outcome.Success -> {
                    Snackbar.make(view, "Course enregistrée!", Snackbar.LENGTH_LONG).show()
                    activity?.finish()
                }
            }
        })
    }

}
