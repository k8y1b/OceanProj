package model;

import javafx.util.Pair;

import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.EventListener;
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
    private static final String WARNING_TAG = "[WARNING]";
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

//    public void deleteBranch(int branchId) {
//        try {
//            PreparedStatement ps = connection.prepareStatement("DELETE FROM branch WHERE branch_id = ?");
//            ps.setInt(1, branchId);
//
//            int rowCount = ps.executeUpdate();
//            if (rowCount == 0) {
//                System.out.println(WARNING_TAG + " Branch " + branchId + " does not exist!");
//            }
//
//            connection.commit();
//
//            ps.close();
//        } catch (SQLException e) {
//            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
//            rollbackConnection();
//        }
//    }

//    public void insertBranch(BranchModel model) {
//        try {
//            PreparedStatement ps = connection.prepareStatement("INSERT INTO branch VALUES (?,?,?,?,?)");
//            ps.setInt(1, model.getId());
//            ps.setString(2, model.getName());
//            ps.setString(3, model.getAddress());
//            ps.setString(4, model.getCity());
//            if (model.getPhoneNumber() == 0) {
//                ps.setNull(5, java.sql.Types.INTEGER);
//            } else {
//                ps.setInt(5, model.getPhoneNumber());
//            }
//
//            ps.executeUpdate();
//            connection.commit();
//
//            ps.close();
//        } catch (SQLException e) {
//            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
//            rollbackConnection();
//        }
//    }

//    public BranchModel[] getBranchInfo() {
//        ArrayList<BranchModel> result = new ArrayList<BranchModel>();
//
//        try {
//            Statement stmt = connection.createStatement();
//            ResultSet rs = stmt.executeQuery("SELECT * FROM branch");
//
////    		// get info on ResultSet
////    		ResultSetMetaData rsmd = rs.getMetaData();
////
////    		System.out.println(" ");
////
////    		// display column names;
////    		for (int i = 0; i < rsmd.getColumnCount(); i++) {
////    			// get column name and print it
////    			System.out.printf("%-15s", rsmd.getColumnName(i + 1));
////    		}
//
//            while(rs.next()) {
//                BranchModel model = new BranchModel(rs.getString("branch_addr"),
//                        rs.getString("branch_city"),
//                        rs.getInt("branch_id"),
//                        rs.getString("branch_name"),
//                        rs.getInt("branch_phone"));
//                result.add(model);
//            }
//
//            rs.close();
//            stmt.close();
//        } catch (SQLException e) {
//            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
//        }
//
//        return result.toArray(new BranchModel[result.size()]);
//    }

//    public void updateBranch(int id, String name) {
//        try {
//            PreparedStatement ps = connection.prepareStatement("UPDATE branch SET branch_name = ? WHERE branch_id = ?");
//            ps.setString(1, name);
//            ps.setInt(2, id);
//
//            int rowCount = ps.executeUpdate();
//            if (rowCount == 0) {
//                System.out.println(WARNING_TAG + " Branch " + id + " does not exist!");
//            }
//
//            connection.commit();
//
//            ps.close();
//        } catch (SQLException e) {
//            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
//            rollbackConnection();
//        }
//    }

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
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);
        ResultSetMetaData md = resultSet.getMetaData();

        List<Pair<String, Integer>> columns = new ArrayList<>();
        for(int i = 0; i < md.getColumnCount(); i++) {
            columns.add(new Pair<>(md.getColumnName(i), md.getColumnType(i)));
        }
        return columns;
    }
}
