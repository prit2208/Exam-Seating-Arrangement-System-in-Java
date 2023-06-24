import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExamSeatingArrangementSystem {
    private List<Student> students;
    private List<ExamHall> examHalls;

    public ExamSeatingArrangementSystem() {
        this.students = new ArrayList<>();
        this.examHalls = new ArrayList<>();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void addExamHall(ExamHall examHall) {
        examHalls.add(examHall);
    }

    public void generateSeatingArrangements() {
        int totalStudents = students.size();
        int totalHalls = examHalls.size();
        int studentsPerHall = totalStudents / totalHalls;
        int remainingStudents = totalStudents % totalHalls;

        int studentIndex = 0;
        for (ExamHall examHall : examHalls) {
            for (int i = 0; i < studentsPerHall; i++) {
                if (studentIndex < totalStudents) {
                    examHall.getAllocatedStudents().add(students.get(studentIndex));
                    studentIndex++;
                }
            }
            if (remainingStudents > 0) {
                examHall.getAllocatedStudents().add(students.get(studentIndex));
                studentIndex++;
                remainingStudents--;
            }
        }
    }

    public void insertStudentsIntoDatabase() {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // Establish database connection
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/examseating", "root", "password");

            // Prepare SQL statement
            String sql = "INSERT INTO students (student_id, student_name) VALUES (?, ?)";
            statement = connection.prepareStatement(sql);

            // Insert students into the database
            for (Student student : students) {
                statement.setInt(1, student.getStudentId());
                statement.setString(2, student.getStudentName());
                statement.executeUpdate();
            }

            System.out.println("Students inserted into the database.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close statement and connection
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Student> retrieveStudentsFromDatabase() {
        List<Student> retrievedStudents = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Establish database connection
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/examseating", "root", "password");

            // Prepare SQL statement
            String sql = "SELECT student_id, student_name FROM students";
            statement = connection.prepareStatement(sql);

            // Retrieve students from the database
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int studentId = resultSet.getInt("student_id");
                String studentName = resultSet.getString("student_name");
                retrievedStudents.add(new Student(studentId, studentName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close result set, statement, and connection
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return retrievedStudents;
    }

    public void displaySeatingArrangements() {
        for (ExamHall examHall : examHalls) {
            System.out.println("Exam Hall: " + examHall.getHallId());
            for (Student student : examHall.getAllocatedStudents()) {
                System.out.println("Seat: " + examHall.getAllocatedStudents().indexOf(student) + ", Student: " + student.getStudentName());
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        ExamSeatingArrangementSystem system = new ExamSeatingArrangementSystem();

        // Add students
        system.addStudent(new Student(1, "John Doe"));
        system.addStudent(new Student(2, "Jane Smith"));
        system.addStudent(new Student(3, "Alice Johnson"));
        // Add more students as needed

        // Add exam halls
        system.addExamHall(new ExamHall(1, 30));
        system.addExamHall(new ExamHall(2, 25));
        system.addExamHall(new ExamHall(3, 35));
        // Add more exam halls as needed

        // Generate seating arrangements
        system.generateSeatingArrangements();

        // Insert students into the database
        system.insertStudentsIntoDatabase();

        // Retrieve students from the database
        List<Student> retrievedStudents = system.retrieveStudentsFromDatabase();

        // Display seating arrangements
        system.displaySeatingArrangements();
    }
}
