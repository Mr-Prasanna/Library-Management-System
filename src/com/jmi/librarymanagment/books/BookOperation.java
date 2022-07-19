package com.jmi.librarymanagment.books;

import java.util.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.jmi.librarymanagment.dbconnection.DbConnector;

import java.sql.*;
import java.text.SimpleDateFormat;

public class BookOperation{
	Scanner scannerObj = new Scanner(System.in);
	Connection connectionObj = null;
	Statement statementObj = null;

	public void addBooks() {
		System.out.println("Enter the Book Details :");

		System.out.println("Enter Book Name : ");
		String bookName = scannerObj.next();
		System.out.println("Enter Author name : ");
		String author = scannerObj.next();
		System.out.println("Enter Book count : ");
		int count = scannerObj.nextInt();
		try {
			connectionObj = DbConnector.connect();
			statementObj = connectionObj.createStatement();

			statementObj.executeUpdate("INSERT INTO bookinfo (Book_Name,Author,count)  VALUES ('" + bookName + "','"
					+ author + "'," + count + ")");
			System.out.println("Record inserted \n");

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

	public void viewBooks() {

		try {
			connectionObj = DbConnector.connect();
			statementObj = connectionObj.createStatement();
			ResultSet resultsetObj = statementObj.executeQuery("select * from bookinfo where isActive=true");
			System.out.println("\nView the Books:");
			System.out.println("BookId\t Book_Name\t\tAuthor\t\tcount");
			while (resultsetObj.next())
			{
				System.out.println("\r" + resultsetObj.getInt(1) + "\t" + resultsetObj.getString(2) + "\r\t\t\t\t"
						+ resultsetObj.getString(3) + "\t" + resultsetObj.getInt(4));
			}

		}

		catch (Exception e) {
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

	public void updateBook() {

		System.out.println("\nEnter the Book id :");
		int bookId = scannerObj.nextInt();
		System.out.println("Enter the Book Name :");
		String bookName = scannerObj.next();

		try {
			connectionObj = DbConnector.connect();
			statementObj = connectionObj.createStatement();
			statementObj.executeUpdate("update bookinfo SET Book_Name='" + bookName + "'WHERE BookID=" + bookId);
			System.out.println("Book Name updated successfully");

		}

		catch (Exception e) {
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

	public void deleteBook() {

		System.out.println("");
		System.out.println("Enter the Book id :");
		int bookId = scannerObj.nextInt();

		try {
			connectionObj = DbConnector.connect();
			statementObj = connectionObj.createStatement();
			ResultSet resultObj = statementObj.executeQuery("select bookid from bookinfo where bookid=" + bookId);
			if (resultObj.next() == false) {
				System.out.println("This Book ID is incorrect");
				System.out.println("Enter the correct book id");
			} else {

				ResultSet resultsetObj = statementObj
						.executeQuery("select bookid from issued_books where bookid=" + bookId);
				if (resultsetObj.next() == false) {

					statementObj.executeUpdate("update bookinfo set isActive=false WHERE BookID=" + bookId);
					System.out.println("This Book is Deleted Successfully");
				} else {

					ResultSet resultSetObj = statementObj
							.executeQuery("select bookid from issued_books where  status=0 and bookid=" + bookId);
					if (resultSetObj.next() == false) {
						System.out.println("This Book is not returned!!!");
						System.out.println("so you cant delete");
					} else {

						statementObj.executeUpdate("update bookinfo set isActive=false WHERE BookID=" + bookId);
						System.out.println("This Book is Deleted Successfully");

					}
				}
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

	public void issueBooks(int loginid) {

		try {
			connectionObj = DbConnector.connect();
			statementObj = connectionObj.createStatement();
			System.out.println("Enter Book_ID : ");
			int bookId = scannerObj.nextInt();

			ResultSet resultsetObj = statementObj.executeQuery("select bookid from bookinfo where bookid=" + bookId);
			if (resultsetObj.next() == false) {
				System.out.println("This Book ID is incorrect");
				System.out.println("Enter the correct book id");
			} else {

				System.out.println("Enter Issued Date : ");
				String issuedDate = scannerObj.next();

				System.out.println("Enter No.of.Period : ");
				int period = scannerObj.nextInt();
				ResultSet checkcount = statementObj
						.executeQuery("SELECT count FROM bookinfo WHERE count>0 and BookID=" + bookId);
				if (checkcount.next() == true) {
					int queryResult = statementObj
							.executeUpdate("INSERT INTO issued_books(loginid,BookID,Issued_date,Period) " + "VALUES ("
									+ loginid + "," + bookId + ",'" + issuedDate + "'," + period + ")");
					// Reduce the book Count
					if (queryResult == 1) {
						statementObj.executeUpdate("update bookinfo set count=count-1 where  BookID=" + bookId);
						System.out.println("Book Issued.....");
						statementObj.executeUpdate("update issued_books set status=1 where return_date is null");
					}
				} else {
					System.out.println("BOOK IS NOT AVAILABLE !!");
				}

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

	public void returnbook() {

		System.out.println("Enter the Book ID :");
		int bookId = scannerObj.nextInt();
		System.out.println("Enter the return date :");
		String returnDate = scannerObj.next();
		int fine = 0;
		String date1 = null;
		String date2 = returnDate;
		int extradays = 0;

		try {
			connectionObj = DbConnector.connect();
			statementObj = connectionObj.createStatement();
			ResultSet resultsetObj = statementObj.executeQuery(
					"SELECT issued_date FROM issued_books WHERE return_date is null and BookID=" + bookId);

			while (resultsetObj.next()) {
				date1 = resultsetObj.getString(1);
				System.out.println("Book Issued Date" + date1);
			}

		}

		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		try {
			Date date3 = new SimpleDateFormat("dd-MM-yyyy").parse(date1);
			Date date4 = new SimpleDateFormat("dd-MM-yyyy").parse(date2);
			long differance = date4.getTime() - date3.getTime();
			extradays = (int) (TimeUnit.DAYS.convert(differance, TimeUnit.MILLISECONDS));
			System.out.println("Total Days : " + extradays);

		} catch (Exception e1) {
			System.out.println(e1.getMessage());
			System.out.println("\nYou have no book so..You cannot return\n");
		}
		try {

			// Update the book count

			statementObj.executeUpdate("UPDATE issued_books SET return_date='" + returnDate
					+ "' WHERE return_date is null and BookID=" + bookId);

			statementObj.executeUpdate("update bookinfo set count=count+1 where BookID=" + bookId);
			statementObj.executeUpdate("update issued_books set status=0 where return_date is not null");

			System.out.println("Returned Date" + returnDate);

			ResultSet resultSetObj = statementObj
					.executeQuery("SELECT Period FROM issued_books WHERE BookID=" + bookId);

			String differance = null;
			while (resultSetObj.next()) {
				differance = resultSetObj.getString(1);
			}
			int differanceNum = Integer.parseInt(differance);
			if (extradays > differanceNum) {
				fine = (extradays - differanceNum) * 100;
			}

			statementObj.executeUpdate("UPDATE issued_books SET Fine=" + fine + " WHERE BookID=" + bookId);
			System.out.println("Fine Amount :" + fine);

		}

		catch (Exception e) {
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

	public void viewIssuedBooks() {

		try {
			connectionObj = DbConnector.connect();
			statementObj = connectionObj.createStatement();
			ResultSet resultSetObj = statementObj.executeQuery(
					"select issued_books.loginid,logininfo.Name,issued_books.bookid,bookinfo.book_name,issued_books.issued_date,issued_books.return_date,issued_books.period,issued_books.fine from logininfo inner join issued_books on logininfo.loginid=issued_books.loginid inner join bookinfo on bookinfo.bookid=issued_books.bookid;");
			System.out.println(
					"+-------------------------------------------------------------------------------------------+");
			System.out.println("|UserID\tUsername\tBook_ID\tBook_Name\tIssue_date\tReturn_Date\tPeriod\tFine|");
			System.out.println(
					"+-------------------------------------------------------------------------------------------+");
			while (resultSetObj.next()) {
				System.out.println(+resultSetObj.getInt(1) + "\t" + resultSetObj.getString(2) + "\r\t\t\t"
						+ resultSetObj.getInt(3) + "\t" + resultSetObj.getString(4) + "\r\t\t\t\t\t\t"
						+ resultSetObj.getString(5) + "\t" + resultSetObj.getString(6) + "\t" + resultSetObj.getInt(7)
						+ "\t" + resultSetObj.getInt(8));
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

	public void takenBooks(int loginid) {
		System.out.println("User Taken Books :");

		try {
			connectionObj = DbConnector.connect();
			statementObj = connectionObj.createStatement();
			ResultSet resultSetObj = statementObj.executeQuery(
					"select issued_books.id,issued_books.bookid,bookinfo.book_name,issued_books.issued_date "
							+ "from logininfo inner join issued_books " + "on logininfo.loginid=issued_books.loginid "
							+ "inner join bookinfo on bookinfo.bookid=issued_books.bookid "
							+ "where issued_books.Return_Date is null and issued_books.loginid=" + loginid);

			System.out.println("\nIm Taken Books:\n");
			System.out.println("+-----------------------------------------+");
			System.out.println("|ID\tBookID\tBook_Name\tIssue_date|");
			System.out.println("+-----------------------------------------+");

			while (resultSetObj.next()) {

				System.out.println(resultSetObj.getInt(1) + "\t" + resultSetObj.getInt(2) + "\t"
						+ resultSetObj.getString(3) + "\t" + resultSetObj.getString(4));
			}

		}

		catch (Exception e) {
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
