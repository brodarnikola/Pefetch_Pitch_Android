package kotlincodes.com.viewpagerkotlin.fragments

import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.perfect_pitch_trainer.settings.AppPreferences
import com.example.perfectpitchcoach.R
import com.example.perfectpitchcoach.activities.LoginActivity
import com.example.perfectpitchcoach.activities.RegisterActivity


class ThirdFragment : Fragment(){

    lateinit var btnRegisterActivity: Button
    lateinit var btnLoginActivity: Button

    companion object {
        fun newInstance(message: String): ThirdFragment {

            val f = ThirdFragment()

            val bdl = Bundle(1)

            bdl.putString(AlarmClock.EXTRA_MESSAGE, message)

            f.setArguments(bdl)

            return f

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view : View? = inflater.inflate(R.layout.fragment_introduction_third, container, false);

        var textView: TextView =view!!.findViewById(R.id.tvLetsStart)
        textView!!.text="THIRD FRAGMENT"

        btnRegisterActivity = view!!.findViewById(R.id.btnRegisterActivity)
        btnLoginActivity = view!!.findViewById(R.id.btnLoginActivity)

        btnRegisterActivity.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {

                if (!AppPreferences.firstRun) {
                    AppPreferences.firstRun = true
                    Log.d("Thrre Fragment", "The value of our pref is: ${AppPreferences.firstRun}")
                }

                val intent = Intent (getActivity(), RegisterActivity::class.java)
                getActivity()?.finish()
                getActivity()?.startActivity(intent)
            }
        })

        btnLoginActivity.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {

                if (!AppPreferences.firstRun) {
                    AppPreferences.firstRun = true
                    Log.d("Thrre Fragment", "The value of our pref is: ${AppPreferences.firstRun}")
                }

                val intent = Intent (getActivity(), LoginActivity::class.java)
                getActivity()?.finish()
                getActivity()?.startActivity(intent)
            }
        })



        return view
    }


}