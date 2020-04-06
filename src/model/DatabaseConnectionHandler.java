package model;

import javafx.util.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


/**
 * This class handles all database related transactions
 */
public class DatabaseConnectionHandler {
    // Use this version of the ORACLE_URL if you are running the code off of the server
//	private static final String ORACLE_URL = "jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu";
    // Use this version of the ORACLE_URL if you are tunneling into the undergrad servers
    private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private String username = "";

    private Connection connection = null;

    private static DatabaseConnectionHandler instance;
    static{ instance = new DatabaseConnectionHandler(); }

    public static DatabaseConnectionHandler getInstance() {
        return instance;
    }

    private DatabaseConnectionHandler() {
        try {
            // Load the Oracle JDBC driver
            // Note that the path could change for new drivers
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    private void insertData(PreparedStatement ps, List<TableEntry> values) throws SQLException {
            insertData(ps, values, 0);
    }

    private void insertData(PreparedStatement ps, List<TableEntry> values, int offset) throws SQLException {
        for(int i=1; i <= values.size(); i++) {
            TableEntry entry = values.get(i-1);
            switch(entry.getType()) {
                case Types.INTEGER:
                    ps.setInt(i+offset, Integer.parseInt(entry.getContents()));
                    break;
                case Types.CHAR:
                    ps.setString(i+offset, entry.getContents());
                    break;
                case Types.NUMERIC:
                case Types.DOUBLE:
                    ps.setDouble(i+offset, Double.parseDouble(entry.getContents()));
                    break;
            }
        }
    }

    public void delete(String tableName, List<TableEntry> values) {
        List<String> equals = new ArrayList<>();
        for (TableEntry value : values) {
            if(value.getType() == 1)
                equals.add(value.getColumnName()+" LIKE (?)");
            else
                equals.add(value.getColumnName()+"=(?)");
        }
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM " + tableName + " WHERE "+String.join(",",equals));
            insertData(ps, values);
            ps.executeUpdate();
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insert(String tableName, List<TableEntry> values) {
        StringBuilder questionMarks = new StringBuilder();
        questionMarks.append("(");
        for(int i=0; i < values.size()-1; i++) {
            questionMarks.append("?,");
        }
        questionMarks.append("?)");
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO " + tableName + " VALUES "+questionMarks.toString());
            insertData(ps, values);
            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void update(String tableName, List<TableEntry> dataList, List<TableEntry> whereList) {
        List<String> updateData = new ArrayList<>();
        for (TableEntry tableEntry : dataList) {
            updateData.add(tableEntry.getColumnName() + "=(?)");
        }

        List<String> equals = new ArrayList<>();
        for (TableEntry value : whereList) {
            if(value.getType() == 1)
                equals.add(value.getColumnName()+" LIKE (?)");
            else
                equals.add(value.getColumnName()+"=(?)");
        }
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE " + tableName
                    + " SET "+String.join(",",updateData)
                    + " WHERE "+String.join(",",equals));
            insertData(ps, dataList);
            insertData(ps, whereList, dataList.size());
            ps.executeUpdate();
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public boolean login(String username, String password) {
        try {
            if (connection != null) {
                return true;
            }
            connection = DriverManager.getConnection(ORACLE_URL, username, password);
            connection.setAutoCommit(false);
            this.username = username;
            return true;
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            return false;
        }
    }

    private void rollbackConnection() {
        try  {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public void databaseSetup() {
        runFile("resources/sql/drop.sql", (s,e)->{});
        runFile("resources/sql/create.sql", (s,e) -> {
            System.out.println(s);
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        });
    }

    private void runFile(String filepath, BiConsumer<String, SQLException> errorHandler) {
        try {
            String statements = new String(Files.readAllBytes(Paths.get(filepath)));
            for (String s : statements.split(";")) {
                if(s.trim().length() > 0) {
                    try {
                        Statement stmt = connection.createStatement();
                        stmt.execute(s);
                        stmt.close();
                    } catch (SQLException e) {
                        errorHandler.accept(s, e);
                    }
                }
            }
            try {
                List<String> tables = getTables();
                for (Consumer<List<String>> listener : listeners) {
                    listener.accept(tables);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ResultSet runQuery(String query) throws SQLException {
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(query);
    }

    private List<String> getTables() throws SQLException {
        DatabaseMetaData md = connection.getMetaData();
        ResultSet rs = md.getTables(null, username.toUpperCase(), "%", null);
        List<String> tables = new ArrayList<>();
        while (rs.next()) {
            tables.add(rs.getString(3));
        }
        return tables;
    }

    private List<Consumer<List<String>>> listeners = new ArrayList<>();
    public void onSetup(Consumer<List<String>> listener) {
            listeners.add(listener);
    }

    public List<Pair<String, Integer>> getColumns(String tableName) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM \"" + tableName + "\"");
        ResultSetMetaData md = resultSet.getMetaData();

        List<Pair<String, Integer>> columns = new ArrayList<>();
        for(int i = 1; i <= md.getColumnCount(); i++) {
            columns.add(new Pair<>(md.getColumnName(i), md.getColumnType(i)));
        }
        return columns;
    }
}
