package giavu.hoangvm.hh.activity.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import giavu.hoangvm.hh.R
import giavu.hoangvm.hh.activity.main.MainActivity
import giavu.hoangvm.hh.activity.register.RegisterActivity
import giavu.hoangvm.hh.databinding.ActivityLoginBinding
import giavu.hoangvm.hh.dialog.DialogFactory
import giavu.hoangvm.hh.dialog.hideProgress
import giavu.hoangvm.hh.dialog.showProgress
import giavu.hoangvm.hh.tracker.Event
import giavu.hoangvm.hh.tracker.FirebaseTracker
import giavu.hoangvm.hh.utils.Status
import org.koin.android.ext.android.inject


class LoginActivity : AppCompatActivity() {

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

    private val tracker: FirebaseTracker by inject()

    private val viewModel: LoginViewModel by inject()

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
    }

    private fun initialize() {
        initializeDataBinding()
        observeViewModel()
    }

    private fun observeViewModel() {
        with(viewModel) {
            showProgressRequest.observe(this@LoginActivity, Observer { showProgress() })
            hideProgressRequest.observe(this@LoginActivity, Observer { hideProgress() })
            status.observe(this@LoginActivity, Observer { state ->
                when (state) {
                    is Status.Success -> onLoginComplete()
                    is Status.Failure -> onError(state.throwable)
                }
            })
            registerEvent.observe(this@LoginActivity, Observer { toRegister() })
            loginByGuestEvent.observe(this@LoginActivity, Observer {
                startActivity(MainActivity.createIntent(this@LoginActivity))
                this@LoginActivity.finish()
            })
        }
    }

    private fun initializeDataBinding() {
        binding = DataBindingUtil.setContentView<ActivityLoginBinding>(
            this@LoginActivity, R.layout.activity_login
        )
            .apply {
                viewModel = this@LoginActivity.viewModel
                setLifecycleOwner(this@LoginActivity)
            }

    }

    private fun toRegister() {
        startActivity(RegisterActivity.createIntent(this@LoginActivity))
        finish()
        tracker.track(Event.IMPSRegister)
    }

    private fun onLoginComplete() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
        tracker.track(Event.LoginSuccess)
    }

    private fun onError(error: Throwable) {
        tracker.track(Event.LoginFailure)
        DialogFactory().create(this@LoginActivity, error)
    }
}
