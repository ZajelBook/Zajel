package com.bernovia.zajel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bernovia.zajel.addBook.AddBookFragment
import com.bernovia.zajel.bookList.ui.BookListFragment
import com.bernovia.zajel.databinding.ActivityMainBinding
import com.bernovia.zajel.dialogs.DialogUtil
import com.bernovia.zajel.helpers.BroadcastReceiversUtil.TOKEN_MISMATCH
import com.bernovia.zajel.helpers.BroadcastReceiversUtil.registerTheReceiver
import com.bernovia.zajel.helpers.FragmentSwitcher
import com.bernovia.zajel.helpers.FragmentSwitcher.replaceFragmentWithNoAnimation
import com.bernovia.zajel.messages.ui.MessagesListFragment
import com.bernovia.zajel.notificationsList.ui.NotificationsListFragment
import com.bernovia.zajel.profile.ProfileFragment
import com.bernovia.zajel.requests.ui.RequestsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private val tokenMissMatchReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            DialogUtil.showTokenMismatch(supportFragmentManager)
        }
    }

    companion object {
        lateinit var bottomNavigationView: BottomNavigationView
        lateinit var fabButton: FloatingActionButton

    }

    @Suppress("SENSELESS_COMPARISON") fun checkIntent() {
        if (intent.extras != null) {
            if (intent.extras!!.getString("type") != null) {
                when (intent.extras!!.getString("type")) {
                    "request_accepted", "borrow_request", "request_rejected" -> {
                        binding.addBookFAB.visibility = View.GONE
                        replaceFragmentWithNoAnimation(supportFragmentManager, R.id.main_content_frameLayout, RequestsFragment.newInstance())
                        binding.bottomNavigation.selectedItemId = R.id.navigation_requests
                    }
                    "new_message" -> {
                        if (intent.extras!!.getString("conversation_id") != null) {
                            FragmentSwitcher.addFragment(supportFragmentManager,
                                R.id.added_FrameLayout,
                                MessagesListFragment.newInstance(intent.extras!!.getString("conversation_id")!!.toInt()),
                                FragmentSwitcher.AnimationType.PUSH)

                        }
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        bottomNavigationView = binding.bottomNavigation
        fabButton = binding.addBookFAB
        registerTheReceiver(tokenMissMatchReceiver, TOKEN_MISMATCH)

        binding.addBookFAB.setOnClickListener {
            FragmentSwitcher.addFragment(supportFragmentManager, R.id.added_FrameLayout, AddBookFragment.newInstance(0), FragmentSwitcher.AnimationType.PUSH)

        }
        replaceFragmentWithNoAnimation(supportFragmentManager, R.id.main_content_frameLayout, BookListFragment.newInstance())

        checkIntent()

        val navigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener = object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(@NonNull item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.navigation_home -> {
                        if (binding.bottomNavigation.selectedItemId != item.itemId) {
                            binding.addBookFAB.visibility = View.VISIBLE
                            replaceFragmentWithNoAnimation(supportFragmentManager, R.id.main_content_frameLayout, BookListFragment.newInstance())

                        }
                        return true
                    }
                    R.id.navigation_requests -> {
                        if (binding.bottomNavigation.selectedItemId != item.itemId) {
                            binding.addBookFAB.visibility = View.GONE
                            replaceFragmentWithNoAnimation(supportFragmentManager, R.id.main_content_frameLayout, RequestsFragment.newInstance())
                        }
                        return true
                    }

                    R.id.navigation_notifications -> {
                        if (binding.bottomNavigation.selectedItemId != item.itemId) {
                            binding.addBookFAB.visibility = View.GONE
                            replaceFragmentWithNoAnimation(supportFragmentManager, R.id.main_content_frameLayout, NotificationsListFragment.newInstance())
                        }
                        return true
                    }

                    R.id.navigation_profile -> {
                        if (binding.bottomNavigation.selectedItemId != item.itemId) {
                            binding.addBookFAB.visibility = View.GONE
                            replaceFragmentWithNoAnimation(supportFragmentManager, R.id.main_content_frameLayout, ProfileFragment.newInstance())
                        }
                        return true
                    }

                }
                return false
            }
        }
        binding.bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener)

    }


}
