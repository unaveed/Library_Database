import java.util.Scanner;

public class ControlCenter {

    // Flags that control flow based on user input
    private boolean _register;
    private boolean _newBook;
    private boolean _inventory;
    private boolean _checkout;
    private boolean _checkIn;
    private boolean _waitList;
    private boolean _review;
    private boolean _bookInfo;
    private boolean _userRecord;
    private boolean _bookRecord;
    private boolean _bookStats;
    private boolean _userStats;
    private boolean _lateBookList;
    private boolean _browse;
    private boolean _browseAuthor;
    private boolean _browseSubject;
    private boolean _browseTitle;
    private boolean _browsePublisher;
    private boolean _browsePubAuthor;
    private boolean _browseTitleAuthor;
    private boolean _browsePubTitle;

    private String _delimiter;

    LibraryManager _libraryManager;

    public ControlCenter()
        {
            _register = false;
            _newBook = false;
            _inventory = false;
            _checkout = false;
            _checkIn = false;
            _waitList = false;
            _review = false;
            _bookInfo = false;
            _userRecord = false;
            _bookRecord = false;
            _bookStats = false;
            _userStats = false;
            _lateBookList = false;
            _browse = false;
            _browseAuthor = false;
            _browseSubject = false;
            _browseTitle = false;
            _browsePublisher = false;
            _browsePubAuthor = false;
            _browseTitleAuthor = false;
            _browsePubTitle = false;

            _delimiter = Constant.DELIMITER;
            _libraryManager = new LibraryManager();
    }

    private void parseInput(Scanner scanner)
    {
        while(scanner.hasNext())
        {
            String input = scanner.nextLine();
            Library library;
            Book book;
            User user;

            if(_browse)
            {
                library = new Library(input);
                System.out.println(library.browse().toString());
                _browse = false;
                continue;
            }
            else if(_register)
            {
                user = new User(input);
                System.out.println(user.register().toString());
                _register = false;
                continue;
            }
            else if (_newBook)
            {
                book = new Book(input);
                System.out.println(book.addBook().toString());
                _newBook = false;
                continue;
            }
            else if (_inventory)
            {
                library = new Library(input);
                System.out.println(library.addInventory().toString());
                _inventory = false;
                continue;
            }
            else if(_checkout)
            {
                library = new Library(input);
                System.out.println(library.checkOut().toString());
                _checkout = false;
                continue;
            }
            else if(_checkIn)
            {
                library = new Library(input);
                System.out.println(library.checkIn().toString());
                _checkIn = false;
                continue;
            }
            else if(_waitList)
            {
                library = new Library(input);
                System.out.println(library.joinWaitList().toString());
                _waitList = false;
                continue;
            }
            else if(_lateBookList)
            {
                library = new Library(input);
                System.out.println(library.getLateBookList().toString());
                _lateBookList = false;
                continue;
            }
            else if(_review)
            {
                book = new Book(input);
                System.out.println(book.addReview().toString());
                _review = false;
                continue;
            }
            else if(_bookInfo)
            {
                getBookInformation(input);
                continue;
            }
            else if(_userRecord)
            {
                getUserRecord(input);
                continue;
            }
            else if(_bookRecord)
            {
                getBookRecord(input);
                continue;
            }
            else if(_bookStats)
            {
                getBookStats(input);
                continue;
            }
            else if(_userStats)
            {
                getUserStats(input);
                continue;
            }


            if(input.equalsIgnoreCase("browse"))
                _browse = true;
            else if(input.equalsIgnoreCase("register"))
                _register = true;
            else if(input.equalsIgnoreCase("add book"))
                _newBook = true;
            else if(input.equalsIgnoreCase("add inventory"))
                _inventory = true;
            else if(input.equalsIgnoreCase("checkout"))
                _checkout = true;
            else if(input.equalsIgnoreCase("checkin"))
                _checkIn = true;
            else if(input.equalsIgnoreCase("waitlist"))
                _waitList = true;
            else if(input.equalsIgnoreCase("late books"))
                _lateBookList = true;
            else if(input.equalsIgnoreCase("review"))
                _review = true;
            else if(input.equalsIgnoreCase("book info"))
                _bookInfo = true;
            else if(input.equalsIgnoreCase("book stats"))
                _bookStats = true;
            else if(input.equals("user record"))
                _userRecord = true;
            else if(input.equalsIgnoreCase("book record"))
                _bookRecord = true;
            else if(input.equalsIgnoreCase("user stats"))
                _userStats = true;
            else if(input.equalsIgnoreCase("help"))
                System.out.println(displayHelp());
            else if(input.equalsIgnoreCase("exit"))
                break ;
            else
                System.out.println("Invalid command.");
        }

        scanner.close();
        _libraryManager.close();
    }

