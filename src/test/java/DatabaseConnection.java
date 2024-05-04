import java.io.IOException;
import java.lang.ref.Cleaner;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DatabaseConnection /*implements AutoCloseable */ {
    /*
    @Override
    public void close() throws Exception {
        cleanable.clean();
    }

    static class CleaningAction implements Runnable {

        private Connection connection;

        public CleaningAction(Connection connection) {
            this.connection = connection;
        }

        @Override
        public void run() {
            System.out.println("Cleaner runninng");
            try {
                if(connection != null && !connection.isClosed())
                {
                    connection.close();
                    connection = null;
                    System.out.println("Connection closed");
                }
            } catch (SQLException e) {
                System.out.println("SQL exception occured");
            }
        }
    }
    */
    private static String connectionString;
    private Connection conn;
    //private Cleaner.Cleanable cleanable;
    //private final Cleaner cleaner;
    private static volatile DatabaseConnection connection;
    private static Object mutex = new Object();
    //private DatabaseConnection(Cleaner cleaner, String connectionString) throws SQLException {
    private DatabaseConnection( String connectionString) throws SQLException {
        //this.cleaner = cleaner;
        conn = DriverManager.getConnection(connectionString);
        //cleanable = cleaner.register(this, new CleaningAction(conn));
        System.out.println("Is connected: " + (!conn.isClosed()));

    }
    public static void setConnectionString(String connectString)
    {
        connectionString = connectString;
    }
    //public static DatabaseConnection getInstance(Cleaner cleaner, String connectionStr) throws SQLException {
    public static DatabaseConnection getInstance() throws SQLException {
        DatabaseConnection result = connection;
        if (result == null) {
            synchronized (mutex) {
                result = connection;
                if (result == null)
                    //connection = result = new DatabaseConnection(cleaner,connectionStr);
                    connection = result = new DatabaseConnection(connectionString);

            }
        }
        return result;
    }

    public void manualConnectionClose() throws SQLException {
        synchronized (mutex) {
            connection.conn.close();
            connection = null;
        }
    }

    /*
    public void register(DatabaseConnection dbc, Connection connection) {
        this.cleanable = cleaner.register(dbc, new CleaningAction(connection));
    }
    */

    public List<DBShoppingList> getListsOfUserWithId(int userId) throws SQLException {
        System.out.println("get lists of user with id");
        String query = "Select * from shopping_list_shoppingList where owner_id=?;";
        PreparedStatement stmnt = connection.conn.prepareStatement(query);
        stmnt.setInt(1, userId);
        //ResultSet rs0 = stmnt.executeQuery("Select * from shopping_list_normaluser where invitehash='f55930a80f';");
        /*while (rs0.next())
        {
            System.out.println("User data: nick: " + rs0.getString("nick") + ", id: " + Integer.toString(rs0.getInt("id")));

        }*/

        ResultSet rs = stmnt.executeQuery();
        List<DBShoppingList> shoppingLists = new LinkedList<>();
        ResultSetMetaData rsmeta = rs.getMetaData();

        while (rs.next())
        {
            String name = rs.getString("name");
            int id = (int)rs.getLong("id");
            Date date = rs.getDate("create_date");
            int ownerId = (int)rs.getLong("owner_id");
            shoppingLists.add( new DBShoppingList(id, ownerId, date.toLocalDate(), name) );
        }
        return  shoppingLists;
    }
}
