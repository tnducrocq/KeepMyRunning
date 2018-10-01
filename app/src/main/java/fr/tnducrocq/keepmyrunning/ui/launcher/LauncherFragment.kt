package fr.tnducrocq.keepmyrunning.ui.launcher

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import fr.tnducrocq.keepmyrunning.R
import fr.tnducrocq.keepmyrunning.databinding.LauncherFragmentBinding
import fr.tnducrocq.keepmyrunning.provider.Outcome
import fr.tnducrocq.keepmyrunning.ui.main.MainActivity
import kotlinx.android.synthetic.main.launcher_fragment.*


class LauncherFragment : Fragment() {

    private val TAG = LauncherFragment::class.java.simpleName
    private val GOOGLE_SIGN_IN_REQUEST_CODE = 100

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mGoogleApiClient: GoogleApiClient

    companion object {
        fun newInstance() = LauncherFragment()
    }

    private val viewModel: LauncherViewModel by lazy {
        ViewModelProviders.of(this).get(LauncherViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleApiClient = GoogleApiClient.Builder(context!!)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = DataBindingUtil.inflate<LauncherFragmentBinding>(inflater, R.layout.launcher_fragment, container, false)
        binding.viewModel = viewModel
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.init(mAuth)
        viewModel.outcomeUser.observe(this, Observer {
            when (it) {
                is Outcome.Failure -> {
                    sign_in_button.visibility = View.VISIBLE
                }
                is Outcome.Success -> {
                    val intent = Intent(activity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
        })

        sign_in_button.setOnClickListener {
            //viewModel.signInClick()
            val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
            startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE) {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                viewModel.firebaseAuthWithGoogle(mAuth, account)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }


}
