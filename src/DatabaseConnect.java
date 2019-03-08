import javax.swing.*;import java.sql.*;public class DatabaseConnect {    String DATABASE = "DBNAMEHERE"; // replace "DBNAMEHERE" with the name you wish to call your database    private static DatabaseConnect handler = null; //This statement to store an instance of the database after its instantiated below    //these are the details the program will use to connect to your database    //in my case, I'm using xampp, a local database, and haven't changed the default login credentials    //the default username is root and no password. thats what I've passed to the program to be    //able to 'log itself into the database'    // N.B PROVIDE THESE DETAILS AS PER YOUR DATABASE    String db_url = "jdbc:mysql://localhost:3306/";    String userPass = "?user=root&password=";    String username = "root";    String password = "";    private static Connection conn = null;    private static Statement stmt = null;    //class constructor    private DatabaseConnect(){        createDatabase();        createConnection();        setupProfilesTable();        setupOutboxTable();    }    //instantiation of the database object    public static DatabaseConnect getInstance()    {        if(handler == null){            handler = new DatabaseConnect();        }        return handler;    }    //method to log program to mysql database in xampp.    void createConnection(){        try{            Class.forName("com.mysql.jdbc.Driver").newInstance();            conn = DriverManager.getConnection(db_url+DATABASE,username,password);        }catch(ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException e){            JOptionPane.showMessageDialog(null,"Could not load database","Database Error",JOptionPane.ERROR_MESSAGE);            System.exit(0);        }    }    //method to create database with the database name provided in Variable 'DATABASE'    void createDatabase(){        try {            Class.forName("com.mysql.jdbc.Driver");            conn = DriverManager.getConnection(db_url+userPass);            stmt = conn.createStatement();            stmt.execute("CREATE DATABASE IF NOT EXISTS " + DATABASE );        } catch (ClassNotFoundException | SQLException e){            System.out.println("UNABLE TO CREATE DATABASE");            e.printStackTrace();        }    }    //method to create a table in the created database (in my case am creating database    // tables for working with Diafaan SMS server)    void setupProfilesTable(){        String TABLE_NAME = "PROFILES";        try {            System.out.println("Checking if table " + TABLE_NAME + " exists");            stmt = conn.createStatement();            DatabaseMetaData dbm = conn.getMetaData();            ResultSet tables = dbm.getTables(null,null,TABLE_NAME.toUpperCase(),null);            System.out.println("Initializing . . .");            if (tables.next()){                System.out.println("Table " +TABLE_NAME + " already exists!");            }            else {                stmt.execute("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +                       " id varchar(10) primary key,\n" +                       " fname varchar(50),\n" +                       " lname varchar(50),\n" +                       " mobileNumber varchar(25)\n" +                       " )");            }        } catch (SQLException e){            System.err.println(e.getMessage() + " -->> Error creating table" + TABLE_NAME);        } finally {        }    }    //method to set up outbox table    void setupOutboxTable() {        String TABLE_NAME = "OUTBOX";        try {            System.out.println("Checking if table " + TABLE_NAME + " exists");            stmt = conn.createStatement();            DatabaseMetaData dbm = conn.getMetaData();            ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);            System.out.println("Initializing . . .");            if (tables.next()) {                System.out.println("Table " + TABLE_NAME + " already exists!");            } else {                stmt.execute("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("                        + " Id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,\n"                        + " MessageTo VARCHAR(80),\n"                        + " MessageFrom VARCHAR(80),\n"                        + " MessageText VARCHAR(800),\n"                        + " MessageType VARCHAR(80),\n"                        + " Gateway VARCHAR(80),\n"                        + " UserId VARCHAR(80),\n"                        + " UserInfo TEXT,\n"                        + " Priority INT,\n"                        + " Scheduled DATETIME,\n"                        + " ValidityPeriod INT,\n"                        + " IsSent TINYINT(1) NOT NULL DEFAULT 0,\n"                        + " IsRead TINYINT(1) NOT NULL DEFAULT 0"                        + " )");            }        } catch (SQLException e) {            System.err.println(e.getMessage() + " -->> Error creating table" + TABLE_NAME);        } finally {        }    }    //research on how to create index using code.}