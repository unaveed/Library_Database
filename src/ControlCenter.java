import java.util.Scanner;

public class ControlCenter {

    // Flags that control flow based on user input
    private boolean mRegister;
    private boolean mNewBook;
    private boolean mCheckout;
    private boolean mReview;
    private boolean mBookInfo;
    private boolean mBookStats;
    private boolean mUserStats;

    LibraryManager mLibraryManager;

    public ControlCenter()
    {
        mRegister = false;
        mNewBook = false;
        mCheckout = false;
        mReview = false;
        mBookInfo = false;
        mBookStats = false;
        mUserStats = false;

        mLibraryManager = new LibraryManager();
    }

    private void parseInput(Scanner inputReader)
    {
        while(inputReader.hasNext())
        {
            String line = inputReader.nextLine();

            if(mRegister)
            {
                register(line);
                continue;
            }
            else if (mNewBook)
            {
                addNewBook(line);
                continue;
            }
            else if(mCheckout)
            {
                checkout(line);
                continue;
            }

            if(line.equalsIgnoreCase("mRegister"))
                mRegister = true;
            else if(line.equalsIgnoreCase("mCheckout"))
                mCheckout = true;
            else if(line.equalsIgnoreCase("help"))
                System.out.println(displayHelp());
            else if(line.equalsIgnoreCase("exit"))
                break ;
            else
                System.out.println("you said >> " + line);
        }

        inputReader.close();
        mLibraryManager.close();
    }

    /**
     * TODO: Implement
     * Checks out book for user, if it is not available, add user to wish list
     */
    private void checkout(String line)
    {
        int res = mLibraryManager.requestBook(line.split(","));
        mCheckout = false;
    }

    /**
     * TODO: Implement
     */
    private void addNewBook(String line)
    {
        mNewBook = false;
    }

    /**
     * TODO: Implement
     */
    private void register(String line)
    {
        int res = mLibraryManager.registerUser(line.split(","));
        if(res == -1)
            System.out.println("That username is already taken, please try a different one.");

        mRegister = false;
    }

    private static String displayHelp()
    {
        String helpOptions = "command\t\t\tParameters, (r) indicates required field, " +
                "blank space between comma if optional field not needed.\n" +
        "mRegister\t\tenter the username(r),address(r),full name(r),phone number,e-mail address\n" +
        "mCheckout\t\tenter username and either isbn:isbn or two of author,title,publisher" +
        "exit\t\t\tLog out of the system";

        return helpOptions;
    }

    public static void main(String[] args)
    {
        ControlCenter controlCenter = new ControlCenter();
        Scanner inputReader = new Scanner(System.in);

        controlCenter.parseInput(inputReader);
    }
}
