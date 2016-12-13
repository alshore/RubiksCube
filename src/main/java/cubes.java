import com.sun.xml.internal.bind.v2.model.core.ID;

import java.sql.*;

public class cubes {

    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/rubiksDB";
    static final String DB_NAME = "cubes";
    static final String USER = "andrew";
    static final String PASSWORD = "pickles";

    static Statement statement = null;
    static Connection conn = null;
    static ResultSet rs = null;

    public final static String RUBIKS_SOLVER_TABLE = "cubes";
    public final static String PK_COLUMN = "id";

    public final static String NAME_COLUMN = "solver";
    public final static String TIME_COLUMN = "time";

    public final static float SOLVE_TIME_MIN = 0;
    public final static float SOLVE_TIME_MAX = 10000;

    public static void main(String args[]) {

        if (!setup()) {
            System.exit(-1);
        }

        if (!loadAllTimes()) {
            System.exit(-1);
        }
        rubiksGui tableGUI = new rubiksGui(rubiksDataModel);
    }

    public static boolean loadAllTimes(){

        try{

            if (rs!=null) {
                rs.close();
            }

            String getAllData = "SELECT * FROM CUBES";
            rs = statement.executeQuery(getAllData);

            if (rubiksDataModel == null) {
                rubiksDataModel = new RubiksDataModel(rs);
            } else {
                rubiksDataModel.updateResultSet(rs);
            }

            return true;

        } catch (Exception e) {
            System.out.println("Could not load content");
            System.out.println(e);
            e.printStackTrace();
            return false;
        }

    }

    public static boolean setup(){
        try {
            try {
                String Driver = "com.mysql.jdbc.Driver";
                Class.forName(Driver);
            } catch (ClassNotFoundException cnfe) {
                System.out.println("No database drivers found. Quitting");
                return false;
            }

            conn = DriverManager.getConnection(DB_CONNECTION_URL + DB_NAME, USER, PASSWORD);

            statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //if not exists, create table.
            if (!cubesTableExists()) {

                //Create a table in the database with 3 columns: Movie title, year and rating
                String createTableSQL = "CREATE TABLE " + RUBIKS_SOLVER_TABLE + " (" + PK_COLUMN + " int NOT NULL AUTO_INCREMENT, " + NAME_COLUMN + " varchar(50), " + TIME_COLUMN + " float, " +  PK_COLUMN + PK_COLUMN)));
                System.out.println(createTableSQL);
                statement.executeUpdate(createTableSQL);

                System.out.println("Created movie_reviews table");
                String addDataSQL = "INSERT INTO " + RUBIKS_SOLVER_TABLE + "(" + NAME_COLUMN + ", " + TIME_COLUMN + ")" + " VALUES ('', 5.65)";
                statement.executeUpdate(addDataSQL);
                addDataSQL = "INSERT INTO " + RUBIKS_SOLVER_TABLE +  "(" + NAME_COLUMN + ", " + TIME_COLUMN + ", " + " VALUES('', 4.85)";
                statement.executeUpdate(addDataSQL);
                addDataSQL = "INSERT INTO " + RUBIKS_SOLVER_TABLE +  "(" + NAME_COLUMN + ", " + TIME_COLUMN + ", " + " VALUES ('', 12.5)";
                statement.executeUpdate(addDataSQL);
            }
            return true;

        } catch (SQLException se) {
            System.out.println(se);
            se.printStackTrace();
            return false;
        }
    }

    private static boolean cubesTableExists() throws SQLException {

        String checkTablePresentQuery = "SHOW TABLES LIKE '" + RUBIKS_SOLVER_TABLE + "'";
        ResultSet tablesRS = statement.executeQuery(checkTablePresentQuery);
        if (tablesRS.next()) {
            return true;
        }
        return false;

    }
    //new solver and time function
    private static void addNewSolverTime(String solver, float newTime) {
        //open connection with try block
        try (Connection conn = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD);
             Statement statement = conn.createStatement()) {

            String prepInsertSQL = "INSERT INTO cubes (cube_solver, solve_time) VALUES ( ? , ? )";
            PreparedStatement psInsert = conn.prepareStatement(prepInsertSQL);
            psInsert.setString(1, solver);
            psInsert.setFloat(2, newTime);
            psInsert.executeUpdate();
            //close statement and connection
            psInsert.close();
            statement.close();
            conn.close();
        }
        catch (SQLException SQLe) {
            System.out.println(SQLe);
        }
    }
    //existing solver and new time
    private static void addTime(String solver, float newTime) {
        //open connection with try block
        try (Connection conn = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD);
             Statement statement = conn.createStatement()) {

            String prepUpdateSQL = "UPDATE cubes SET solve_time = ( ? )WHERE cube_solver = ( ? )";
            PreparedStatement psInsert = conn.prepareStatement(prepUpdateSQL);
            psInsert.setFloat(1, newTime);
            psInsert.setString(2, solver);
            psInsert.executeUpdate();
            //close statement and connection
            psInsert.close();
            statement.close();
            conn.close();
        }
        catch (SQLException SQLe) {
            System.out.println(SQLe);
        }
    }

    public static void shutdown(){
        try {
            if (rs != null) {
                rs.close();
                System.out.println("Result set closed");
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }

        try {
            if (statement != null) {
                statement.close();
                System.out.println("Statement closed");
            }
        } catch (SQLException se){
            //Closing the connection could throw an exception too
            se.printStackTrace();
        }

        try {
            if (conn != null) {
                conn.close();
                System.out.println("Database connection closed");
            }
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    private static RubiksDataModel rubiksDataModel;
}
