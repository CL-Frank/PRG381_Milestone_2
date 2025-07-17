package com.bcwellness.view;

import com.bcwellness.model.Counselor;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class CounselorTableModel extends AbstractTableModel {
    private List<Counselor> counselors;
    private String[] columnNames = {"ID", "Name", "Email", "Phone", "Specialization"};

    public void setCounselors(List<Counselor> counselors) {
        this.counselors = counselors;
        fireTableDataChanged();
    }

    public Counselor getCounselorAt(int row) {
        return counselors.get(row);
    }

    @Override
    public int getRowCount() {
        return counselors == null ? 0 : counselors.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int row, int column) {
        Counselor c = counselors.get(row);
        switch (column) {
            case 0: return c.getId();
            case 1: return c.getName();
            case 2: return c.getEmail();
            case 3: return c.getPhone();
            case 4: return c.getSpecialization();
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}