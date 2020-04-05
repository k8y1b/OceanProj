package model;

public class TableEntry {
    private Integer type;
    private String contents;
    private String columnName;

    public TableEntry(Integer type, String contents, String columnName) {
        this.type = type;
        this.contents = contents;
        this.columnName = columnName;
    }

    public Integer getType() {
        return type;
    }

    public String getContents() {
        return contents;
    }

    public String getColumnName() {
        return columnName;
    }
}
