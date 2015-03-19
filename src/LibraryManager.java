import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LibraryManager {

    private Connection connection;

    public LibraryManager()
    {
        connection = null;
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
        connection = DriverManager.getConnection(url, username, password);
        System.out.println ("Welcome to the CS5530 Library\nFor help, type in help.");
    }

    public void close()
    {
        try {
            if (connection != null)
            {
                connection.close();
                System.out.println("Connection to database closed.");
            }
        }
        catch (Exception e)
        {
            System.out.println("Error disconnecting.");
            e.printStackTrace();
        }
    }

    public int registerUser(String[] params)
    {
        return registerUser(params[0],params[1],params[2],params[3],params[4]);
    }

    public int registerUser(String userName, String address, String name, String phone, String email)
    {
        try
        {
            String query = "insert into `cs5530db26`.`user` (`username`," +
                         "`address`,`full_name`,`phone_no`,`email`)" +
                         " values(?,?,?,?,?)";

            PreparedStatement insert = connection.prepareStatement(query);

            insert.setString(1, userName);
            insert.setString(2, address);
            insert.setString(3, name);
            insert.setString(4, phone);
            insert.setString(5, email);

            int result = insert.executeUpdate();
            insert.close();

            return result;
        }
        catch(MySQLIntegrityConstraintViolationException ex)
        {
            return -1;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -2;
        }
    }

    public int requestBook(String username, String isbn)
    {
        try
        {
            String query = "select * from inventory where isbn like ? and availability<>-1";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, isbn);
            ResultSet resultSet = statement.executeQuery();

            int bookNumber = -1;
            while(resultSet.next())
            {
                bookNumber = resultSet.getInt("book_no");
                if(resultSet.getInt("availability") == 1)
                {

                }
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return -1;
    }

    public int requestBook(String[] params)
    {
        if(params.length == 2)
        {
            return requestBook(params[0], params[1]);
        }
        else if (params.length > 3)
        {

        }

        return -1;
    }
}
