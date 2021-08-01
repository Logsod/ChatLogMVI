package com.local.chatlog.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.local.chatlog.App
import com.local.chatlog.R
import com.local.chatlog.databinding.ActivityUserListBinding
import com.local.chatlog.di.viewmodel.ViewModelFactory
import com.local.chatlog.model.ChatUser
import com.local.chatlog.model.FlowUserList
import com.local.chatlog.view.intents.UserListIntent
import com.local.chatlog.view.recyclerviewitems.UserItem
import com.local.chatlog.view.states.UserListStates
import com.local.chatlog.view.viewmodel.UserListViewModelImpl
import com.xwray.groupie.GroupieAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


class UserListActivity : AppCompatActivity() {
    lateinit var binding: ActivityUserListBinding
    lateinit var currentUser: ChatUser
    val adapter = GroupieAdapter()

    companion object {
        const val ROOM_KEY = "ROOM_KEY"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val userListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(UserListViewModelImpl::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        (application as App).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_list)
        if (FirebaseAuth.getInstance().uid == null)
            startRegisterActivity()


        binding()
        observeViewModel()
        observeState()

    }

    private fun binding() {
        binding.recyclerViewUserListUserList.adapter = adapter
        binding.recyclerViewUserListUserList.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        adapter.setOnItemClickListener { item, _ ->
            val row = item as UserItem
            lifecycleScope.launch {

                userListViewModel.userIntent.send(
                    UserListIntent.StartChat(
                        userListViewModel.getCurrentUser(),
                        ChatUser(row.uid, row.name, row.profileImg)
                    )
                )
            }
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            userListViewModel.state.collect {
                when (it) {
                    is UserListStates.EnterRoom -> {
                        val intent = Intent(applicationContext, ChatLogActivity::class.java)
                        intent.putExtra(ROOM_KEY, it.chatRoom)
                        startActivity(intent)
                    }
                    is UserListStates.Fail -> {
                    }
                    is UserListStates.CurrentUserDataReceived -> {
                        userListViewModel.fetchAllUser()
                    }
                    else -> {
                    }
                }
            }
        }
    }

    private fun observeViewModel() {

        userListViewModel.userName.observe(this, Observer {
            supportActionBar?.title = it ?: ""
        })

        userListViewModel.user.observe(this, Observer {
            when (it) {
                is FlowUserList.FlowUserListAddUser -> {
                    if (it.chatUser.uid != userListViewModel.getCurrentUser().uid) {
                        adapter.add(
                            UserItem(
                                it.chatUser.uid,
                                it.chatUser.userName,
                                it.chatUser.userImageUri
                            )
                        )
                    }
                }
                is FlowUserList.FlowUserListRemoveUser -> {
                    loop@ for (i in 0 until adapter.itemCount) {
                        when (val item = adapter.getItem(i)) {
                            is UserItem -> {
                                if (item.uid == it.chatUser.uid) {
                                    adapter.remove(item)
                                    adapter.notifyDataSetChanged()
                                    break@loop
                                }
                            }
                        }
                    }
                }
                else -> {
                }
            }
        })
    }

    private fun startRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
        startRegisterActivity()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_sign_out) {
            signOut()
        }
        return super.onOptionsItemSelected(item)
    }
}