package dao;

import dto.Book;
import dto.Borrow;
import util.DataBaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BorrowDAO {

    // 도서 대출을 처리 기능
    public void borrowBook(int bookId, int studentPk) throws SQLException {
        // 대출 가능 여부 ---> select(books)
        // 대출 한다면 ---> insert(borrows)
        // 대출이 실행 되었다면 ---> update(books -> avilable)

        String isCheckSQL = "select available from books where id = ?";
        try (Connection connection = DataBaseUtil.getConnection();
             PreparedStatement checkPstmt = connection.prepareStatement(isCheckSQL)) {
            checkPstmt.setInt(1, bookId);
            ResultSet set = checkPstmt.executeQuery();
            if(set.next() && set.getBoolean("available")){
                // insert , update
                // 학생이 대출을 할시에 대출한 책의 id , 대출을 한 학생 id 그리고 대출 시간을 borrow에 넣어준다
                String insertSQL = "insert into borrow (book_id,student_id,borrow_date) values(?,?,current_date()) ";
                PreparedStatement insertPstmt = connection.prepareStatement(insertSQL);
                insertPstmt.setInt(1,bookId);
                insertPstmt.setInt(2,studentPk);

                // 대출을 하면 기존 books DB안에 대출한 책에 대출여부(available) 를 false 로 변경
                String updateSQL = "update books set available = False where id = ?";
                PreparedStatement updatePstmt = connection.prepareStatement(updateSQL);
                updatePstmt.setInt(1,bookId);

                insertPstmt.executeUpdate();
                updatePstmt.executeUpdate();
            }else {
                throw new SQLException("도서 대출 불가능 합니다");
            }
        }
    }


    public void borrowReturnBook(int bookId, int studentPk) throws SQLException {
        // 대출 가능 여부 ---> select(books)
        // 대출 한다면 ---> insert(borrows)
        // 대출이 실행 되었다면 ---> update(books -> avilable)

        String isCheckSQL = "select book_id  from books where id = ?";
        try (Connection connection = DataBaseUtil.getConnection();
             PreparedStatement checkPstmt = connection.prepareStatement(isCheckSQL)) {
            checkPstmt.setInt(1, bookId);
            ResultSet set = checkPstmt.executeQuery();
            if(set.next() && set.getBoolean("available")){
                // insert , update
                // 학생이 대출을 할시에 대출한 책의 id , 대출을 한 학생 id 그리고 대출 시간을 borrow에 넣어준다
                String insertSQL = "insert into borrow (book_id,student_id,borrow_date) values(?,?,current_date()) ";
                PreparedStatement insertPstmt = connection.prepareStatement(insertSQL);
                insertPstmt.setInt(1,bookId);
                insertPstmt.setInt(2,studentPk);

                // 대출을 하면 기존 books DB안에 대출한 책에 대출여부(available) 를 false 로 변경
                String updateSQL = "update books set available = False where id = ?";
                PreparedStatement updatePstmt = connection.prepareStatement(updateSQL);
                updatePstmt.setInt(1,bookId);

                insertPstmt.executeUpdate();
                updatePstmt.executeUpdate();
            }else {
                throw new SQLException("도서 대출 불가능 합니다");
            }
        }
    }


    public static void main(String[] args) {
        BorrowDAO borrowDAO = new BorrowDAO();
        try{
            borrowDAO.borrowBook(2,4);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
