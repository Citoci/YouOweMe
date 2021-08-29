package com.cito.youoweme.ui

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.pow

class FadeableTextView : AppCompatTextView, CoordinatorLayout.AttachedBehavior {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    override fun getBehavior(): CoordinatorLayout.Behavior<*> = FadeableBehavior()

    class FadeableBehavior : CoordinatorLayout.Behavior<View> {
        constructor() : super()
        constructor(context: Context, attrs: AttributeSet?): super(context, attrs)

        override fun layoutDependsOn(
            parent: CoordinatorLayout,
            child: View,
            dependency: View
        ) = (dependency is AppBarLayout).also {
            Log.d(this::class.simpleName, "p: ${parent.tag}, c: ${child.tag}, d: ${dependency.tag}")
        }
//                return super.layoutDependsOn(parent, child, dependency)

        override fun onDependentViewChanged(
            parent: CoordinatorLayout,
            child: View,
            dependency: View
        ): Boolean {
            if (dependency !is AppBarLayout) return false

//            Log.d(this::class.simpleName, "${dependency.y}/${dependency.totalScrollRange}")
            child.alpha = (with(dependency) { y/totalScrollRange } + 1).pow(4)

            return true
        }

        override fun onDependentViewRemoved(
            parent: CoordinatorLayout,
            child: View,
            dependency: View
        ) {}
    }

}