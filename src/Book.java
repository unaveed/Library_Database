import java.sql.Connection;

/**
 * Created by Umair on 4/15/2015.
 */
public class Book
{
    private Connection _connection;
    private boolean _badRequest;
    private String _username;
    private String _isbn;
    private String _title;
    private String _author;
    private String _publisher;
    private String _publishYear;
    private String _subject;
    private String _genre;
    private String _format;
    private String _summary;

    public Book(String parameters)
    {
        _connection = DatabaseSingleton.getInstance().getConnection();
    }

    public Result addBook()
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

    public Result addReview()
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

    public Result bookRecord()
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

    public Result bookData()
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

    public Result bookCopies()
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

    public Result bookCheckOutHistory()
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

    public Result bookReviews()
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

    public Result getBookStatistics()
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
