package org.openl.rules.ui.tablewizard;

/**
 * @author Aliaksandr Antonik.
 */
public class TableWizardManager extends TableWizard{

    static enum TableType {
        UNKNOWN,
        DECISION,
        DATATYPE,
        TEST,
        TEST_DIRECT,
        PROPERTY
    }

    private TableType tableType = TableType.DECISION;

    @Override
    public String cancel() {
        if (wizard != null) {
            wizard.cancel();
        }
        return "newTableCancel";
    }
    
    public String getTableType() {
        return tableType.name();
    }

    public void setTableType(String tableType) {
        try {
            this.tableType = TableType.valueOf(tableType);
        } catch (IllegalArgumentException e) {
            this.tableType = TableType.DECISION;
        }
    }
    
    @Override
    public String start() {
        tableType = TableType.DECISION;
        return "wizardSelect";
    }
    
    @Override
    public String startWizard() {
        reload();
        switch (tableType) {
            case DECISION:
                wizard = new DecisionTableCreationWizard();
                wizard.setStepsCount(6);
                break;
            case DATATYPE:
                wizard = new DatatypeTableCreationWizard();
                wizard.setStepsCount(3);
                break;
            case TEST:
                wizard = new TestTableCreationWizard();
                wizard.setStepsCount(3);
                break;
            case TEST_DIRECT:
                wizard = new TestTableCreationWizardDirect(getElementUri());
                wizard.setStepsCount(2);
                break;
            case PROPERTY:
                wizard = new PropertyTableCreationWizard();
                wizard.setStepsCount(3);
                break;
            default:
                return null;
        }

        String ret = wizard.start();
        wizard.next();
        return ret;
    }
}
