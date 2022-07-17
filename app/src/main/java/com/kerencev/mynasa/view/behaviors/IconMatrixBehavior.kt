package com.kerencev.mynasa.view.behaviors

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.transition.*
import com.google.android.material.appbar.AppBarLayout
import com.kerencev.mynasa.R

class IconMatrixBehavior(context: Context, attrs: AttributeSet? = null) :
    CoordinatorLayout.Behavior<View>(context, attrs) {

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return dependency.id == R.id.bottomSheet
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        if (dependency.id == R.id.bottomSheet) {
            child.y = dependency.y - child.width / 2
            child.x = (dependency.width - child.width - child.width / 4).toFloat()

            TransitionManager.beginDelayedTransition(parent, Fade())
            if (dependency.y < 70) {
                child.visibility = View.GONE
            } else {
                child.visibility = View.VISIBLE
            }
        }
        return super.onDependentViewChanged(parent, child, dependency)
    }
}