package fr.tnducrocq.keepmyrunning.ui.running

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import fr.tnducrocq.keepmyrunning.R
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.running_fragment_old.*


class RunningOldFragment : Fragment() {

    companion object {
        fun newInstance() = RunningOldFragment()
    }

    private val disposables = CompositeDisposable()
    private lateinit var viewModel: RunningOldViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.running_fragment_old, container, false)
        viewModel = ViewModelProviders.of(this).get(RunningOldViewModel::class.java)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button.setOnClickListener {
            viewModel.setState()
        }

        viewModel.started.observe(this, Observer {
            if (it) {
                text_action.visibility = View.VISIBLE
                text_timer.visibility = View.VISIBLE
                button.setText(R.string.label_stop_new_running)
            } else {
                text_action.visibility = View.INVISIBLE
                button.setText(R.string.label_start_new_running)
            }
        })

        viewModel.timer.observe(this, Observer {
            text_timer.text = String.format("%02d:%02d:%02d", it / 3600, (it % 3600) / 60, (it % 60))
        })

        viewModel.started.value = false
        viewModel.timer.value = 0
    }

    override fun onStop() {
        super.onStop()
        disposables.clear()
    }
}
