import java.sql.Connection;

/**
 * Created by Umair on 4/15/2015.
 */
public class Library
{
    private Connection _connection;
    private boolean _badRequest;
    private String _isbn;
    private String _location;
    private String _username;
    private int _copies;

    public Library(String parameters)
    {
        _connection = DatabaseSingleton.getInstance().getConnection();
        _badRequest = false;
        initialize(parameters);
    }

    // TODO: Implement
    private void initialize(String input)
    {

    }

    public Result checkOut()
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

    public Result checkIn()
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

    public Result browse()
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

    public Result addInventory()
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

    public Result joinWaitList()
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

    public Result getLateBookList()
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
}
