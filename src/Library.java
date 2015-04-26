import java.sql.*;
import java.util.Calendar;

/**
 * Created by Umair on 4/15/2015.
 */
public class Library
{
    private Connection _connection;

    public enum Availability {
        LOST, AVAILABLE, UNAVAILABLE;

        public int getAvailability()
        {
            switch(this)
            {
                case AVAILABLE:
                    return 1;
                case UNAVAILABLE:
                    return 0;
                case LOST:
                    return -1;
                default:
                    return 0;
            }
        }
    }

    public enum Sort
    {
        YEAR_PUBLISHED, AVG_RATING, POPULARITY;

        public int getSort()
        {
            switch(this)
            {
                case YEAR_PUBLISHED:
                    return 0;
                case AVG_RATING:
                    return 1;
                case POPULARITY:
                    return 2;
                default:
                    return 0;
            }
        }
    }

    public enum Order
    {
        ASC, DESC;

        public int getOrder()
        {
            switch(this)
            {
                case ASC:
                    return 0;
                case DESC:
                    return 1;
                default:
                    return 1;
            }
        }
    }

    public enum Status
    {
        RETURN(1), LOST(0);

        private final int num;

        Status(int num)
        {
            this.num = num;
        }

        public int getStatus()
        {
            return num;
        }
    }

    public enum Fulfillment {
        YES(1), NO(0);

        private final int data;

        Fulfillment(int num)
        {
            data = num;
        }

        public int getValue()
        {
            return data;
        }
    }

    public Library(Connection connection)
    {
        _connection = connection;
    }

    /**
     * Returns today's date in SQL date object
     */
    private Date getCurrentDate()
    {
        java.util.Date date = new java.util.Date();
        return new java.sql.Date(date.getTime());
    }

    /**
     * Returns the date 30 days from today
     */
    private Date getCheckoutDate()
    {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 30);

