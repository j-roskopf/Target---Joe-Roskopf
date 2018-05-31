package com.target.dealbrowserpoc.dealbrowser.detail

import android.os.Build
import android.support.annotation.RequiresApi
import android.support.transition.ChangeBounds
import android.support.transition.ChangeImageTransform
import android.support.transition.ChangeTransform
import android.support.transition.TransitionSet

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class DetailsTransition : TransitionSet() {
    init {
        ordering = ORDERING_TOGETHER
        addTransition(ChangeBounds()).addTransition(ChangeTransform()).addTransition(ChangeImageTransform())
    }
}