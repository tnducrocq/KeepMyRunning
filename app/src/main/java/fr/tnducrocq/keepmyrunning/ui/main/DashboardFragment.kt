/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.tnducrocq.keepmyrunning.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import fr.tnducrocq.keepmyrunning.R
import fr.tnducrocq.keepmyrunning.provider.Outcome
import kotlinx.android.synthetic.main.dashboard.*
import kotlinx.android.synthetic.main.main_activity.*

/**
 * Presents how multiple steps flow could be implemented.
 */
class DashboardFragment : Fragment() {

    private val TAG = "DashboardFragment"

    private lateinit var viewModel: DashboardViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)

        viewModel.distance.observe(this, Observer {
            val distanceValue = it / 1000F
            distance.text = "%.2f km".format(distanceValue)
        })

        viewModel.duration.observe(this, Observer {
            val timeValue = it / 1000
            val s = timeValue % 60
            val m = timeValue / 60 % 60
            val h = timeValue / (60 * 60) % 24

            duration_hour.text = if (h == 0L) "0" else "%d".format(h)
            duration_min.text = "%02d".format(m)
            duration_sec.text = "%02d".format(s)
        })


        viewModel.mustDuration.observe(this, Observer {
            val timeValue = it / 1000
            val s = timeValue % 60
            val m = timeValue / 60 % 60
            val h = timeValue / (60 * 60) % 24

            must_duration_hour.text = if (h == 0L) "0" else "%d".format(h)
            must_duration_min.text = "%02d".format(m)
            must_duration_sec.text = "%02d".format(s)
        })

        viewModel.mostSpeed.observe(this, Observer {
            most_speed.text = it
        })

        var userId = FirebaseAuth.getInstance().currentUser!!.uid
        viewModel.load(userId)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val activity = activity as MainActivity
        activity.stateful.showLoading(R.string.label_loading_running)

        viewModel.result.observe(this, Observer {
            when (it) {
                is Outcome.Success -> {
                    activity.stateful.showContent()
                }
                is Outcome.Failure -> {
                    activity.stateful.showError(it.error.localizedMessage, View.OnClickListener {
                        var userId = FirebaseAuth.getInstance().currentUser!!.uid
                        viewModel.load(userId)
                    })
                }
            }
        })


    }
}