        return new java.sql.Date(cal.getTimeInMillis());
    }

    /**
     * Return the current time stamp
     */
    private Timestamp getDateTime()
    {
        java.util.Date date = new java.util.Date();
        return new Timestamp(date.getTime());
    }

    /**
     * Returns the book number if there is an available copy for the given isbn.
     * If book is unavilable returns 0.
     * If book has a wait list in which the user hasn't been on for the longest time, returns -1
     */
    private int getAvailableBook(String isbn, String username)
    {
        try
        {
            String query = "select book_no from inventory where isbn=? and availability=1";
            PreparedStatement statement = _connection.prepareStatement(query);
            statement.setString(1, isbn);
            ResultSet availableBooks = statement.executeQuery();

            if(!availableBooks.isBeforeFirst())
                return Availability.UNAVAILABLE.getAvailability();

            availableBooks.next();
            int bookNumber = availableBooks.getInt("book_no");
            statement.close();

            ResultSet longestWaiting = longestOnWaitList(isbn);

            // Exception was caught, can't process
            if(longestWaiting == null)
                return Availability.UNAVAILABLE.getAvailability();

            // No results, meaning there is not waitlist
            if(!longestWaiting.isBeforeFirst())
                return bookNumber;

            longestWaiting.next();
            String longestWaitingUser = longestWaiting.getString("username");

            // Username was waiting the longest
            if(longestWaitingUser.equals(username))
            {
                updateWaitList(isbn, username);
                return bookNumber;
            }

            return -1;
        }
        catch (Exception e)
        {
            return Availability.UNAVAILABLE.getAvailability();
        }
    }

    /**
     * Finds the username of the user who has been on the waitlist longest for given book
     * This should only be called form within getAvailableBooks.
     */
    private ResultSet longestOnWaitList(String isbn)
    {
        try
        {
            String query = "select * from waitlist where isbn=? and " +
                           "fulfilled=0 order by date_requested asc limit 1";
            PreparedStatement statement = _connection.prepareStatement(query);
            statement.setString(1, isbn);

            return statement.executeQuery();
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Return book number > 0 of the book with isbn checked out by username, 0 for error.
     */
    private int getBookNumber(String isbn, String username)
    {
        try
        {
            String queryBookNumber = "select book_no from checkout " +
                    "where username=? and isbn=? and date_returned is null";

            PreparedStatement statement = _connection.prepareStatement(queryBookNumber);
            statement.setString(1, username);
            statement.setString(2, isbn);
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.isBeforeFirst())
                return 0;

            resultSet.next();
            int bookNumber = resultSet.getInt("book_no");
            statement.close();

            return bookNumber;
        }
        catch(Exception e)
        {
            System.out.println("getBookNumber" + e.getMessage());
            return 0;
        }
    }

    /**
     * Updates the library inventory with the status of the book that was checked in or marked as lost.
     */
    private void updateInventory(String isbn, String username, Status status)
    {
        try
        {
            int bookNumber = getBookNumber(isbn, username);

            if(bookNumber > 0)
            {
                String query = "update inventory set availability=? where isbn=? and book_no=?";
                PreparedStatement update = _connection.prepareStatement(query);
                update.setInt(1, status.getStatus());
                update.setString(2, isbn);
                update.setInt(3, bookNumber);

                update.executeUpdate();
                update.close();
            }

        }
        catch (Exception e)
        {
            System.out.println("updateInventory: " + e.getMessage());
        }
    }

    /**
     * Called when user checks out a book they were on the wait list for. Updates their status to fulfilled.
     */
    private boolean updateWaitList(String isbn, String username)
    {
        try
        {
            String query = "update waitlist set fulfilled=1 where isbn=? and username=? and fulfilled=0";
            PreparedStatement statement = _connection.prepareStatement(query);
            statement.setString(1, isbn);
            statement.setString(2, username);

            statement.executeUpdate();
            statement.close();

            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    /**
     * Checks out book with given isbn for user if it is available.
     */
    public Result checkOut(String isbn, String username)
    {
        try
        {
            int bookNumber = getAvailableBook(isbn, username);

            // Indicates there is a book available for checkout
            if(bookNumber > 0)
            {
                String query = "insert into checkout(isbn,book_no,username,user_id," +
                               "date_checkedout,due_date) values(?,?,?," +
                               "(select user_id from user where username= ?),?,?)";
                PreparedStatement statement = _connection.prepareStatement(query);

                statement.setString(1, isbn);
                statement.setInt(2, bookNumber);
                statement.setString(3, username);
                statement.setString(4, username);
                statement.setDate(5, getCurrentDate());
                statement.setDate(6, getCheckoutDate());

                int queryResult = statement.executeUpdate();
                return new Result(queryResult);
            }
            else if (bookNumber == -1)
                return new Result("Cannot check out, wait list exists for book.");
            else
                return new Result("Book unavailable for check out.");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new Result(e.getMessage());
        }
    }

    /**
     * Check in the book with isbn that was checkout by username. Status can be RETURN or LOST.
     * Upon successful query, displays a list of user (if any) that are on the wait list for this isbn.
     */
    public Result checkIn(String isbn, String username, Status status)
    {
        try
        {
            updateInventory(isbn, username, status);

            String query = "update checkout set date_returned=? " +
                    "where username=? and isbn=? and date_returned is null";

            PreparedStatement statement = _connection.prepareStatement(query);
            statement.setDate(1, getCurrentDate());
            statement.setString(2, username);
            statement.setString(3, isbn);
            statement.executeUpdate();
            statement.close();

            String waitlist = "select username from waitlist where isbn=? and fulfilled=0";
            statement = _connection.prepareStatement(waitlist);
            statement.setString(1, isbn);
            ResultSet waitlistResult = statement.executeQuery();

            String result = "Users on waitlist for this book\n";
            while(waitlistResult.next())
                result += waitlistResult.getString("username") + "\n";

            statement.close();
            return new Result(result);
        }
        catch(Exception e)
        {
            return new Result(e.getMessage());
        }
    }

    public Result browse()
    {
        return null;
    }

    /**
     * Add 'copies' number of books for the given isbn.
     */
    public Result addInventory(String isbn, int copies)
    {
        try
        {
            String query = "insert into inventory(isbn,availability) values(?,?)";
            PreparedStatement statement = _connection.prepareStatement(query);
            statement.setString(1, isbn);
            statement.setInt(2, Availability.AVAILABLE.getAvailability());

            for(int i = 0; i < copies; i++)
                statement.executeUpdate();
            statement.close();

            return new Result(Constant.SUCCESS);
        }
        catch (Exception e)
        {
            return new Result(e.getMessage());
        }
    }

    /**
     * Add 'copies' number of books for the given isbn at the given location
     */
    public Result addInventory(String isbn, String location, int copies)
    {
        try
        {
            String query = "insert into inventory(isbn,availability,location) values(?,?,?)";
            PreparedStatement statement = _connection.prepareStatement(query);
            statement.setString(1, isbn);
            statement.setInt(2, Availability.AVAILABLE.getAvailability());
            statement.setString(3, location);

            for(int i = 0; i < copies; i++)
                statement.executeUpdate();
            statement.close();

            return new Result(Constant.SUCCESS);
        }
        catch (Exception e)
        {
            return new Result(e.getMessage());
        }
    }

    /**
     * Add user to the wait list for the book with given isbn.
     */
    public Result joinWaitList(String isbn, String username)
    {
        try
        {
            String query = "insert into waitlist(isbn,username,user_id,date_requested,fulfilled) " +
                           "values(?,?,(select user_id from user where username= ?),?,?)";
            PreparedStatement statement = _connection.prepareStatement(query);
            statement.setString(1, isbn);
            statement.setString(2, username);
            statement.setString(3, username);
            statement.setObject(4, getDateTime());
            statement.setInt(5, Fulfillment.NO.getValue());

            statement.executeUpdate();
            statement.close();
            return new Result(Constant.SUCCESS);
        }
        catch (Exception e)
        {
            return new Result(e.getMessage());
        }
    }

    /**
     * Based on the provided date, returns the books checked out that are late.
     */
    public Result getLateBookList(Date date)
    {
        try
        {
            String query = "SELECT b.title, c.due_date, u.full_name, u.phone_no, u.email FROM checkout c " +
                    "join book b ON c.isbn = b.b_isbn " +
                    "join user u ON c.username = u.username " +
                    "where c.due_date <= ? AND date_returned IS NULL;";

            PreparedStatement statement = _connection.prepareStatement(query);
            statement.setDate(1, date);
            ResultSet result = statement.executeQuery();

            String lateBooks = "";
            while(result.next())
            {
                lateBooks += result.getString("title") + "\t\t" + result.getString("due_date") +
                        "\t\t" + result.getString("full_name") + "\t\t" + result.getString("phone_no") +
                        "\t\t" + result.getString("email") + "\n";
            }

            result.close();
            return new Result(lateBooks);
        }
        catch (Exception e)
        {
            return new Result(e.getMessage());
        }
    }
}
