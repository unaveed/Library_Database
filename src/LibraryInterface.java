/**
 * Created by Umair on 3/30/2015.
 */
public interface LibraryInterface
{
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
    public abstract int userStatistics(int count, String request);
}
