import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by Umair on 4/15/2015.
 */
public final class DatabaseSingleton
{
    private static Connection _connection = null;
    private static DatabaseSingleton _database = null;

    private DatabaseSingleton()
    {
        try
        {
            connect();
        }
        catch(Exception e)
        {
            System.out.println("Could not connect to database");
            e.printStackTrace();

            System.exit(0);
        }
    }

    private void connect() throws Exception
    {
        final String username = "cs5530u26";
        final String password = "ugum6goq";
        final String url = "jdbc:mysql://georgia.eng.utah.edu/cs5530db26";

        Class.forName ("com.mysql.jdbc.Driver").newInstance ();

        if(_connection == null)
            _connection = DriverManager.getConnection(url, username, password);
    }

    public static synchronized DatabaseSingleton getInstance()
    {
        if(_database == null)
            _database = new DatabaseSingleton();

        System.out.println(_database);
        return _database;
    }

    public Connection getConnection()
    {
        return _connection;
    }

    public void close()
    {
        try {
            if (_connection != null)
            {
                _connection.close();
                System.out.println("Connection to database closed.");
            }
        }
        catch (Exception e)
        {
            System.out.println("Error disconnecting.");
            e.printStackTrace();
        }
    }
}
