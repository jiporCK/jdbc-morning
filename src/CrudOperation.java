import java.sql.*;
import java.util.Scanner;

public class CrudOperation {

    private static final String URL = "jdbc:postgresql://localhost:5432/test_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "adminpw";

    private static final Scanner scanner = new Scanner(System.in);

    public void createUser() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        System.out.print("Enter user id: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter user name: ");
        String name = scanner.nextLine();
        System.out.print("Enter user age: ");
        int age = Integer.parseInt(scanner.nextLine());

        User user = new User(id, name, age);
        String sql = """
                insert into users
                values (?, ?, ?)
                """;
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, user.getId());
        ps.setString(2, user.getName());
        ps.setInt(3, user.getAge());

        int rowsAffected = ps.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Inserted successful");
        } else {
            System.out.println("Failed to insert");
        }

        conn.close();
        ps.close();
    }

    public void readUserById() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        System.out.print("Enter user id to find: ");
        int id = Integer.parseInt(scanner.nextLine());

        String sql = """
               select * from users
               where id = ?
               """;
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            User user = new User(
                    rs.getInt("id"), rs.getString("name"), rs.getInt("age")
            );
            System.out.println(user);
        }
    }

    public static void main(String[] args) {
        CrudOperation operation = new CrudOperation();
        try {
//            operation.createUser();
            operation.readUserById();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
