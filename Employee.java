package com.employee.assignment;

import java.util.* ;
import java.io.*;
import java.sql.*;

public abstract class Employee implements java.io.Serializable {
    private int age;
    private double salary;
    private String designation;
    private String name;
    private int id;
    private static Connection con;
    private static Statement stmt;

    static{
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "123");
            stmt = con.createStatement();
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }
    //Main constructor
    Employee(String name, int age, double salary, String designation) {
        this.name = name;
        this.age = age;
        this.salary = salary;
        this.designation = designation;
        this.id = (int)(Math.random()*100)+100;
    }

    Employee(double salary, String designation) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try{
            PreparedStatement pstmt = con.prepareStatement("insert into employees values(?,?,?,?,?)");
            System.out.println("----------------New Employee------------------");
            System.out.print("Name: ");
            this.name = br.readLine();
            this.age = UserException.readAge();
            this.salary = salary;
            this.designation = designation;
            //Randomly generated id between 0 to 100
            this.id = (int)(Math.random()*100)+100;

            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setInt(3, age);
            pstmt.setString(4, designation);
            pstmt.setString(5, ("" + salary));

            pstmt.execute();
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    public String toString() {
        return "Name: " + this.name + "\nAge: " + this.age + "\nSalary: " + this.salary + "\nDesignation: "+ this.designation;
    }

    public static void raiseSalary() {
        try{
            stmt.executeUpdate("update employees set emp_salary=to_char(to_number(emp_salary)+2000) where emp_designation='CLERK'");
            stmt.executeUpdate("update employees set emp_salary=to_char(to_number(emp_salary)+5000) where emp_designation='PROGRAMMER'");
            stmt.executeUpdate("update employees set emp_salary=to_char(to_number(emp_salary)+10000) where emp_designation='MANAGER'");
        }
        catch(Exception e) {
            System.out.println(e);
        }

    }

    public static void sortBy(String param) {
        try{
            ResultSet rs = stmt.executeQuery("select * from employees order by emp_" + param);
            System.out.println("-------------------------------");
            System.out.println("ID Name Age Designation Salary");
            System.out.println("-------------------------------");
            while(rs.next()) {
                    System.out.print(rs.getInt(1));
                    System.out.print(" " + rs.getString(2));
                    System.out.print(" " + rs.getInt(3));
                    System.out.print(" " + rs.getString(4));
                    System.out.println(" " + rs.getString(5));
            }
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    public static int getCount()  {
        try{
            ResultSet rs = stmt.executeQuery("select count(*) from employees");
            return rs.next()? rs.getInt(1) : 0;
        }
        catch(Exception e) {
            System.out.println(e);
            return 0;
        }
    }

}
