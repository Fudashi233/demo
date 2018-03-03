package cn.edu.jxau.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowCallbackHandler;

public class RowCountCallbackHandlerF implements RowCallbackHandler {

    private int rowCount;
    private int columnCount;
    private String[] columnNames;
    private int[] columnTypes;

    public RowCountCallbackHandlerF() {

    }

    @Override
    public void processRow(ResultSet rs) throws SQLException {

        if (rowCount == 0) {
            ResultSetMetaData metaData = rs.getMetaData();
            columnCount = metaData.getColumnCount();

            columnNames = new String[columnCount];
            columnTypes = new int[columnCount];
            for (int i = 0; i < columnNames.length; i++) {
                columnNames[i] = metaData.getColumnName(i+1);
                columnTypes[i] = metaData.getColumnType(i+1);
            }
            
            
        }
        rowCount++;
    }

    public int[] getColumnTypes() {
        return columnTypes;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }
}
