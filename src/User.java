
public class User {
    private String mUsername;
    private int mUserID;
    private String mAddress;
    private String mFullName;
    private String mPhoneNumber;
    private String mEmail;

    public User(String user, int id, String address, String name, String number, String email)
    {
        mUsername = user;
        mUserID = id;
        mAddress = address;
        mFullName = name;
        mPhoneNumber = number;
        mEmail = email;
    }

    public User(String user, int id, String address, String name)
    {
        mUsername = user;
        mUserID = id;
        mAddress = address;
        mFullName = name;
        mPhoneNumber = null;
        mEmail = null;
    }
}
