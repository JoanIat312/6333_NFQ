package net.dankito.richtexteditor

import net.dankito.utils.Color


abstract class CommandView {

    abstract var appliedTintColor: Color


    abstract fun setIsEnabled(isEnabled: Boolean)

    abstract fun setBackgroundColor(color: Color)

    abstract fun getParentBackgroundColor(): Color?

    abstract fun setTintColor(color: Color)

}