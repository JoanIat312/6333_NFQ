package net.dankito.richtexteditor.android.extensions

import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.RelativeLayout
import net.dankito.richtexteditor.android.AndroidCommandView
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.toolbar.EditorToolbar
import net.dankito.richtexteditor.android.toolbar.IFloatingView
import net.dankito.richtexteditor.command.ToolbarCommand
import net.dankito.utils.android.animation.ShowHideViewAnimator
import net.dankito.utils.android.extensions.executeActionAfterMeasuringSize
import net.dankito.utils.android.extensions.getLocationOnScreenY
import net.dankito.utils.android.extensions.isVisible


private val animator = ShowHideViewAnimator()


fun IFloatingView.initializeView(editor: RichTextEditor, command: ToolbarCommand) {
    this.editor = editor
    this.command = command

    editor.viewTreeObserver.addOnGlobalLayoutListener {
        hasEditorHeightChanged = editor.measuredHeight != lastEditorHeight // most probably due to keyboard show/hide
    }

    findToolbar(command.commandView as? AndroidCommandView)?.let { toolbar ->
        this.toolbar = toolbar
        toolbar.addCommandInvokedListener { commandInvoked(it) }

        addToLayout(editor, toolbar)
    }
}

private fun IFloatingView.commandInvoked(command: ToolbarCommand) {
    if(isVisible() && command != this.command) { // another command has been invoked but the SelectValueView is visible -> hide
        hideView()
    }
}

fun IFloatingView.isVisible(): Boolean {
    return (this as? View)?.isVisible() == true
}


fun IFloatingView.updatePosition() {
    (this as? View)?.let { view ->
        editor?.let { editor ->
            toolbar?.let { toolbar ->
                view.y =
                        if(isToolbarBelowEditor(editor, toolbar)) {
                            getToolbarTopMinusEditorTop(editor, toolbar) - measuredHeight
                        }
                        else {
                            toolbar.bottom.toFloat()
                        }
            }
        }
    }
}


fun IFloatingView.findToolbar(commandView: AndroidCommandView?): EditorToolbar? {
    var parent = commandView?.view?.parent

    while(parent != null) {
        if(parent is EditorToolbar) {
            return parent
        }

        parent = parent.parent
    }

    return null
}

fun IFloatingView.addToLayout() {
    editor?.let { editor ->
        toolbar?.let { toolbar ->
            addToLayout(editor, toolbar)
        }
    }
}

fun IFloatingView.addToLayout(editor: RichTextEditor, toolbar: EditorToolbar) {
    (this as? View)?.let { view ->
        findParentViewGroup(editor)?.let { toolbarParent ->
            toolbarParent.addView(view)

            (view.layoutParams as? RelativeLayout.LayoutParams)?.let { layoutParams ->
                if(isToolbarBelowEditor(editor, toolbar)) {
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                }
                else {
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP)
                }
            }
        }
    }
}

private fun findParentViewGroup(view: View): ViewGroup? {
    var parent = view.parent

    while(parent != null) {
        if(parent is ViewGroup) {
            return parent
        }

        parent = parent.parent
    }

    return null
}


fun IFloatingView.calculateOnMeasure(heightMeasureSpec: Int) : Int {
    var adjustedHeight = heightMeasureSpec

    editor?.let { editor ->
        if(lastEditorHeight != editor.measuredHeight) {
            adjustedHeight = View.MeasureSpec.makeMeasureSpec(editor.measuredHeight, View.MeasureSpec.AT_MOST)
        }
        else if(setMaxHeightOnNextMeasurement) {
            setMaxHeightOnNextMeasurement = false
            adjustedHeight = View.MeasureSpec.makeMeasureSpec(editor.measuredHeight, View.MeasureSpec.AT_MOST)
        }
    }

    return adjustedHeight
}

