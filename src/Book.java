import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by Umair on 4/15/2015.
 */
public class Book
{
    private Connection _connection;

    public Book(Connection connection)
    {
        _connection = connection;
    }

    /**
     * Add one or more authors for the given isbn.
     * Should only be called inside of addBook.
     */
    private void addAuthors(String isbn, String author)
    {
        try
        {
            String[] authors = author.split(",");

            String query = "insert into author(isbn,author) values(?,?)";
            PreparedStatement statement = _connection.prepareStatement(query);

            for (String a : authors)
            {
                statement.setString(1, isbn);
                statement.setString(2, a.trim());
                statement.executeUpdate();
            }

            statement.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Returns the author(s) for the given book.
     */
    private String getAuthors(String isbn)
    {
        try
        {
            String authors = "";
            String query = "select * from author where isbn=?";
            PreparedStatement statement = _connection.prepareStatement(query);
            statement.setString(1, isbn);
            ResultSet results = statement.executeQuery();

            if(!results.isBeforeFirst())
                return authors;

            while(results.next())
                authors += results.getString("author") + ", ";

            statement.close();
            return authors;
        }
        catch (Exception e)
        {
            return "";
        }
    }

    /**
     * Returns where each copy of the book is located
     */
    private String getTrackedCopies(String isbn)
    {
        try
        {
            String copies = "\nBook No.\tLocation\n";
            String query = "select book_no, (case when availability=-1 then 'Lost' " +
                            "when availability=0 then 'Checked Out' \n" +
                            "else location end) as location from inventory where isbn=?";
            PreparedStatement statement = _connection.prepareStatement(query);
            statement.setString(1, isbn);
            ResultSet results = statement.executeQuery();

            if(!results.isBeforeFirst())
                return copies+= Constant.NO_MATCH + "\n";

            while(results.next())
                copies += results.getString("book_no") + "\t\t\t" +
                        results.getString("location") + "\n";

            statement.close();
            return copies;
        }
        catch (Exception e)
        {
            return "";
        }
    }

    /**
     * Returns usernames and dates for all users that checked out this book
     */
    private String getBookCheckOuts(String isbn)
    {
        try
        {
            String checkouts = "\nCheck Out History\nUser\t\tCheck Out\tReturn\n";

            String query = "select username,date_checkedout,date_returned from checkout where isbn=?";
            PreparedStatement statement = _connection.prepareStatement(query);
            statement.setString(1, isbn);
            ResultSet results = statement.executeQuery();

            if(!results.isBeforeFirst())
                return checkouts += Constant.NO_MATCH + "\n";

            while(results.next())
                checkouts += results.getString("username") + "\t\t" +
                        results.getString("date_checkedout") + "\t\t" +
                        results.getString("date_returned") + "\n";

            statement.close();
            return checkouts;
        }
        catch (Exception e)
        {
            return "";
        }
    }

    /**
     * Returns all reviews for the book with given isbn as well as the average review score.
     */
    private String getBookReviews(String isbn)
    {
        try
        {
            String reviews = "\nBook Reviews and Ratings\nUser\t\tScore\tReview\n";

            String query = "select username,score,rev_text from reviews where isbn=?";

            PreparedStatement statement = _connection.prepareStatement(query);
            statement.setString(1, isbn);
            ResultSet results = statement.executeQuery();

            if(!results.isBeforeFirst())
                return reviews += Constant.NO_MATCH + "\n";

            while(results.next())
                reviews += results.getString("username") + "\t\t" + results.getString("score") +
                        "\t\t" + results.getString("rev_text") + "\n";

            statement.close();

            String averageScore = "select AVG(score) as average from reviews where isbn=?";
            statement = _connection.prepareStatement(averageScore);
            statement.setString(1, isbn);
            ResultSet averageResult = statement.executeQuery();
            averageResult.next();

            reviews += "Average Review Score: " + averageResult.getDouble("average");
            averageResult.close();

            return reviews;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Constant.FAIL;
        }
    }

    /**
     * Adds book with the given parameters. Returns whether book was added or not.
     * Makes separate call to addAuthors to add one or more authors for the book.
     */
    public Result addBook(String isbn, String title, String author, String subject,
                          String publisher, String publishYear, String format, String summary)
    {
        try
        {
            String query = "insert into `cs5530db26`.`book` (`b_isbn`," +
                    "`title`,`subject`,`publisher`,`pub_year`,`format`,`summary`)" +
                    " values(?,?,?,?,?,?,?)";

            PreparedStatement insert = _connection.prepareStatement(query);

            insert.setString(1, isbn);
            insert.setString(2, title);
            insert.setString(3, subject);
            insert.setString(4, publisher);
            insert.setString(5, publishYear);
            insert.setString(6, format);
            insert.setString(7, summary);

            insert.executeUpdate();
            insert.close();

            addAuthors(isbn, author);
            return new Result(Constant.SUCCESS);
        }
        catch (Exception e)
        {
            return new Result(e.getMessage());
        }
    }

    /**
     * Adds a review score for the book by the user with the optional review test as well.
     */
    public Result addReview(String isbn, String username, String score, String review)
    {
        try
        {
            String query = "insert into reviews(isbn,username,user_id,score,rev_text) " +
                    "values(?,?,(select user_id from user where username= ?),?,?)";

            PreparedStatement insert = _connection.prepareStatement(query);
            insert.setString(1, isbn);
            insert.setString(2, username);
            insert.setString(3, username);
            insert.setInt(4, Integer.parseInt(score));
            insert.setString(5, review);

            insert.executeUpdate();

            insert.close();

            return new Result(Constant.SUCCESS);
        }
        catch (Exception e)
        {
            return new Result(Constant.FAIL + ": "+ e.getMessage());
        }
    }

    /**
     * Used for when user doesn't provide review text.
     */
    public Result addReview(String isbn, String username, String score)
    {
        return addReview(isbn,username,score," ");
    }

    /**
     * Returns a comprehensive record about the book including general book data, check out history,
     * users who have checked it out, and reviews for the book.
     * @param isbn
     * @return
     */
    public Result bookRecord(String isbn)
    {
        try
        {
            String query = "select * from book where b_isbn=?";
            PreparedStatement statement = _connection.prepareStatement(query);
            statement.setString(1, isbn);
            ResultSet results = statement.executeQuery();

            if(!results.isBeforeFirst())
                return new Result(Constant.BAD_REQUEST + ": No book with that ISBN");
            results.next();

            String authors = getAuthors(isbn);

            String bookInfo = "Book Information\n" +
                    "ISBN: " + results.getString("b_isbn") + "\n" +
                    "Title: " + results.getString("title") + "\n" +
                    "Author(s): " + authors + "\n" + "Subject: " +
                    results.getString("subject") + "\n" + "Publisher: " +
                    results.getString("publisher") + "\n" + "Year Published: " +
                    results.getString("pub_year") + "\n" + "Format: " +
                    results.getString("format") + "\n" + "Summary: " +
                    results.getString("summary") + "\n";

            bookInfo += getTrackedCopies(isbn);
            bookInfo += getBookCheckOuts(isbn);
            bookInfo += getBookReviews(isbn);

            return new Result(bookInfo);
        }
        catch (Exception e)
        {
            return new Result(Constant.FAIL + ": "+ e.getMessage());
        }
    }

    public Result bookCheckOutHistory()
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

    public Result bookReviews()
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

    /**
     * Returns the top count number of books that have been checked out.
     */
    public Result getMostCheckedOut(int count)
    {
        try
        {
            String query = "SELECT isbn, book.title, COUNT(*) as count " +
                    "FROM checkout INNER JOIN book " +
                    "ON isbn=b_isbn group by isbn " +
                    "order by count desc limit ?";

            PreparedStatement statement = _connection.prepareStatement(query);
            statement.setInt(1, count);
            ResultSet result = statement.executeQuery();

            String rows = "";
            while(result.next())
                rows += result.getString("count") + "\t\t" +
                        result.getString("isbn") + "\t\t" +
                        result.getString("title") + "\n";

            result.close();
            return new Result(rows);
        }
        catch (Exception e)
        {
            return new Result(e.getMessage());
        }
    }
}
