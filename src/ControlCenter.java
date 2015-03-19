import java.util.Scanner;

public class ControlCenter {

    public ControlCenter()
    {

    }

    private static String displayHelp()
    {
        StringBuilder helpOptions = new StringBuilder();
        helpOptions.append("command\t\t\tParameters, (r) indicates required field, " +
                "blank space between comma if optional field not needed.\n");
        helpOptions.append("register\t\tenter the username(r),address(r),full name(r),phone number,e-mail address\n");
        helpOptions.append("checkout\t\tenter username and either isbn:isbn or two of author,title,publisher");

        helpOptions.append("exit\t\t\tLog out of the system");

        return helpOptions.toString();
    }

    public static void main(String[] args)
    {
        LibraryManager manager = new LibraryManager();
        Scanner inputReader = new Scanner(System.in);
        boolean register = false, addBook = false, checkout = false;

        while(inputReader.hasNext())
        {
            String line = inputReader.nextLine();

            if(register)
            {
                int res = manager.registerUser(line.split(","));
                if(res == -1)
                    System.out.println("That username is already taken, please try a different one.");

                register = false;
                continue;
            }
            else if(checkout)
            {
                int res = manager.requestBook(line.split(","));
            }

            if(line.equalsIgnoreCase("register"))
            {
                register = true;
            }
            else if(line.equalsIgnoreCase("checkout"))
            {
                checkout = true;
            }
            else if(line.equalsIgnoreCase("help"))
            {
                System.out.println(displayHelp());
            }
            else if(line.equalsIgnoreCase("exit"))
            {
                break ;
            }
            else
                System.out.println("you said >> " + line);
        }
        inputReader.close();
        manager.close();
    }
}