fun IFloatingView.richTextEditorChanged(editor: RichTextEditor?) { // TODO: remove OnGlobalLayoutListener from previous RichTextEditor (but that editor changes currently should never be the case)
    editor?.let { editor ->
        editor.viewTreeObserver.addOnGlobalLayoutListener {
            if(lastEditorHeight != editor.measuredHeight) {
                lastEditorHeight = editor.measuredHeight

                updatePosition()
            }
        }
    }
}


fun IFloatingView.showView() {
    (this as? View)?.let { view ->
        view.visibility = android.view.View.VISIBLE
        setMaxHeightOnNextMeasurement = true
        editor?.bringChildToFront(view)

        animateShowView()
    }
}

fun IFloatingView.hideView() {
    (this as? View)?.let { view ->
        editor?.let { editor ->
            toolbar?.let { toolbar ->
                val isToolbarBelowEditor = isToolbarBelowEditor(editor, toolbar)

                var startPosition = view.top
                if(startPosition == 0) { // then toolbar is most likely embedded into another view -> get startPosition from location on screen
                    startPosition = view.getLocationOnScreenY() - (view.parent as View).getLocationOnScreenY()
                }

                val endPosition = if(isToolbarBelowEditor) startPosition + view.measuredHeight else startPosition - view.measuredHeight

                playAnimation(view, false, startPosition.toFloat(), endPosition.toFloat())
            }
        }
    }
}


fun IFloatingView.animateShowView() {
    (this as? View)?.let { view ->
        view.executeActionAfterMeasuringSize(hasEditorHeightChanged) {
            animateShowViewAfterMeasuringHeight(view)
        }

        hasEditorHeightChanged = false
    }
}

private fun IFloatingView.animateShowViewAfterMeasuringHeight(view: View) {
    editor?.let { editor ->
        toolbar?.let { toolbar ->
            val isToolbarBelowEditor = isToolbarBelowEditor(editor, toolbar)

            if(isToolbarBelowEditor) {
                val startPosition = getToolbarTopMinusEditorTop(editor, toolbar)
                val endPosition = startPosition - view.measuredHeight

                playAnimation(view, true, startPosition, endPosition)
            }
            else {
                val endPosition = 0f
                val startPosition = endPosition - view.measuredHeight

                playAnimation(view, true, startPosition, endPosition)
            }
        }
    }
}

private fun getToolbarTopMinusEditorTop(editor: RichTextEditor, toolbar: EditorToolbar): Float {
    var toolbarTop = getToolbarTop(toolbar)

    (editor.parent as? ViewGroup)?.let { editorParent -> // adjustment needed if above editor is (are) another view(s) -> subtract that view(s)' height
        toolbarTop -= editorParent.top
    }

    return toolbarTop
}

private fun getToolbarTop(toolbar: EditorToolbar): Float {
    var toolbarTop = toolbar.top
    var parent: ViewParent? = toolbar

    while(toolbarTop == 0 && parent != null) { // then toolbar is most likely embedded into another view -> get parent view's top
        (parent.parent as? ViewGroup)?.let { parentsParent ->
            toolbarTop = parentsParent.top
            parent = parentsParent
        }
    }

    return toolbarTop.toFloat()
}

private fun playAnimation(view: View, show: Boolean, yStart: Float, yEnd: Float, animationEndListener: (() -> Unit)? = null) {
    animator.playVerticalAnimation(view, show, yStart, yEnd, animationEndListener = animationEndListener)
}


fun IFloatingView.isToolbarBelowEditor(): Boolean {
    editor?.let { editor ->
        toolbar?.let { toolbar ->
            return isToolbarBelowEditor(editor, toolbar)
        }
    }

    return false
}

fun IFloatingView.isToolbarBelowEditor(editor: RichTextEditor, toolbar: EditorToolbar): Boolean {
    val editorY = editor.getLocationOnScreenY()
    val toolbarY = toolbar.getLocationOnScreenY()

    return toolbarY > editorY
}