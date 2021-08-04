package com.cito.youoweme.ui

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import com.google.android.material.appbar.AppBarLayout

// Not used anymore
// First used to create a textview that disappears when the appbar is collapsed
class CollapsableTextField : AppCompatTextView, CoordinatorLayout.AttachedBehavior {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    override fun getBehavior(): CoordinatorLayout.Behavior<*> {
        return object : CoordinatorLayout.Behavior<TextView?>() {

            override fun layoutDependsOn(
                parent: CoordinatorLayout,
                child: TextView,
                dependency: View
            ): Boolean {
                return dependency is AppBarLayout
//        return super.layoutDependsOn(parent, child, dependency)
            }

            override fun onDependentViewChanged(
                parent: CoordinatorLayout,
                child: TextView,
                dependency: View
            ): Boolean {
                if (dependency !is AppBarLayout)
                    return false

//                Log.d(
//                    "kitemmurt",
//                    """
//                        ${dependency.y}
//                        ${dependency.totalScrollRange}
//                    """.trimIndent()
//                )

                child.visibility = if (dependency.y == 0f) View.VISIBLE else View.INVISIBLE

                return true
            }

            override fun onDependentViewRemoved(
                parent: CoordinatorLayout,
                child: TextView,
                dependency: View
            ) {}
        }
    }
}