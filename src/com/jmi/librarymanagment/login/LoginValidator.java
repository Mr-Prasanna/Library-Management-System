package com.jmi.librarymanagment.login;

import java.util.*;
import com.jmi.librarymanagment.user.UserValidator;
import com.jmi.librarymanagment.user.AdminValidator;

public class LoginValidator {

	public static void main(String args[]) {

		@SuppressWarnings("resource")
		Scanner scannerObj = new Scanner(System.in);

		boolean active = true;
		try {
			while (active) {
				System.out.println("Welcome to JMI LIBRARY");
				System.out.println("1.ADMIN");
				System.out.println("2.USER");
				System.out.println("3.REGISTER");
				System.out.println("4.EXIT");
				System.out.println("Choose the choice : ");
				int choice = scannerObj.nextInt();

				switch (choice) {
				case 1:
					AdminValidator adminObj = new AdminValidator();
					adminObj.adminValidation();
					break;
				case 2:
					UserValidator userObj = new UserValidator();
					userObj.userValidation();
					break;
				case 3:
					UserValidator newUserObj = new UserValidator();
					newUserObj.registerUser();
					break;
				case 4:
					System.out.println("Thank you....");
					System.out.println("Visit again Library....");
					active = false;
					break;
				default:
					System.out.println("Invalid");
					break;
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
