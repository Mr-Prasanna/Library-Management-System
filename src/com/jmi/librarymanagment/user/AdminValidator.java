package com.jmi.librarymanagment.user;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import com.jmi.librarymanagment.books.BookOperation;
import com.jmi.librarymanagment.dbconnection.DbConnector;

public class AdminValidator {

	@SuppressWarnings("resource")
	public void adminValidation() {
		Scanner scannerObj = new Scanner(System.in);
		System.out.println("Welcome to Admin Login Page");
		scannerObj = new Scanner(System.in);
		System.out.println("Username : ");
		String userName = scannerObj.next();
		System.out.println("Password :");
		String password = scannerObj.next();
		Connection connectionObj = null;
		Statement statementObj = null;
		try {

			connectionObj = DbConnector.connect();
			statementObj = connectionObj.createStatement();
			ResultSet resultsetObj = statementObj.executeQuery("select * from logininfo where role='admin' and NAME='"
					+ userName + "' AND PASSWORD='" + password + "'");
			if (resultsetObj.next() == true) {
				System.out.println("Login Successfully\n");

				boolean active = true;
				while (active) {
					System.out.println("1.Available Books");
					System.out.println("2.Update Book information");
					System.out.println("3.Delete Book information");
					System.out.println("4.Add Books");
					System.out.println("5.View Issued books");
					System.out.println("6.EXIT");
					System.out.println("Enter the choice :");
					int choice = scannerObj.nextInt();
					BookOperation bookObj = new BookOperation();
					switch (choice) {

					case 1:
						bookObj.viewBooks();
						break;
					case 2:
						bookObj.updateBook();
						break;
					case 3:

						bookObj.deleteBook();
						break;
					case 4:
						bookObj.addBooks();
						break;
					case 5:

						bookObj.viewIssuedBooks();
						break;
					case 6:
						System.out.println("Closed the Admin Page...");
						active = false;
						break;
					default:
						break;
					}
				}
			} else {

				System.out.println("Invalid Admin_name/password");
				adminValidation();

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
