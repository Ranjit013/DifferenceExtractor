package com.example.differenceextractor;

import com.intellij.codeInsight.hint.HintManager;
import com.intellij.diff.FrameDiffTool;
import com.intellij.diff.tools.simple.SimpleDiffChange;
import com.intellij.diff.tools.simple.SimpleDiffViewer;
import com.intellij.diff.tools.util.DiffDataKeys;
import com.intellij.diff.util.DiffUtil;
import com.intellij.diff.util.Side;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;

import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DifferenceExtractor extends DumbAwareAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        FrameDiffTool.DiffViewer diffViewer = event.getRequiredData(DiffDataKeys.DIFF_VIEWER);
        EditorEx editor = null;
        List<String> lines = new ArrayList<>();
        if(diffViewer instanceof SimpleDiffViewer){
            editor = ((SimpleDiffViewer) diffViewer).getEditor2();
            Document document = ((SimpleDiffViewer) diffViewer).getContent2().getDocument();
            List<SimpleDiffChange> diffChanges = ((SimpleDiffViewer) diffViewer).getDiffChanges();
            lines = diffChanges.stream().flatMap(diffChange -> DiffUtil.getLines(document, diffChange.getStartLine(Side.RIGHT), diffChange.getEndLine(Side.RIGHT)).stream()).collect(Collectors.toList());
        }
        StringSelection selection = new StringSelection(String.join("\n", lines));
        CopyPasteManager.getInstance().setContents(selection);
        assert editor != null;
        HintManager.getInstance().showInformationHint(editor, "Copied!");
}

    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
    }
}