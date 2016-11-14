import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class cubes {

    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/rubiksDB";
    static final String USER = "andrew";
    static final String PASSWORD = "pickles";
    private static Scanner s = new Scanner(System.in);
    public static void main(String[] args) {

        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Can't instantiate driver class; check you have drivers and classpath configured correctly?");
            cnfe.printStackTrace();
            System.exit(-1);
        }
        //open connection in try block
        try (Connection conn = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD);
             Statement statement = conn.createStatement()) {

//            String createTableSQL = "CREATE TABLE IF NOT EXISTS cubes (cube_solver varchar(30), solve_time FLOAT)";
//            statement.executeUpdate(createTableSQL);
//            System.out.println("Created Cubes table");
//
//            String addDataSQL = "INSERT INTO cubes VALUES ('Cubestormer II Robot', 5.270)";
//            statement.executeUpdate(addDataSQL);
//
//            addDataSQL = "INSERT INTO cubes VALUES ('Fakhri Raihaan', 27.93)";
//            statement.executeUpdate(addDataSQL);
//
//            addDataSQL = "INSERT INTO cubes VALUES ('Ruxin Liu', 99.33)";
//            statement.executeUpdate(addDataSQL);
//
//            addDataSQL = "INSERT INTO cubes VALUES ('Mats Valk', 6.27)";
//            statement.executeUpdate(addDataSQL);

        statement.close();
        conn.close();

        } catch (SQLException se) {
        se.printStackTrace();
        }
        // create array list of current cube solvers to search
        ArrayList<String> solvers = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD);
             Statement statement = conn.createStatement()) {
            String query = "SELECT cube_solver FROM cubes";
            ResultSet rs = statement.executeQuery(query);
            //while loop adds each found item to the solvers array list created
            while (rs.next()) {
                solvers.add(rs.getString("cube_solver"));
            }
            //close connection and statement
            rs.close();
            conn.close();
        } catch (SQLException SQLe) {
            System.out.println(SQLe);
        }
        //get input from user for entering and/or updating times
        System.out.println("Do you have a new puzzle solving time to enter (y for yes)?");
        String again = s.nextLine();
        //while block allows user to enter multiple new solvers and/or times
        while (again.equalsIgnoreCase("y")) {
            System.out.println("Please enter the name of the solver: ");
            String solver = s.nextLine();
            if (solvers.contains(solver)) {
                System.out.println("Please enter a new time for " + solver + ":");
                float newTime = s.nextFloat();
                //calls addtime function for existing users
                addTime(solver, newTime);
                System.out.println("Time updated for " + solver + "!");
            } else {
                System.out.println("Please enter the solve time for " + solver);
                float newTime = s.nextFloat();
                //calls different function for new users
                addNewSovlerTime(solver, newTime);
                System.out.println(solver + " has been added with a time of " + newTime);
            }
            //get input from user for entering new times or solvers
            System.out.println("Do you have another solver or time to enter (y for yes)?");
            again = s.nextLine();
        }
    }
    //new solver and time function
    private static void addNewSovlerTime(String solver, float newTime) {
        //open connection with try block
        try (Connection conn = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD);
             Statement statement = conn.createStatement()) {

            String query = "INSERT INTO cubes (cube_solver, solve_time) VALUES ('" + solver +"', " + newTime + ")";
            statement.execute(query);
            //close statement and connection
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

            String query = "UPDATE cubes SET solve_time = " + newTime + " WHERE cube_solver = '" + solver + "'";
            statement.execute(query);
            //close statement and connection
            statement.close();
            conn.close();
        }
        catch (SQLException SQLe) {
            System.out.println(SQLe);
        }
    }
}
