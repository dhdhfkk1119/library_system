package dao;

import dto.Student;
import util.DataBaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    //새 학생 데이터베이스에 추가하는 기능
    public void addStudent(Student student) throws SQLException {
        // 쿼리문..
        String sql = "insert into student(name,student_id) values(?,?) ";
        try (Connection connection = DataBaseUtil.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getStudentId());
            pstmt.executeUpdate();
        }
    }

    // 모든 학생 목록 조회하는 기능
    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "select * from student";
        try (Connection conn = DataBaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                Student student = new Student();
                student.setId(resultSet.getInt("id"));
                student.setName(resultSet.getString("name"));
                student.setStudentId(resultSet.getString("student_id"));
                students.add(student);
            }
        }
        return students;
    }

    // 학생 student_id로 학생 인증(로그인 용)
    public Student authenticateStudent(String IsStudentId) throws SQLException {
        // 학생이 정확한 학번을 입력하면 Student 객체가 만들어져서 리턴 됨
        // 학생이 잘못된 학번을 입력하면 null 값을 반환함

        Student students = new Student();
        String sql = "select * from student where student_id = ?";
        try (Connection conn = DataBaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, IsStudentId);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                students.setId(resultSet.getInt("id"));
                students.setName(resultSet.getString("name"));
                students.setStudentId(resultSet.getString("student_id"));
                return students;
            }
            return null;
        }
    }

    public static void main(String[] args) {
        StudentDAO dao = new StudentDAO();

        try {
            // 학생 추가하기
            // dao.addStudent(new Student(0, "조정우", "20212955")); //데이터 넣기

            // 해당 학생 조회
            System.out.println(dao.authenticateStudent("20230002"));
                // 전체 학생 조회
//            for (int i=0;i<dao.getAllStudents().size(); i++){
//                System.out.println(dao.getAllStudents().get(i));
//            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
