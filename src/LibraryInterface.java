/**
 * Created by Umair on 3/30/2015.
 */
public interface LibraryInterface
{
    public abstract int addBook(String isbn, String title, String author,
                                 String subject, String publisher,
                                 String publishYear, String format,
                                 String summary);
    public abstract int addUser(String userName, String address, String name, String phone, String email);
    public abstract int addToInventory(String isbn, String quantity);
    public abstract int checkout(String isbn, String username);
    public abstract int checkin(String isbn, String username);
    public abstract int addReview(String isbn, String username, String userID, String score, String reviewText);
}
