package com.kerencev.mynasa.view.behaviors

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout

class ActionButtonBehavior(context: Context, attrs: AttributeSet?=null) : CoordinatorLayout.Behavior<View>(context, attrs) {

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        if (dependency is AppBarLayout) {
            child.y = dependency.y + dependency.height - child.height / 2
            child.x = (dependency.width - child.width - child.width / 4).toFloat()
            child.alpha = 1 - (kotlin.math.abs(dependency.y) / (dependency.height))
        }
        return super.onDependentViewChanged(parent, child, dependency)
    }
}