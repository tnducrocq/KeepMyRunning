package fr.tnducrocq.keepmyrunning.ui.running

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import fr.tnducrocq.keepmyrunning.R
import fr.tnducrocq.keepmyrunning.model.Running
import kotlinx.android.synthetic.main.time_fragment.*


class TimeFragment : Fragment() {

    companion object {
        fun newInstance() = TimeFragment()
    }

    private lateinit var viewModel: TimeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.time_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(TimeViewModel::class.java)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tmpRunning = arguments?.getParcelable<Running>("running")
        var userId = FirebaseAuth.getInstance().currentUser!!.uid
        var running = if (tmpRunning == null) Running(userId) else tmpRunning!!

        viewModel.str.observe(this, Observer {
            timer_hour.text = it.substring(0, 2)
            timer_min.text = it.substring(2, 4)
            timer_sec.text = it.substring(4, 6)
        })

        viewModel.empty.observe(this, Observer {
            next.visibility = if (it) FloatingActionButton.INVISIBLE else FloatingActionButton.VISIBLE
            backspace.isEnabled = !it
        })

        viewModel.init()

        backspace.setOnClickListener {
            viewModel.remove()
        }

        next.setOnClickListener {
            running.apply {
                time = viewModel.time()
            }

            val direction = TimeFragmentDirections.actionNavigationNewRunningToDistanceFragment(running)
            Navigation.findNavController(it).navigate(direction)
        }

        keyboard.setKeyboardButtonClickedListener {
            viewModel.add(it.buttonValue.toString().toCharArray()[0])
        }
    }
}
