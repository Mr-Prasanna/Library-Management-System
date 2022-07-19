package com.jmi.librarymanagment.user;

import java.util.*;

import com.jmi.librarymanagment.books.BookOperation;
import com.jmi.librarymanagment.dbconnection.DbConnector;

import java.sql.*;

public class UserValidator {

	Scanner scannerObj = new Scanner(System.in);
	Connection connectionObj = null;
	Statement statementObj = null;

	public void registerUser() {

		System.out.println("Enter the Name");
		String userName = scannerObj.next();
		System.out.println("Enter the New Password");
		String password = scannerObj.next();

		try {
			connectionObj = DbConnector.connect();
			statementObj = connectionObj.createStatement();

			statementObj.executeUpdate(
					"INSERT INTO logininfo (Name,Password) \n" + "VALUES ('" + userName + "','" + password + "')");
			System.out.println("Record inserted");

		} catch (Exception e) {

			System.out.println(e.getMessage());
		} finally {
			try {
				if (statementObj != null) {
					statementObj.close();
				}
				if (connectionObj != null) {
					connectionObj.close();
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}

	}

	public void userValidation() {
		System.out.println("Welcome to USER Login page");
		scannerObj = new Scanner(System.in);
		System.out.println("Username : ");
		String userName = scannerObj.nextLine();
		System.out.println("Password : ");
		String password = scannerObj.nextLine();

		int loginid;
		int logid;
		try {
			connectionObj = DbConnector.connect();
			statementObj = connectionObj.createStatement();

			ResultSet resultSetObj = statementObj
					.executeQuery("select loginid from logininfo where role='user' and Name='" + userName
							+ "' AND PASSWORD='" + password + "'");

			if (resultSetObj.next() == true) {
				System.out.println("Login Successfully\n");

				boolean active = true;
				while (active) {
					System.out.println("Choose the choice :");

					System.out.println("1.Available books");
					System.out.println("2.Pick Books");
					System.out.println("3.Taken Books");
					System.out.println("4.Return Books");
					System.out.println("5.Exit ");
					System.out.println("Enter the number :");
					int choose = scannerObj.nextInt();
					BookOperation bookObj = new BookOperation();
					switch (choose) {
					case 1:

						bookObj.viewBooks();
						break;

					case 2:
						loginid = resultSetObj.getInt(1);

						bookObj.issueBooks(loginid);
						break;
					case 3:
						logid = resultSetObj.getInt(1);

						bookObj.takenBooks(logid);
						break;
					case 4:

						bookObj.returnbook();
						break;

					case 5:
						System.out.println("Thank you for visiting....");
						System.out.println("Closing the User Login ");
						active = false;

						break;
					default:
						break;
					}
				}
			} else {

				System.out.println("Invalid username/password");
				System.out.println("If you are new user please register before login!!!");
				userValidation();

			}
		} catch (Exception e) {

			System.out.println(e.getMessage());
		} finally {
			try {
				if (statementObj != null) {
					statementObj.close();
				}
				if (connectionObj != null) {
					connectionObj.close();
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}

	}

}
