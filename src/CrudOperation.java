import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CrudOperation {

    private static final String URL = "jdbc:postgresql://localhost:5432/test_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "adminpw";

    private static final Scanner scanner = new Scanner(System.in);
    public static List<User> userList = new ArrayList<>();

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

        if (!existById(id)) {
            System.out.println("User does not exist!");
        }

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

    public void updateUserById() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        System.out.print("Enter id to update: ");
        int id = Integer.parseInt(scanner.nextLine());

        if (!existById(id)) {
            System.out.println("User not found");
        }
        System.out.print("Enter new name: ");
        String newName = scanner.nextLine();
        System.out.print("Enter new age: ");
        int newAge = Integer.parseInt(scanner.nextLine());

        String sql = """
                update users
                set name = ?, age = ?
                where id = ?
                """;
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, newName);
        ps.setInt(2, newAge);
        ps.setInt(3, id);

        int rowsAffected = ps.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Updated successful");
        } else {
            System.out.println("Failed to update");
        }
    }

    public static boolean existById(int id) throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        String sql = """
                select 1 from users where id = ?
                """;
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();

        return rs.next();
    }

    public static void main(String[] args) {
        CrudOperation operation = new CrudOperation();

        while (true) {
            System.out.println("""
                    1. Add user
                    2. Read by id
                    3. Update by id
                    4. Delete by id
                    5. Read all
                    0. Exit program""");
            System.out.print("Enter an option: ");
            int op = Integer.parseInt(scanner.nextLine());

            if (op == 0) break;

            try {
                switch (op) {
                    case 1 -> operation.createUser();
                    case 2 -> operation.readUserById();
                    case 3 -> operation.updateUserById();
                    default -> System.out.println("Invalid");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }

    }
}
