package model;


import javafx.scene.control.TextField;

public class ColumnListing {
    private String columnName;
    private Integer type;
    private TextField input;

    public ColumnListing(String columnName, Integer type, TextField input) {
        this.columnName = columnName;
        this.type = type;
        this.input = input;
    }

    public String getColumnName() {
        return columnName;
    }

    public Integer getType() {
        return type;
    }

    public TextField getInput() {
        return input;
    }
}
