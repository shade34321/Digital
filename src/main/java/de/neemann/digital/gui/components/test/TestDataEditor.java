package de.neemann.digital.gui.components.test;

import de.neemann.digital.core.element.ElementAttributes;
import de.neemann.digital.core.element.Key;
import de.neemann.digital.gui.Main;
import de.neemann.digital.gui.components.EditorFactory;
import de.neemann.digital.lang.Lang;
import de.neemann.gui.ToolTipAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author hneemann
 */
public class TestDataEditor extends EditorFactory.LabelEditor<TestData> {

    private final TestData data;
    private final Key<TestData> key;

    /**
     * Creates a new editor
     *
     * @param data the data to edit
     * @param key  the data key
     */
    public TestDataEditor(TestData data, Key<TestData> key) {
        this.data = new TestData(data);
        this.key = key;
    }

    @Override
    public TestData getValue() {
        return data;
    }

    @Override
    protected JComponent getComponent(ElementAttributes elementAttributes) {
        JPanel panel = new JPanel(new FlowLayout());

        panel.add(new ToolTipAction(Lang.get("btn_edit")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TestDataDialog(panel, data).setVisible(true);
            }
        }.createJButton());

        panel.add(new ToolTipAction(Lang.get("btn_editDetached")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                getAttributeDialog().dispose();
                TestDataDialog dialog = new TestDataDialog(getAttributeDialog().getDialogParent(), data, key, elementAttributes);
                Main main = getAttributeDialog().getMain();
                if (main!=null)
                    main.getWindowPosManager().register("testdata", dialog);
                dialog.setVisible(true);
            }
        }.setToolTip(Lang.get("btn_editDetached_tt"))
                .createJButton());

        return panel;
    }
}
