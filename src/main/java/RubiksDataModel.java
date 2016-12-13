import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RubiksDataModel extends AbstractTableModel{
    private int rowCount = 0;
    private int colCount = 0;
    ResultSet resultSet;

    public RubiksDataModel(ResultSet rs) {
        this.resultSet = rs;
        setup();
    }

    private void setup() {

        countRows();

        try {
            colCount = resultSet.getMetaData().getColumnCount();

        } catch (SQLException se) {
            System.out.println("Error counting columns" + se);
        }

    }

    public void updateResultSet(ResultSet newRS) {
        resultSet = newRS;
        setup();
    }

    public boolean deleteRow(int row){
        try {
            resultSet.absolute(row + 1);
            resultSet.deleteRow();
            //Tell table to redraw itself
            fireTableDataChanged();
            return true;
        }catch (SQLException se) {
            System.out.println("Delete row error " + se);
            return false;
        }
    }


    private void countRows() {
        rowCount = 0;
        try {

            resultSet.beforeFirst();
            while (resultSet.next()) {
                rowCount++;
            }
            resultSet.beforeFirst();

        } catch (SQLException se) {
            System.out.println("Error counting rows " + se);
        }

    }

    @Override
    public int getRowCount() {
        countRows();
        return rowCount;
    }

    @Override
    public int getColumnCount() {
        return colCount;
    }

    @Override
    public Object getValueAt(int row, int col) {
        try {
            //  System.out.println("get value at, row = " +row);
            resultSet.absolute(row + 1);
            Object o = resultSet.getObject(col + 1);
            return o.toString();
        } catch (SQLException se) {
            System.out.println(se);
            //se.printStackTrace();
            return se.toString();

        }
    }

    @Override

    public void setValueAt(Object newValue, int row, int col) {

        float newTime;

        try {
            newTime = Float.parseFloat(newValue.toString());

            if (newTime < cubes.solve_time || newTime > cubes.solve_time) {
                throw new NumberFormatException("");
            }
        } catch (NumberFormatException ne) {
            JOptionPane.showMessageDialog(null, "Try entering a number between 1 and 10000");
            return;
        }

        //This only happens if the new time is valid
        try {
            resultSet.absolute(row + 1);
            resultSet.updateFloat(2, newTime);
            resultSet.updateRow();
            fireTableDataChanged();
        } catch (SQLException e) {
            System.out.println("error changing time " + e);
        }
    }
}