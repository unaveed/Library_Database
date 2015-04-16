import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class User {
    private boolean _badRequest;
    private String _username;
    private String _address;
    private String _name;
    private String _phone;
    private String _email;
    private Connection _connection;

    public User(String parameters)
    {
        _connection = DatabaseSingleton.getInstance().getConnection();
        _badRequest = false;
        initialize(parameters);
    }

    private void initialize(String input)
    {
        String[] parameters = input.split(Constant.DELIMITER);

        if(parameters.length == 1)
            _username = parameters[0];
        else if(parameters.length == 5)
        {
            _username = parameters[0];
            _address = parameters[1];
            _name = parameters[2];
            _phone = parameters[3];
            _email = parameters[4];
        }
        else
            _badRequest = true;
    }

    public Result register()
    {
        if(!_badRequest)
        {
            try
            {
                String query = "insert into `cs5530db26`.`user` (`username`," +
                        "`address`,`full_name`,`phone_no`,`email`)" +
                        " values(?,?,?,?,?)";

                PreparedStatement insert = _connection.prepareStatement(query);

                insert.setString(1, _username);
                insert.setString(2, _address);
                insert.setString(3, _name);
                insert.setString(4, _phone);
                insert.setString(5, _email);

                insert.executeUpdate();
                insert.close();
                _connection.close();

                return new Result("User successfully registered.\n");
            }
            catch (Exception e)
            {
                return new Result(e.getMessage());
            }
        }

        return new Result(Constant.BAD_REQUEST);
    }


    public Result getPersonalData()
    {
        if(!_badRequest)
        {
            try
            {
                String query = "select * from user where username=?";
                PreparedStatement statement = _connection.prepareStatement(query);

                statement.setString(1, _username);
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
                _connection.close();

                return new Result(result);
            }
            catch(Exception e)
            {
                return new Result(e.getMessage());
            }
        }

        return new Result(Constant.BAD_REQUEST);
    }

    public Result getCheckedOutList()
    {
        if(!_badRequest)
        {
            try
            {
                _connection.close();
                return null;
            }
            catch (Exception e)
            {
                return new Result(e.getMessage());
            }
        }

        return new Result(Constant.BAD_REQUEST);
    }

    public Result getLostBookList()
    {
        if(!_badRequest)
        {
            try
            {
                _connection.close();
                return null;
            }
            catch (Exception e)
            {
                return new Result(e.getMessage());
            }
        }

        return new Result(Constant.BAD_REQUEST);
    }

    public Result getWaitList()
    {
        if(!_badRequest)
        {
            try
            {
                _connection.close();
                return null;
            }
            catch (Exception e)
            {
                return new Result(e.getMessage());
            }
        }

        return new Result(Constant.BAD_REQUEST);
    }

    public Result getReviews()
    {
        if(!_badRequest)
        {
            try
            {
                _connection.close();
                return null;
            }
            catch (Exception e)
            {
                return new Result(e.getMessage());
            }
        }

        return new Result(Constant.BAD_REQUEST);
    }

    public Result getTopUsersByCheckOut()
    {
        if(!_badRequest)
        {
            try
            {
                _connection.close();
                return null;
            }
            catch (Exception e)
            {
                return new Result(e.getMessage());
            }
        }

        return new Result(Constant.BAD_REQUEST);
    }

    public Result getTopUsersByReviews()
    {
        if(!_badRequest)
        {
            try
            {
                _connection.close();
                return null;
            }
            catch (Exception e)
            {
                return new Result(e.getMessage());
            }
        }

        return new Result(Constant.BAD_REQUEST);
    }

    public Result getTopUsersByLostBooks()
    {
        if (!_badRequest)
        {
            try
            {
                _connection.close();
                return null;
            } catch (Exception e)
            {
                return new Result(e.getMessage());
            }
        }

        return new Result(Constant.BAD_REQUEST);
    }
}