    private void browseByPublisherAuthor(String line)
    {
        String[] params = line.split(_delimiter);

        if(params.length != 4)
        {
            System.out.println("Cannot complete query, you have an incorrect number of parameters.");
        }
        else
        {
            String publisher = params[0];
            String author = params[1];
            String request = params[2];
            String sort = params[3];
            publisher = '%' + publisher + '%';
            author = '%' + author + '%';

            int result = _libraryManager.browseByPublisherAndAuthor(publisher, author, request, sort);
            printResult(result, " ");
            _browsePubAuthor = false;
        }
    }

    private void browseByPublisherTitle(String line)
    {
        String[] params = line.split(_delimiter);

        if(params.length != 4)
        {
            System.out.println("Cannot complete query, you have an incorrect number of parameters.");
        }
        else
        {
            String publisher = params[0];
            String title = params[1];
            String request = params[2];
            String sort = params[3];
            publisher = '%' + publisher + '%';
            title = '%' + title + '%';

            int result = _libraryManager.browseByPublisherAndTitle(publisher, title, request, sort);
            printResult(result, " ");
            _browsePubTitle = false;
        }
    }

    private void browseByTitleAuthor(String line)
    {
        String[] params = line.split(_delimiter);

        if(params.length != 4)
        {
            System.out.println("Cannot complete query, you have an incorrect number of parameters.");
        }
        else
        {
            String title = params[0];
            String author = params[1];
            String request = params[2];
            String sort = params[3];
            title = '%' + title + '%';
            author = '%' + author + '%';

            int result = _libraryManager.browseByPublisherAndAuthor(title, author, request, sort);
            printResult(result, " ");
            _browseTitleAuthor = false;
        }
    }

    private void browseBySubject(String line)
    {
        String[] params = line.split(_delimiter);

        if(params.length != 3)
        {
            System.out.println("Cannot complete query, you have an incorrect number of parameters.");
        }
        else
        {
            String subject = params[0];
            String request = params[1];
            String sort = params[2];
            subject = '%' + subject + '%';
            int result = _libraryManager.browseBySubject(subject, request, sort);
            printResult(result, " ");
            _browseAuthor = false;
        }
    }

    private void browseByAuthor(String line)
    {
        String[] params = line.split(_delimiter);

        if(params.length != 3)
        {
            System.out.println("Cannot complete query, you have an incorrect number of parameters.");
        }
        else
        {
            String author = params[0];
            String request = params[1];
            String sort = params[2];
            author = '%' + author + '%';
            int result = _libraryManager.browseByAuthor(author, request, sort);
            printResult(result, " ");
            _browseAuthor = false;
        }
    }

    private void browseByTitle(String line)
    {
        String[] params = line.split(_delimiter);

        if(params.length != 3)
        {
            System.out.println("Cannot complete query, you have an incorrect number of parameters.");
        }
        else
        {
            String title = params[0];
            String request = params[1];
            String sort = params[2];
            title = '%' + title + '%';
            int result = _libraryManager.browseByTitle(title, request, sort);
            printResult(result, " ");
            _browseTitle = false;
        }
    }

    private void browseByPublisher(String line)
    {
        String[] params = line.split(_delimiter);

        if(params.length != 3)
        {
            System.out.println("Cannot complete query, you have an incorrect number of parameters.");
        }
        else
        {
            String publisher = params[0];
            String request = params[1];
            String sort = params[2];
            publisher = '%' + publisher + '%';
            int result = _libraryManager.browseByPublisher(publisher, request, sort);
            printResult(result, " ");
            _browsePublisher = false;
        }
    }

