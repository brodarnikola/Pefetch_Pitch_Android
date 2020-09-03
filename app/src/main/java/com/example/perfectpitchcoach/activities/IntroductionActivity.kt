package com.example.perfectpitchcoach.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.example.perfect_pitch_trainer.adapters.IntroductionAdapter
import com.example.perfect_pitch_trainer.adapters.ViewPagerListener
import com.example.perfectpitchcoach.R
import kotlincodes.com.viewpagerkotlin.fragments.FirstFrament
import kotlincodes.com.viewpagerkotlin.fragments.SecondFragment
import kotlincodes.com.viewpagerkotlin.fragments.ThirdFragment
import kotlinx.android.synthetic.main.activity_introduction.*

class IntroductionActivity : AppCompatActivity() {

    companion object {
        private const val MIN_SCALE = 0.65f
        private const val MIN_ALPHA = 0.3f
    }

    private lateinit var pagerAdapterView: IntroductionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_introduction)
        pagerAdapterView = IntroductionAdapter(supportFragmentManager)
        addPagerFragments()
        myViewPager.adapter = pagerAdapterView
        myViewPager.setPageTransformer(true, this::zoomOutTransformation)
        myViewPager.addOnPageChangeListener(ViewPagerListener(this::onPageSelected))
    }

    private fun onPageSelected(position: Int) {
        when (position) {
            0 -> {
                firstDotImageView.setImageResource(R.drawable.current_position_icon)
                secondDotImageView.setImageResource(R.drawable.disable_position_icon)
                thirdDotImageView.setImageResource(R.drawable.disable_position_icon)
            }
            1 -> {
                firstDotImageView.setImageResource(R.drawable.disable_position_icon)
                secondDotImageView.setImageResource(R.drawable.current_position_icon)
                thirdDotImageView.setImageResource(R.drawable.disable_position_icon)
            }
            2 -> {
                firstDotImageView.setImageResource(R.drawable.disable_position_icon)
                secondDotImageView.setImageResource(R.drawable.disable_position_icon)
                thirdDotImageView.setImageResource(R.drawable.current_position_icon)
            }
        }
    }

    private fun addPagerFragments() {

        //val adapter = MyFragmentPagerAdapter(getSupportFragmentManager())

        //var firstFragmet: FirstFrament = FirstFrament.newInstance("First Fragment")
        //adapter.addFragment(thirdFragmet, "THREE")

        //viewpager!!.adapter = adapter

        //tabs!!.setupWithViewPager(viewpager)

        pagerAdapterView.addFragments(FirstFrament())
        pagerAdapterView.addFragments(SecondFragment())
        pagerAdapterView.addFragments(ThirdFragment())
    }

    private fun zoomOutTransformation(page: View, position: Float) {
        when {
            position < -1 ->
                page.alpha = 0f
            position <= 1 -> {
                page.scaleX = Math.max(MIN_SCALE, 1 - Math.abs(position))
                page.scaleY = Math.max(MIN_SCALE, 1 - Math.abs(position))
                page.alpha = Math.max(MIN_ALPHA, 1 - Math.abs(position))
            }
            else -> page.alpha = 0f
        }
    }
}
