package com.local.chatlog.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.local.chatlog.App
import com.local.chatlog.R
import com.local.chatlog.databinding.ActivityRegisterBinding
import com.local.chatlog.di.viewmodel.ViewModelFactory
import com.local.chatlog.view.intents.RegisterIntent
import com.local.chatlog.view.states.RegisterStates
import com.local.chatlog.view.viewmodel.RegisterViewModelImpl
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

//class RegisterViewModelFactory(val repositoryImp: FirebaseRepositoryImpl) : ViewModelProvider.Factory{
//    @ExperimentalCoroutinesApi
//   override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        if(modelClass.isAssignableFrom(RegisterViewModelImpl::class.java))
//        {
//            return RegisterViewModelImpl(repositoryImp) as T
//        }
//        throw IllegalArgumentException("Unknown class name")
//    }
//}
class RegisterActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @ExperimentalCoroutinesApi
    private val registerViewModel: RegisterViewModelImpl
            by lazy {
                ViewModelProvider(this, viewModelFactory).get(
                    RegisterViewModelImpl::class.java
                )
            }

    var profileImg: Uri? = null
    private lateinit var binding: ActivityRegisterBinding

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as App).appComponent.inject(this)

        //registerViewModel = ViewModelProvider(this,RegisterViewModelFactory(FirebaseRepositoryImpl())).get(RegisterViewModelImpl::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        supportActionBar?.hide()

        binding()

        observeState()

    }

    @ExperimentalCoroutinesApi
    private fun observeState() {
        lifecycleScope.launch {
            registerViewModel.state.collect {
                when (it) {
                    is RegisterStates.Success -> {
                        //"Success $it user id is : ${registerViewModel.getUser().uid}".Log(applicationContext)

                        val intent = Intent(applicationContext, UserListActivity::class.java)
                        startActivity(intent)
                    }
                    is RegisterStates.Fail -> {

                        FancyToast.makeText(
                            applicationContext,
                            it.e.message,
                            FancyToast.LENGTH_SHORT,
                            FancyToast.ERROR,
                            false
                        ).show()
                    }
                    else -> {
                    }
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun binding() {
        binding.textViewAlreadyHaveAccountRegister
            .setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }

        binding.imageViewProfileImageRegister
            .setOnClickListener {
                getContent.launch("image/*")
            }

        binding.buttonRegisterRegister
            .setOnClickListener {
                val userName = binding.textViewUserNameRegister.text.toString().trim()
                val userEmail = binding.textViewEmailRegister.text.toString().trim()
                val userPassword = binding.textViewPasswordRegister.text.toString().trim()
                lifecycleScope.launch {
                    registerViewModel.userIntent.send(
                        RegisterIntent.RegisterWithEmailAndPassword(
                            userName,
                            userEmail,
                            userPassword,
                            profileImg
                        )
                    )
                }
            }
    }

    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        val target = binding.imageViewProfileImageRegister
        if (uri == null) return@registerForActivityResult
        profileImg = uri
        target.setImageURI(uri)
    }


}