    private void setBrowseSubject(String line)
    {
        String[] params = line.split(_delimiter);

        if(params.length != 3)
        {
            System.out.println("Cannot complete query, you have an incorrect number of parameters.");
        }
        else
        {
            String subject = params[0];
            String request = params[1];
            String sort = params[2];
            subject = '%' + subject + '%';
            int result = _libraryManager.browseBySubject(subject, request, sort);
            printResult(result, " ");
            _browseSubject = false;
        }
    }

    /**
     * TODO: Implement
     */
    private void addBookInventory(String line)
    {
        if(line.equals("") || line == null)
        {
            System.out.println("You must enter a valid ISBN number.");
        }
        else
        {
            int result = _libraryManager.addInventory(line);

            String noBook = "There is no book with that ISBN, " +
                            "try a different ISBN or use addBook to add information about this book.";
            printResult(result, noBook);
            _inventory = false;
        }

    }

    /**
     * TODO: Implement
     */
    private void getUserStats(String line)
    {
        String[] params = line.split(_delimiter);

        if(params.length != 2)
        {
            System.out.println("Cannot complete query, you have an incorrect number of parameters.");
        }
        else
        {
            int count = (int) Double.parseDouble(params[0]);
            String request = params[1];
            int result = _libraryManager.userStatistics((count = count < 1 ? 10 : count), request);
            printResult(result, " ");
            _userStats = false;
        }
    }

    /**
     * TODO: Implement
     */
    private void getBookStats(String line)
    {
        String[] params = line.split(_delimiter);

        if(params.length != 2)
        {
            System.out.println("Cannot complete query, you have an incorrect number of parameters.");
        }
        else
        {
            int count = (int) Double.parseDouble(params[0]);
            String request = params[1];
            int result = _libraryManager.bookStatistics((count = count < 1 ? 10 : count), request);
            printResult(result, " ");
            _bookStats = false;
        }
    }

    /**
     * TODO: Implement
     */
    private void getBookInformation(String line)
    {
    }

    /**
     * TODO: Implement
     */
    private void lateBookList(String line)
    {
        java.sql.Date date = java.sql.Date.valueOf(line);
        int result = _libraryManager.lateBooks(date);
        printResult(result," ");
        _lateBookList = false;
    }

    /**
     * Adds a review from a user for a book, prevents a user from adding multiple reviews for the same book.
     */
    private void addReview(String line)
    {
        String[] params = line.split(_delimiter);

        if(params.length != 4)
        {
            System.out.println("Cannot complete query, you have an incorrect number of parameters.");
        }
        else
        {
            String isbn = params[0];
            String username = params[1];
            String score = params[2];
            String reviewText = params[3];

            double rating = Double.parseDouble(score);
            if(rating >= 1 || rating <= 10)
            {
                int result = _libraryManager.addReview(isbn, username, score, reviewText);
                String duplicate = "You have already left a review for this book.";
                printResult(result, duplicate);
                _review = false;
            }
            else
            {
                System.out.println("Please enter a valid number between 1 and 10 for the rating");
            }
        }
    }

    /**
     * Checks out book for user, if it is not available, adds user to wish list.
     */
    private void checkBookOut(String line)
    {
        String[] params = line.split(_delimiter);

        if(params.length != 2)
        {
            System.out.println("Cannot complete query, you are missing one or more parameters.");
        }
        else
        {
            String isbn = params[0];
            String username = params[1];

            int result = _libraryManager.checkout(isbn, username);

            String duplicate = "You are either already on the wait list for " +
                               "this book or currently have it checked out";
            String noCopies = "There are no available copies for that book. You have been added to the waitlist.";

            if(result == -3)
                printResult(-2, duplicate);
            else
                printResult(result, duplicate);

            _checkout = false;
        }
    }

    private void checkBookIn(String line)
    {
        String[] params = line.split(_delimiter);

        if(params.length != 3)
        {
            System.out.println("Cannot complete query, you are missing one or more parameters.");
        }
        else
        {
            String isbn = params[0];
            String username = params[1];
            int status = Integer.parseInt(params[2]);

            int result = _libraryManager.checkin(isbn, username, status);
            printResult(result, "");
            _checkIn = false;
        }
    }

