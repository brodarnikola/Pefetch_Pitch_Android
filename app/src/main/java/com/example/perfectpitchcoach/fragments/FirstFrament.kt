package kotlincodes.com.viewpagerkotlin.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.perfectpitchcoach.R


class FirstFrament : Fragment() {
    /* companion object {
        fun newInstance(message: String): FirstFrament {

            val f = FirstFrament()

            val bdl = Bundle(1)

            bdl.putString(EXTRA_MESSAGE, message)

            f.setArguments(bdl)

            return f

        }
    } */


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View? = inflater.inflate(R.layout.fragment_introduction_first, container, false);


        //val message = arguments!!.getString(EXTRA_MESSAGE)

        //var textView: TextView = view!!.findViewById(R.id.tvLetsStart)
        //textView!!.text = message

        return view
    }


}