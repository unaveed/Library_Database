import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import java.sql.*;
import java.util.Calendar;

public class LibraryManager implements LibraryInterface {

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
        System.out.println ("Welcome to the CS5530 LibraryInterface\n" +
                            "Type in a command and press enter. " +
                            "To see a list of commands and parameters, type help");
    }

    private Date getCurrentDate()
    {
        java.util.Date date = new java.util.Date();
        return new java.sql.Date(date.getTime());
    }

    private Date getCheckoutDate()
    {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 30);

        return new java.sql.Date(cal.getTimeInMillis());
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

    private int addRequest(String isbn, String username, Date date)
    {
        try
        {
            String query = "insert into requests(isbn,username,user_id,date_requested) " +
                           "values(?,?,(select user_id from user where username= ?),?)";
            PreparedStatement insert = connection.prepareStatement(query);

            insert.setString(1, isbn);
            insert.setString(2, username);
            insert.setString(3, username);
            insert.setDate(4, date);

            int result = insert.executeUpdate();
            insert.close();

            return result;
        }
        catch(MySQLIntegrityConstraintViolationException ex)
        {
            return -1;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return -2;
        }

    }

    private int updateInventory(String isbn, int bookNumber, int status)
    {
        try
        {
            String query = "update inventory set availability=? where isbn=? and book_no=?";
            PreparedStatement update = connection.prepareStatement(query);

            update.setInt(1, status);
            update.setString(2, isbn);
            update.setInt(3, bookNumber);

            int result = update.executeUpdate();
            update.close();

            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -2;
        }
    }

    /**
     * Returns the book number if there is an available copy for the given isbn.
     * If there is no match, returns -3
     */
    private int getAvailableBook(String isbn)
    {
        try
        {
            String queryForAvailability = "SELECT book_no FROM inventory where isbn=? and availability=1";
            PreparedStatement statement = connection.prepareStatement(queryForAvailability);

            statement.setString(1, isbn);
            ResultSet availability = statement.executeQuery();

            if(!availability.isBeforeFirst())
                return -3;

            availability.next();
            int bookNumber = availability.getInt("book_no");
            availability.close();

            return bookNumber;
        }
        catch (Exception e)
        {
            return -2;
        }
    }

    @Override
    public int addBook(String isbn, String title, String author,
                        String subject, String publisher,
                        String publishYear, String format, String summary)
    {
        try
        {
            String query = "insert into `cs5530db26`.`book` (`b_isbn`," +
                    "`title`,`author`,`subject`,`publisher`,`pub_year`,`format`,`summary`)" +
                    " values(?,?,?,?,?,?,?,?)";

            PreparedStatement insert = connection.prepareStatement(query);

            insert.setString(1, isbn);
            insert.setString(2, title);
            insert.setString(3, author);
            insert.setString(4, subject);
            insert.setString(5, publisher);
            insert.setString(6, publishYear);
            insert.setString(7, format);
            insert.setString(8, summary);

            int result = insert.executeUpdate();
            insert.close();

            return result;
        }
        catch(MySQLIntegrityConstraintViolationException ex)
        {
            // Return -1 to indicate a book with this isbn already exists
            return -1;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -2;
        }
    }

    /**
     * Adds book to inventory provided a book with that ISBN exists in the book table.
     * Adds a book number for each individual copy, increments from the current highest
     * copy number or 1 if no copies exist.
     * @return n > 0 for successful queries, n < 0 for errors.
     */
    @Override
    public int addInventory(String isbn)
    {
        try
        {
            String nextBookSequence = "select book_no from inventory where isbn=? order by book_no desc limit 1;";
            PreparedStatement selectNextBookNumber = connection.prepareStatement(nextBookSequence);
            selectNextBookNumber.setString(1, isbn);

            ResultSet selectResult = selectNextBookNumber.executeQuery();
            int book_no;

            // Sets the book number for this copy
            if(!selectResult.isBeforeFirst())
                book_no = 0;
            else
                book_no = selectResult.getInt("book_no");
            selectResult.close();

            String query = "insert into inventory(isbn,book_no,availability) values " +
                           "((select b_isbn from book where b_isbn = ?),?,?);";

            PreparedStatement insert = connection.prepareStatement(query);

            insert.setString(1, isbn);
            insert.setInt(2, ++book_no);
            insert.setInt(3, 1);

            int result = insert.executeUpdate();
            insert.close();

            return result;
        }
        catch (MySQLIntegrityConstraintViolationException ex)
        {
            return -1;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -2;
        }
    }

    /**
     * Adds a new user to the database with the provided parameters.
     * Ensures only unique usernames are added.
     * @return n > 0 for successful query, n = -1 for already existing username, -2 for other errors.
     */
    @Override
    public int addUser(String username, String address, String name, String phone, String email)
    {
        try
        {
            String query = "insert into `cs5530db26`.`user` (`username`," +
                    "`address`,`full_name`,`phone_no`,`email`)" +
                    " values(?,?,?,?,?)";

            PreparedStatement insert = connection.prepareStatement(query);

            insert.setString(1, username);
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

    @Override
    public int checkout(String isbn, String username)
    {
        try
        {
            Date date = getCurrentDate();
            int bookNumber = getAvailableBook(isbn);

            if(bookNumber == -3)
                return addRequest(isbn,username,date);

            // Make sure user doesn't already have the book
            String queryUserHasBook = "select * from checkout where isbn=? and username=? and date_returned=?";
            PreparedStatement statement = connection.prepareStatement(queryUserHasBook);

            statement.setString(1, isbn);
            statement.setString(2,username);
            statement.setString(3, "");
            ResultSet userHasBookResult = statement.executeQuery();

            if(userHasBookResult.isBeforeFirst())
                return -1;

            String query = "insert into checkout(isbn,book_no,username,user_id,date_checkedout,due_date,date_returned) " +
                           "values(?,?,?,(select user_id from user where username= ?),?,?)";
            statement = connection.prepareStatement(query);

            statement.setString(1, isbn);
            statement.setInt(2, bookNumber);
            statement.setString(3, username);
            statement.setString(4, username);
            statement.setDate(5, date);
            statement.setDate(6, getCheckoutDate());

            int result = statement.executeUpdate();
            statement.close();

            updateInventory(isbn,bookNumber,0);

            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -2;
        }
    }

    @Override
    public int checkin(String isbn, String username, int status)
    {
        try
        {
            return -1;
        }
        catch(Exception e)
        {
            return -2;
        }
    }

    @Override
    public int addReview(String isbn, String username, String userID, String score, String reviewText)
    {
        try
        {
            String query = "insert into reviews(isbn,username,user_id,score,rev_text) " +
                    "values((select b_isbn from book where b_isbn= ?)," +
                    "(select username from user where username= ?)," +
                    "(select user_id from user where user_id= ?),?,?)";

            PreparedStatement insert = connection.prepareStatement(query);
            insert.setString(1, isbn);
            insert.setString(2, username);
            insert.setString(3, userID);
            insert.setString(4, score);
            insert.setString(5, reviewText);

            int result = insert.executeUpdate();
            insert.close();

            return result;
        }
        catch(MySQLIntegrityConstraintViolationException ex)
        {
            System.out.println(ex.getMessage());
            return -1;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -2;
        }
    }

    @Override
    public int userRecord(String username, String request)
    {
        String query;
        PreparedStatement statement;

        if(request.equalsIgnoreCase("personal data"))
        {
            try
            {
                query = "select * from user where username=?";
                statement = connection.prepareStatement(query);

                statement.setString(1, username);
                ResultSet result = statement.executeQuery();

                if(!result.isBeforeFirst())
                {
                    System.out.println("No user exists with that username.");
                }
                else
                {
                    result.next();
                    System.out.println("username: " + result.getString("username") + "\n" +
                            "name: " + result.getString("full_name") + "\n" +
                            "address: " + result.getString("address") + "\n" +
                            "phone: " + result.getString("phone_no") + "\n" +
                            "email: " + result.getString("email") + "\n");
                }

                statement.close();
                return 1;
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return -2;
            }

        }
        else if (request.equalsIgnoreCase("checkedout books"))
        {
            try
            {
                query = "SELECT  isbn, b.title, date_checkedout,date_returned " +
                        "FROM checkout c INNER JOIN book b " +
                        "ON c.isbn = b.b_isbn " +
                        "WHERE c.username=?";

                statement = connection.prepareStatement(query);
                statement.setString(1, username);

                ResultSet result = statement.executeQuery();

                System.out.println("ISBN\t\t\tTitle\t\t\t\t\t\t\t\t\t\t\t\t\tCheck Out Date\t\tDate Returned");
                while(result.next())
                    System.out.println(result.getString("isbn") + "\t" + result.getString("title") + "\t\t\t\t\t\t\t\t" +
                                       result.getString("date_checkedout") + "\t\t" + result.getString("date_returned"));

                statement.close();
                return 1;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return -2;
            }
        }
        else if (request.equalsIgnoreCase("lost books"))
        {

        }
        else if (request.equalsIgnoreCase("requested books"))
        {
            try
            {
                query = "SELECT isbn, book.title,date_requested FROM cs5530db26.requests " +
                        "inner join book " +
                        "on requests.isbn = book.b_isbn " +
                        "where username=? and date_fulfilled is NULL;";

                statement = connection.prepareStatement(query);
                statement.setString(1, username);
                ResultSet result = statement.executeQuery();

                System.out.println("ISBN\t\t\tTitle\t\t\t\t\t\t\t\t\t\t\t\t\tDate Requested");
                while(result.next())
                    System.out.println(result.getString("isbn") + "\t" + result.getString("title") +
                                        "\t\t\t\t\t\t\t\t" + result.getString("date_requested"));

                statement.close();
                return 1;
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return -2;
            }

        }
        else if (request.equalsIgnoreCase("reviewed books"))
        {
            try
            {
                query = "select isbn, book.title,score,rev_text from reviews " +
                        "inner join book " +
                        "on reviews.isbn=book.b_isbn " +
                        "where username=?";

                statement = connection.prepareStatement(query);
                statement.setString(1, username);
                ResultSet result = statement.executeQuery();

                System.out.println("ISBN\t\t\tTitle\t\t\t\t\t\t\t\t\t\t\t\t\tScore\tReview");
                while(result.next())
                    System.out.println(result.getString("isbn") + "\t" + result.getString("title") +
                            "\t\t\t\t\t\t\t" + result.getString("score") + "\t\t" + result.getString("rev_text"));

                result.close();
                return 1;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return -2;
            }
        }
        else
        {
            System.out.println("You entered an invalid command.");
        }
        return 0;
    }

    @Override
    public int userStatistics(int count, String request)
    {
        try
        {
            if(request.equalsIgnoreCase("most checkedout"))
            {

            }
            else if(request.equalsIgnoreCase("most reviews"))
            {

            }
            else if(request.equalsIgnoreCase("most lost"))
            {

            }
            else
            {

            }

            return -1;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -2;
        }
    }
}