    /**
     * Checks to make sure user provided correct parameters for adding a book.
     * Attempts to add the book to the database, displays errors should they occur.
     */
    private void addNewBook(String line)
    {
        String[] params = line.split(_delimiter);

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

            int result = _libraryManager.addBook(isbn, title, author,
                    subject, publisher, pubYear, format, summary);

            String duplicate = "A book with that isbn already exists.";
            printResult(result, duplicate);
            _newBook = false;
        }
    }

    private void getUserRecord(String line)
    {
        String[] params = line.split(_delimiter);

        if(params.length < 2)
        {
            System.out.println("You have not provided the correct parameters, please try again.");
        }
        else
        {
            String username = params[0];
            String request = params[1];

            int result = _libraryManager.userRecord(username, request);
            printResult(result, "");
            _userRecord = false;
        }
    }

    private void getBookRecord(String line)
    {
        String[] params = line.split(_delimiter);

        if(params.length < 2)
        {
            System.out.println("You have not provided the correct parameters, please try again.");
        }
        else
        {
        }
    }

    /**
     * TODO: Implement
     */
    private void browseBooks(String line)
    {
        String[] params = line.split(_delimiter);

        if(params.length == 6)
        {
            String authors = params[0];
            String publisher = params[1];
            String titleWords = params[2];
            String subject = params[3];
            String sort = params[4];
            String status = params[5];

            if(sort.length() == 0)
            {
                System.out.println("You must specify how the results are sorted");
            }
            else if(status.length() == 0)
            {
                System.out.println("You must specify whether you want all books or just available ones");
            }
            else
            {
                authors = authors.length() > 0 ? '%' + authors + '%' : authors;
                publisher = publisher.length() > 0 ? '%' + publisher + '%' : publisher;
                titleWords = titleWords.length() > 0 ? '%' + titleWords + '%' : titleWords;
                subject = subject.length() > 0 ? '%' + subject + '%' : subject;

                int result = _libraryManager.browseLibrary(authors, publisher, titleWords, subject, sort,status);
            }
        }
        else
        {

        }

    }

    /**
     * TODO: Write comments
     */
    private void registerUser(String line)
    {
        String[] params = line.split(_delimiter);

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

            if(phone.length() > 1)
                phone = phone.replaceAll("([-()+])+","");

            int result = _libraryManager.addUser(username, address, name, phone, email);

            String duplicate = "That username already exists, please try another.";
            printResult(result, duplicate);
            _register = false;
        }
    }

    /**
     * Prints a message based on what the result was.
     */
    private void printResult(int result, String duplicate)
    {
        if(result > 0)
            System.out.println("Query successful!");
        else if (result == -1)
            System.out.println(duplicate);
        else
            System.out.println("Your query could not be completed.");
    }

    private String displayHelp()
    {
        return "command\t\t\tParameters, (r) indicates required field, " +
                "separate parameters with a " + _delimiter + "character, leave blank " +
                "space between " + _delimiter + "s for no value \n" +
                "browse\t\t\tauthor,publisher,title-words,subject,sort[](r),status[](r)\n" +
                "register\t\tusername(r),address(r),full name(r),phone number,e-mail address\n" +
                "add book\t\tisbn(r),title,author,subject,publisher,publish year,format,summary\n" +
                "inventory\t\tisbn(r)\n" +
                "checkout\t\tisbn(r),username(r)\n" +
                "checkin\t\t\tisbn(r),username(r),status(-1 for lost, 1 for returning)\n" +
                "late books\t\t\tdate(r) in the format of YYYY-MM-DD\n" +
                "review\t\t\tisbn(r),username(r),score(r),review text" +
                "book stats\t\t\tOne of: most checkedout,most requested,most author,most lost" +
                "user stats\t\t\tOne of: most checkedout,most reviews,most lost\n" +
                "book record\t\tOne of: book data,book copies,book users,book reviews\n" +
                "user record\t\tusername(r),request(r) valid requests: personal data,checkedout books\n" +
                "lost books,requested books,reviewed books\n" +
                "exit\t\t\tLog out of the system\n";
    }

    public static void main(String[] args)
    {
        ControlCenter controlCenter = new ControlCenter();
        Scanner scanner = new Scanner(System.in);
        System.out.println ("Welcome to the CS5530 Library!\n" +
                "Type in a command and press enter. " +
                "To see a list of commands and parameters, type help");
        controlCenter.parseInput(scanner);
        DatabaseSingleton.getInstance().close();
    }
}
