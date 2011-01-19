package org.openl.rules.table.actions.style;

import org.openl.rules.table.IGridTable;
import org.openl.rules.table.IWritableGrid;
import org.openl.rules.table.actions.AUndoableCellAction;
import org.openl.rules.table.ui.ICellStyle;

public class SetFillColorAction extends AUndoableCellAction {

    private short[] prevColor;
    private short[] newColor;

    public SetFillColorAction(int col, int row, short[] color) {
        super(col, row);
        this.newColor = color;
    }

    public void doAction(IGridTable table) {
        IWritableGrid grid = (IWritableGrid) table.getGrid();

        ICellStyle style = grid.getCell(getCol(), getRow()).getStyle();
        prevColor = style != null ? style.getFillForegroundColor() : null;

        grid.setCellFillColor(getCol(), getRow(), newColor);
    }

    public void undoAction(IGridTable table) {
        IWritableGrid grid = (IWritableGrid) table.getGrid();
        grid.setCellFillColor(getCol(), getRow(), prevColor);
    }

}
