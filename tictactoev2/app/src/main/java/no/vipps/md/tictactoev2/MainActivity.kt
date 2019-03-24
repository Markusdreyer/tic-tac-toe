package no.vipps.md.tictactoev2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import no.vipps.md.tictactoev2.fragments.StartupFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        displayStartupFragment()
    }

    fun displayStartupFragment() {
        val startupFragment = StartupFragment.newInstance()
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager
            .beginTransaction()

        //Adding a fragment to fragment_container
        fragmentTransaction.replace(R.id.fragment_container, startupFragment).addToBackStack(null).commit()

    }

}
