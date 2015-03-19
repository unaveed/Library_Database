public class Example {

//    public static void main(String[] args) {
//        System.out.println("Example for CS5530");
//        Connection con = null;
//
//        try
//        {
//            //remember to replace the password
//            String userName = "cs5530";
//            String password = "cs5530_Spring2015";
//            String url = "jdbc:mysql://georgia.eng.utah.edu/cs5530db";
//            Class.forName ("com.mysql.jdbc.Driver").newInstance ();
//            con = DriverManager.getConnection(url, userName, password);
//            System.out.println ("Database connection established");
//            Statement stmt=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
//
//            String sql="select * from course";
//            ResultSet rs=stmt.executeQuery(sql);
//            while (rs.next())
//            {
//                System.out.print("cname:");
//                System.out.print(rs.getString("cname")+" || ");
//            }
//            System.out.println(" ");
//            rs.close();
//
//            // how to do query which take user input?
//            System.out.println("Enter a name to search by: ");
//            Scanner in = new Scanner(System.in);
//            String name = in.nextLine();
//
//            ////// OPTION 1 - DO NOT USE, BUT IT IS SOMETHING THAT CAN BE DONE ///////
//		    /*String query_1 = "SELECT * FROM student where sname LIKE \'%" + name + "%\'";
//
//		    ResultSet rs1=stmt.executeQuery(query_1);
//		    System.out.println("Results for query 1");
//		    while(rs1.next())
//		    {
//		    	System.out.print("name:");
//		    		System.out.print(rs1.getString("sname"));
//		    }
//		    System.out.println("");
//		    */
//
//
//            /////// OPTION 2 - USE THIS TECHNIQUE //////
//            String query_2 = "SELECT * FROM student where sname LIKE ?";
//            PreparedStatement query_2_statment = con.prepareStatement(query_2);
//
//            query_2_statment.setString(1, "%" + name + "%");
//            ResultSet rs2=query_2_statment.executeQuery();
//            System.out.println("Results for query 2");
//            while(rs2.next())
//            {
//                System.out.print("name:");
//                System.out.print(rs2.getString("sname"));
//            }
//            System.out.println("");
//
//            stmt.close();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            System.err.println ("Cannot connect to database server");
//        }
//        finally
//        {
//            if (con != null)
//            {
//                try
//                {
//                    con.close ();
//                    System.out.println ("Database connection terminated");
//                }
//                catch (Exception e) { /* ignore close errors */ }
//            }
//        }
//
//        System.out.println("DONE");
//    }

}
