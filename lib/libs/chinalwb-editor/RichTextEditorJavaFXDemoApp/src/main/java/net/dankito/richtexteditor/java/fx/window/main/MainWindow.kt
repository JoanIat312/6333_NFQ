package net.dankito.richtexteditor.java.fx.window.main

import javafx.geometry.Insets
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import net.dankito.richtexteditor.java.fx.RichTextEditor
import net.dankito.richtexteditor.java.fx.toolbar.EditorToolbar
import net.dankito.richtexteditor.java.fx.toolbar.GroupedCommandsEditorToolbar
import tornadofx.*


class MainWindow : View(FX.messages["application.title"]) {

    private lateinit var editor: RichTextEditor

    private lateinit var toolbar: EditorToolbar


    override val root = vbox {
        prefWidth = 800.0
        prefHeight = 450.0

        toolbar = GroupedCommandsEditorToolbar()
//        toolbar = AllCommandsEditorToolbar()
        toolbar.root.background = Background(BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY))
        add(toolbar)

        editor = RichTextEditor()
        add(editor)

        toolbar.editor = editor

        editor.vboxConstraints {
            vGrow = Priority.ALWAYS
        }
    }

}