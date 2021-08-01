package com.local.chatlog.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.local.chatlog.App
import com.local.chatlog.R
import com.local.chatlog.databinding.ActivityLoginBinding
import com.local.chatlog.di.viewmodel.ViewModelFactory
import com.local.chatlog.view.intents.LoginIntent
import com.local.chatlog.view.states.LoginStates
import com.local.chatlog.view.viewmodel.LoginViewModelImpl
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @ExperimentalCoroutinesApi
    private val loginViewModelImpl by lazy {
        ViewModelProvider(this, viewModelFactory).get(LoginViewModelImpl::class.java)
    }

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as App).appComponent.inject(this)
        supportActionBar?.hide()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding()
        observeState()
    }

    private fun binding() {
        binding.buttonLoginLogin.setOnClickListener {
            val email = binding.editTextEmailLogin.text.toString().trim()
            val password = binding.editTextPasswordLogin.text.toString().trim()

            lifecycleScope.launch {
                loginViewModelImpl.userIntent.send(LoginIntent.Login(email, password))
            }

        }
        binding.textViewBackToRegisterLogin
            .setOnClickListener {
                finish()
            }
    }

    @ExperimentalCoroutinesApi
    private fun observeState() {
        lifecycleScope.launch {
            loginViewModelImpl.state.collect {
                when (it) {
                    is LoginStates.Fail -> {
                        FancyToast.makeText(

                            applicationContext,
                            it.e.message,
                            Toast.LENGTH_SHORT,
                            FancyToast.ERROR,
                            false
                        ).show()
                    }
                    is LoginStates.Success -> {
                        val intent = Intent(applicationContext, UserListActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)

                    }
                }
            }
        }
    }
}