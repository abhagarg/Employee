import java.io.*;
import java.lang.Thread.State;
import java.util.*;
import java.sql.*;

abstract class Employee implements Serializable
{
    private static int eid;
    private String name;
    private int age;
    private int salary;
    private String designation;
 
 public static int count=0;
 public static Connection con;
 public static Statement stmt;

 static
 {
    
    try
    {
      Class.forName("oracle.jdbc.driver.OracleDriver");
      con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:xe","system","tiger");
      stmt=con.createStatement();
    }
    catch(Exception e)
    {
      System.out.println(e);
    }
 }
 public static void disconnect()
 {
  try
  {
    stmt.close();
    con.close();
  }
  catch(Exception e)
  {
    System.out.println(e);
  }
 }
 public Employee(int salary,String designation) 
 {
    try{
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

        System.out.println("\nEnter employee ID: ");
        eid=Integer.parseInt(br.readLine());
        System.out.println("Enter name of the employee: ");
        name=br.readLine();

        age=InvalidAgeException.readAge();
        this.salary=salary;
        this.designation=designation; 

       PreparedStatement pstmt = con.prepareStatement("insert into EMP values(?,?,?,?,?)");
       pstmt.setInt(1,eid);
       pstmt.setString(2,name);
       pstmt.setInt(3,age);
       pstmt.setInt(4,salary);
       pstmt.setString(5,designation);
       pstmt.execute();

       count++;
    }
    catch(Exception e1)
    {
      System.out.println(e1);
    }
  }
  public static void display()
  {
    try{
      ResultSet rs=stmt.executeQuery("select * from EMP");
      while(rs.next())
      {
        System.out.println("\nID: "+rs.getString(1));
        System.out.println("Name: "+rs.getString(2));
        System.out.println("Age: "+rs.getString(3));
        System.out.println("Salary: "+rs.getString(4));
        System.out.println("Designation: "+rs.getString(5));
      }
      rs.close();
  }
  catch(Exception e2)
  {
    System.out.println(e2);
  }
}
public static void update()
{
  try
  {
    stmt.executeUpdate("update EMP set SALARY=SALARY+2000 where DESIGNATION='Clerk'");
    stmt.executeUpdate("update EMP set SALARY=SALARY+5000 where DESIGNATION='Programmer'");
    stmt.executeUpdate("update EMP set SALARY=SALARY+10000 where DESIGNATION='Manager'");
  }
  catch(Exception e3)
  {
    System.out.println(e3);
  }
}
 public static void remove()
 {
  try{
    BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

    System.out.println("\nEnter Emp ID: ");
    eid=Integer.parseInt(br.readLine());

    ResultSet rs=stmt.executeQuery("select * from EMP where E_ID="+eid);
    if(rs.next())
    {
      System.out.println("\nEmployee ID: "+rs.getString(1));
      System.out.println("Name: "+rs.getString(2));
      System.out.println("Age: "+rs.getString(3));
      System.out.println("Salary: "+rs.getString(4));
      System.out.println("Designation: "+rs.getString(5));

      System.out.println("Do you really want to remove this record? (Y/N)");
      String confirm=br.readLine();
      if(confirm.equals("Y"))
      {
        stmt.executeUpdate("delete  from EMP where E_ID="+eid);
        System.out.println("Successfully removed the employee record");
      }
      else 
        System.out.println("You chose not to remove this employee. Thanks! ");
    }
    else
    {
      System.out.println("Sorry! Invalid Employee ID");
    }
  }
  catch(Exception e)
  {
    System.out.println(e);
  }
 }
 
}
final class Clerk extends Employee
{
 public Clerk()
 {
   super(10000,"Clerk");
 }
}
final class Programmer extends Employee
{
 public Programmer()
 {
  super(25000,"Programmer");
 }
}
final class Manager extends Employee
{
 public Manager()
 {
  super(80000,"Manager");
 }
}
public class EmployeeDB
{
    public static void main(String args[])
    {
     int choice1,choice2;
   
    //ArrayList<Employee> list=Employee.readFromFile();//doesnt create 100 objects. only1 which can refer to 100;
     do
    {
    
     System.out.println("------------------");
     System.out.println("1.Create");
     System.out.println("2.Display");
     System.out.println("3.Update");
     System.out.println("4.Remove");
     System.out.println("5.Exit");
     System.out.println("------------------");
     System.out.println("Enter Choice: ");
     
     choice1=new Scanner(System.in).nextInt();

     switch(choice1)
     {
      case 1: 
            do{
               //ind++;
               System.out.println("------------------");
               System.out.println("1.Clerk");
               System.out.println("2.Programmer");
               System.out.println("3.Manager");
               System.out.println("4.Exit");
               System.out.println("------------------");
               System.out.println("Enter Choice: ");
               
               choice2=new  Scanner(System.in).nextInt();
                 if(choice2==1)
                {
        
                 new Clerk(); //runtime polymorphism
            
                }
                else if(choice2==2)
                {
                  new Programmer();
                  
                }
                else if(choice2==3)
                {
                  new Manager();
                 
                }
            
              }while(choice2!=4);
              break;
            
      case 2: Employee.display();
              
              break;
      case 3: Employee.update();
              break;
      case 4: Employee.remove();
              break;
              
   }

  }while(choice1!=5);
  //Employee.WriteToFile(list);
  System.out.println("Total number of employees joined: "+Employee.count);
 }
}
class InvalidAgeException extends Exception

{
  //int x;
  public InvalidAgeException()
  {
    super();
  }
  public InvalidAgeException(String msg)
  {
    super(msg);
  }
  public static int readAge()
  {
    while(true)
    {
      try{
        System.out.println("Enter age");
        int age=new Scanner(System.in).nextInt();
        if(age>60||age<21)
           throw new InvalidAgeException(" ");
        else 
           return age;
      }
      catch(InputMismatchException e)
      {
        System.out.println("Please enter number only");
      }
      catch(InvalidAgeException e)
      {
        System.out.println("Please enter between 21 and 60 only");
      }
    }
  }
  
 
}