import java.sql.Date;

/**
 * Created by Umair on 3/30/2015.
 */
public interface LibraryInterface
{
    public abstract int browseLibrary(String authors, String publisher, String titleWords,
                                      String subject, String sort, String status);
    public abstract int addBook(String isbn, String title, String author,
                                 String subject, String publisher,
                                 String publishYear, String format,
                                 String summary);
    public abstract int addInventory(String isbn);
    public abstract int addUser(String username, String address, String name, String phone, String email);
    public abstract int checkout(String isbn, String username);
    public abstract int checkin(String isbn, String username, int status);
    public abstract int addReview(String isbn, String username, String userID, String score, String reviewText);
    public abstract int userRecord(String username, String request);
    public abstract int lateBooks(Date date);
    public abstract int bookRecord(String isbn, String request);
    public abstract int userStatistics(int count, String request);
    public abstract int bookStatistics(int count, String request);
}
