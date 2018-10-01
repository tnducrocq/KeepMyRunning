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
import kotlinx.android.synthetic.main.distance_fragment.*


class DistanceFragment : Fragment() {

    companion object {
        fun newInstance() = DistanceFragment()
    }

    private lateinit var viewModel: DistanceViewModel
    private lateinit var running: Running

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.distance_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(DistanceViewModel::class.java)

        val tmpRunning = arguments?.getParcelable<Running>("running")
        var userId = FirebaseAuth.getInstance().currentUser!!.uid
        running = if (tmpRunning == null) Running(userId) else tmpRunning!!
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.str.observe(this, Observer {
            distance_km.text = it.substring(0, 2)
            distance_kmdec.text = it.substring(2, 4)
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
            running.distance = viewModel.distance()

            val direction = DistanceFragmentDirections.actionDistanceFragmentToMoodFragment(running)
            Navigation.findNavController(it).navigate(direction)
        }

        keyboard.setKeyboardButtonClickedListener {
            viewModel.add(it.buttonValue.toString().toCharArray()[0])
        }

    }
}
