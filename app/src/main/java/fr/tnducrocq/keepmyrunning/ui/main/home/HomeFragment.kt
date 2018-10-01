package fr.tnducrocq.keepmyrunning.ui.main.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import fr.tnducrocq.keepmyrunning.R
import fr.tnducrocq.keepmyrunning.provider.Outcome
import fr.tnducrocq.keepmyrunning.ui.running.detail.RunningDetailActivity
import kotlinx.android.synthetic.main.home_fragment.*
import java.text.SimpleDateFormat


class HomeFragment : Fragment(), RunningItemTouchHelper.RecyclerItemTouchHelperListener {

    private lateinit var viewModel: HomeViewModel

    private var adapter: RunningAdapter? = null

    private val TAG = "HomeFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        floatingActionButton.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.add_running_action)
        }

        refreshlayout.setOnRefreshListener {
            var userId = FirebaseAuth.getInstance().currentUser!!.uid
            viewModel.load(userId)
        }

        recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && floatingActionButton.visibility === View.VISIBLE) {
                    floatingActionButton.hide()
                } else if (dy < 0 && floatingActionButton.visibility !== View.VISIBLE) {
                    floatingActionButton.show()
                }
            }
        })

        val itemTouchHelperCallback = RunningItemTouchHelper(0, ItemTouchHelper.LEFT, this)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerview)


        var userId = FirebaseAuth.getInstance().currentUser!!.uid
        refreshlayout.isRefreshing = true
        viewModel.load(userId)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.runningList.observe(this, Observer {
            when (it) {
                is Outcome.Failure -> {
                    refreshlayout.isRefreshing = false
                    Log.d(TAG, it.error.message)
                    Snackbar.make(recyclerview, it.error.message!!, Snackbar.LENGTH_LONG).show()
                }
                is Outcome.Success -> {
                    refreshlayout.isRefreshing = false
                    adapter = RunningAdapter(it.data, context!!)
                    adapter!!.setRunningAdapterListener { view, running ->
                        val intent = Intent(activity, RunningDetailActivity::class.java)
                        intent.putExtra("running", running)
                        startActivity(intent)
                    }
                    recyclerview.adapter = adapter
                }
            }
        })

    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        if (viewHolder is RunningAdapter.RunningViewHolder) {
            // get the removed item name to display it in snack bar
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
            val name = sdf.format(viewModel.list.get(viewHolder.getAdapterPosition()).date)

            // backup of removed item for undo purpose
            val deletedItem = viewModel.list.get(viewHolder.getAdapterPosition())
            val deletedIndex = viewHolder.getAdapterPosition()
            var undo = false

            // remove the item from recycler view
            adapter!!.removeItem(viewHolder.getAdapterPosition())

            // showing snack bar with Undo option
            val snackbar = Snackbar.make(recyclerview, name + " removed from cart!", Snackbar.LENGTH_LONG)
            snackbar.setAction("UNDO") {
                // undo is selected, restore the deleted item
                undo = true
                adapter!!.restoreItem(deletedItem, deletedIndex)
            }
            snackbar.addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    if (!undo) {
                        viewModel.delete(deletedItem.id!!)
                    }
                }
            })
            snackbar.setActionTextColor(Color.YELLOW)
            snackbar.show()
        }
    }

}
