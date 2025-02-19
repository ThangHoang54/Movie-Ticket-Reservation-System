# COSC2440 Further Programming Lab Test - Movie Ticket Reservation System Application 📽️🎫

Welcome to the COSC2440 Further Programming Lab Test repository! This project serves as the Lab Test source code 
for COSC2440 Further Programming course.

## About the Project ℹ️
A movie ticket reservation plays a vital role in the efficient operation of 
the modern theaters. This project involves using __JavaFX__ to create an intuitive front-end interface
and __Java__ to develop the back-end functionality.

In addition to selling tickets at physical counters, most theaters now offer the convenience of online reservations through their websites. This system will facilitate
the online booking of movies tickets. Each user can make one or more bookings, with each booking linked to a specific movie show. The booking includes details of the seats 
reserved for the chosen show.

## Feature 🎯
> ___User___

- User can create an account, login to perform booking function.
- User can view their personal information (name, email, list of bookings).
- User can view show timing for selected theater.
- User can make one or more online booking tickets at one time.
- User can view and take the screenshot of their booking QR code after buying movie ticket(s) successfully.

> ___Admin___

- Admin can not sign up.
- Admin can perform add and update operations on users. Also, a customer
  can be searched by name or contact information, and the search results will be 
  ascending ordered.
- Admin can perform add, update and delete operations on bookings. Also, an order can be
  searched by its id or the corresponding screen and the search results will be ascending ordered.
- Admin can perform add, update and delete operations on screens. Also, a screen
  can be searched by movie name, and the search results will be ordered with respect to show timings
  in ascending order.
- Admin can perform add, update and delete operations on theater. Also, a theater
  can be searched by name or address, and the search results will be ordered with respect to name in
  ascending order.

## Getting Started 🚀
- You must install the SQL Workbench 8.0 CE into your local device (computer, laptop, desktop) in order to the app retrieve data successfully.
<br></br>
- Dependencies:
    - [JavaFX 23.0.1](https://gluonhq.com/products/javafx/)
    - [Apache Maven 3.9.9](https://maven.apache.org/download.cgi)
<br></br>
- Run from source code:
1. Clone this repository to your local machine.
2. Sync the Maven dependencies provided in `pom.xml`.
3. Copy the SQL script in [sql.file](src/main/resources/org/example/finalexam/SQL_Data/Movie_Ticket_SQL.sql) into 
SQL Workbench 8.0 CE and execute it.
4. Go to [DatabaseConnection_Class](src/main/java/org/example/finalexam/utils/DatabaseConnection.java) and change the PASSWORD
to your registered password.
3. Compile the Java source code and run the program.
4. Explore the functionalities and provide feedback for improvements.

> __Sample user accounts__

| Full Name             | Email                        |
|-----------------------|------------------------------|
| Le Pham Quang Dai     | 19042005dai@gmail.com        |
| Dang Hoang My         | danghoangmy1827@gmail.com    |
| Nguyen Thuc Thuy Tien | Talents@senvangvn.com        |
| Son Tung MTP          | booking@mtpentertainment.com |

> __Admin account__

| Username    | Password   |
|-------------|------------|
| movieAdmin  | admin123!  |

## Author 👨‍💻

- **Hoang Minh Thang** - [ThangHoang54](https://github.com/ThangHoang54).


## Technologies Used 💻

Java, Maven, JavaFX, JAR, JUnit, MySQL with MySQLWorkbench 8.0 CE, JDBC, IntelliJ, Git

<p align="center">
  <a href="https://skillicons.dev">
    <img src="https://skillicons.dev/icons?i=git,idea,java,mysql"/>
  </a>
</p>

## Acknowledgments 🙏

Special thanks to the course instructors for providing guidance and resources for this assignment.
Additionally, gratitude to all contributors and users who help improve the system through feedback and suggestions.

Thank you for visiting the COSC2440 Further Programming Lab Test repository. Happy coding! 🎉