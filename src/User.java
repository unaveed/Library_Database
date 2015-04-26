import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class User {
    private Connection _connection;

    public User(Connection connection)
    {
        _connection = connection;
    }

    public Result register(String username, String address, String name, String phone, String email)
    {
        try
        {
            String query = "insert into `cs5530db26`.`user` (`username`," +
                    "`address`,`full_name`,`phone_no`,`email`)" +
                    " values(?,?,?,?,?)";

            PreparedStatement insert = _connection.prepareStatement(query);

            insert.setString(1, username);
            insert.setString(2, address);
            insert.setString(3, name);
            insert.setString(4, phone);
            insert.setString(5, email);

            insert.executeUpdate();
            insert.close();

            return new Result(Constant.SUCCESS);
        }
        catch (Exception e)
        {
            return new Result(e.getMessage());
        }
    }


    public Result getPersonalData(String username)
    {
        try
        {
            String query = "select * from user where username=?";
            PreparedStatement statement = _connection.prepareStatement(query);

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if(!resultSet.isBeforeFirst())
                return new Result(Constant.NO_MATCH);

            resultSet.next();
            String result = "username: " + resultSet.getString("username") + "\n" +
                    "name: " + resultSet.getString("full_name") + "\n" +
                    "address: " + resultSet.getString("address") + "\n" +
                    "phone: " + resultSet.getString("phone_no") + "\n" +
                    "email: " + resultSet.getString("email") + "\n";

            statement.close();

            return new Result(result);
        }
        catch(Exception e)
        {
            return new Result(e.getMessage());
        }

    }

    public Result getCheckedOutList()
    {
        try
        {
            return null;
        }
        catch (Exception e)
        {
            return new Result(e.getMessage());
        }
    }


    public Result getLostBookList()
    {
        try
        {
            return null;
        }
        catch (Exception e)
        {
            return new Result(e.getMessage());
        }
    }

    public Result getWaitList()
    {
        try
        {
            return null;
        }
        catch (Exception e)
        {
            return new Result(e.getMessage());
        }
    }

    public Result getReviews()
    {
        try
        {
            return null;
        }
        catch (Exception e)
        {
            return new Result(e.getMessage());
        }
    }

    public Result getTopUsersByCheckOut()
    {
        try
        {
            return null;
        }
        catch (Exception e)
        {
            return new Result(e.getMessage());
        }
    }

    public Result getTopUsersByReviews()
    {
        try
        {
            return null;
        }
        catch (Exception e)
        {
            return new Result(e.getMessage());
        }
    }

    public Result getTopUsersByLostBooks()
    {
        try
        {
            return null;

        } catch (Exception e)
        {
            return new Result(e.getMessage());
        }
    }
}
