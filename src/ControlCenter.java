import java.util.Scanner;

public class ControlCenter {

    // Flags that control flow based on user input
    private boolean mRegister;
    private boolean mNewBook;
    private boolean mInventory;
    private boolean mCheckout;
    private boolean mCheckIn;
    private boolean mReview;
    private boolean mBookInfo;
    private boolean mBookStats;
    private boolean mUserStats;

    LibraryManager mLibraryManager;

    public ControlCenter()
    {
        mRegister = false;
        mNewBook = false;
        mInventory = false;
        mCheckout = false;
        mCheckIn = false;
        mReview = false;
        mBookInfo = false;
        mBookStats = false;
        mUserStats = false;

        mLibraryManager = new LibraryManager();
    }

    private void parseInput(Scanner scanner)
    {
        while(scanner.hasNext())
        {
            String line = scanner.nextLine();

            if(mRegister)
            {
                registerUser(line);
                continue;
            }
            else if (mNewBook)
            {
                addNewBook(line);
                continue;
            }
            else if (mInventory)
            {
                addBookInventory(line);
                continue;
            }
            else if(mCheckout)
            {
                checkBookOut(line);
                continue;
            }
            else if(mCheckIn)
            {
                checkBookIn(line);
                continue;
            }
            else if(mReview)
            {
                addReview(line);
                continue;
            }
            else if(mBookInfo)
            {
                getBookInformation(line);
                continue;
            }
            else if(mBookStats)
            {
                getBookStats(line);
                continue;
            }
            else if(mUserStats)
            {
                getUserStats(line);
                continue;
            }

            if(line.equalsIgnoreCase("register"))
                mRegister = true;
            else if(line.equalsIgnoreCase("add book"))
                mNewBook = true;
            else if(line.equalsIgnoreCase("inventory"))
                mInventory = true;
            else if(line.equalsIgnoreCase("checkout"))
                mCheckout = true;
            else if(line.equalsIgnoreCase("checkin"))
                mCheckIn = true;
            else if(line.equalsIgnoreCase("review"))
                mReview = true;
            else if(line.equalsIgnoreCase("book info"))
                mBookInfo = true;
            else if(line.equalsIgnoreCase("book stats"))
                mBookStats = true;
            else if(line.equalsIgnoreCase("user stats"))
                mUserStats = true;
            else if(line.equalsIgnoreCase("help"))
                System.out.println(displayHelp());
            else if(line.equalsIgnoreCase("exit"))
                break ;
            else
                System.out.println("you said >> " + line);
        }

        scanner.close();
        mLibraryManager.close();
    }

    /**
     * TODO: Implement
     */
    private void addBookInventory(String line)
    {

    }

    /**
     * TODO: Implement
     */
    private void getUserStats(String line)
    {
    }

    /**
     * TODO: Implement
     */
    private void getBookStats(String line)
    {
    }

    /**
     * TODO: Implement
     */
    private void getBookInformation(String line)
    {
    }

    /**
     * Adds a review from a user for a book, prevents a user from adding multiple reviews for the same book.
     */
    private void addReview(String line)
    {
        String[] params = line.split(",");

        if(params.length != 5)
        {

        }
        else
        {
            String isbn = params[0];
            String username = params[1];
            String userID = params[2];
            String score = params[3];
            String reviewText = params[4];

            int result = mLibraryManager.addReview(isbn, username, userID, score, reviewText);
            String duplicate = "You have already left a review for this book.";
            printResult(result, duplicate);
            mReview = false;
        }
    }

    /**
     * Checks out book for user, if it is not available, adds user to wish list.
     */
    private void checkBookOut(String line)
    {
        String[] params = line.split(",");

        if(params.length != 2)
        {
            System.out.println("Cannot complete query, you are missing one or more parameters.");
        }
        else
        {
            String isbn = params[0];
            String username = params[1];

            int result = mLibraryManager.checkout(isbn, username);

            String duplicate = "You are either on the wait list for this book or already have it checked out";
            printResult(result, duplicate);
            mCheckout = false;
        }
    }

    private void checkBookIn(String line)
    {

    }

    /**
     * Checks to make sure user provided correct parameters for adding a book.
     * Attempts to add the book to the database, displays errors should they occur.
     */
    private void addNewBook(String line)
    {
        String[] params = line.split(",");

        if(params.length != 8)
        {
            System.out.println("Cannot complete query, you are missing one or more parameters.");
        }
        else
        {
            String isbn = params[0];
            String title = params[1];
            String author = params[2];
            String subject = params[3];
            String publisher = params[4];
            String pubYear = params[5];
            String format = params[6];
            String summary = params[7];

            int result = mLibraryManager.addBook(isbn, title, author,
                    subject, publisher, pubYear, format, summary);

            String duplicate = "A book with that isbn already exists.";
            printResult(result, duplicate);
            mNewBook = false;
        }
    }

    /**
     * TODO: Implement
     */
    private void registerUser(String line)
    {
        String[] params = line.split(",");

        if(params.length != 5)
        {
            System.out.println("Cannot complete query, you are missing one or more parameters.");
        }
        else
        {
            String username = params[0];
            String address = params[1];
            String name = params[2];
            String phone = params[3];
            String email = params[4];

            int result = mLibraryManager.addUser(username, address, name, phone, email);

            String duplicate = "That username already exists, please try another.";
            printResult(result, duplicate);
            mRegister = false;
        }
    }

    /**
     * TODO: Implement, print a message of success or failure
     */
    private void printResult(int result, String duplicate)
    {
        if(result > 0)
            System.out.println("Query successful!");
        else if (result == -1)
            System.out.println(duplicate);
        else
        {
            // some other error
        }
    }

    private static String displayHelp()
    {
        String helpOptions = "command\t\t\tParameters, (r) indicates required field, " +
                "blank space between comma if optional field not needed.\n" +
        "register\t\tusername(r),address(r),full name(r),phone number,e-mail address\n" +
        "checkout\t\tisbn(r),username(r)" +
        "checkin\t\tisbn(r),username(r)" +
        "exit\t\t\tLog out of the system";

        return helpOptions;
    }

    public static void main(String[] args)
    {
        ControlCenter controlCenter = new ControlCenter();
        Scanner scanner = new Scanner(System.in);
        controlCenter.parseInput(scanner);
    }
}
