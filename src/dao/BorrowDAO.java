package dao;

import dto.Book;
import dto.Borrow;
import util.DataBaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

        String isCheckSQL = "select book_id , student_id , return_date from borrow where book_id = ? and student_id = ? and return_date is null";
        try (Connection connection = DataBaseUtil.getConnection();
             PreparedStatement checkPstmt = connection.prepareStatement(isCheckSQL)) {
            checkPstmt.setInt(1, bookId);
            checkPstmt.setInt(2, studentPk);
            ResultSet set = checkPstmt.executeQuery();
            if(set.next()){
                // insert , update
                // 어떤 학생이 어떤 책에 반납을 했는지 반납한 시간을 update 해준다 borrow_date
                String updateBorrowSQL = "update borrow set return_date = current_date() where book_id = ? and student_id = ?";
                PreparedStatement updateBorrowPstmt = connection.prepareStatement(updateBorrowSQL);
                updateBorrowPstmt.setInt(1,bookId);
                updateBorrowPstmt.setInt(2,studentPk);

                // 대출을 반납하면 해당 책에 available 를 true로 변경해준다
                String updateBookSQL = "update books set available = true where id = ?";
                PreparedStatement updateBookPstmt = connection.prepareStatement(updateBookSQL);
                updateBookPstmt.setInt(1,bookId);

                updateBorrowPstmt.executeUpdate();
                updateBookPstmt.executeUpdate();
            }else {
                throw new SQLException("해당 학생은 도서 대출을 한적이 없습니다.");
            }
        }
    }

    //

    // 도서 반납을 처리하는 기능 추가
    // 1. borrows 테이블에 책 정보 조회 (check) -- SELECT (복합 조건)
    // 2. borrows 테이블에 return_date 수정 --- UPDATE
    // 3. books 테이블에 available 수정 --- UPDATE
    /*public void returnBook(int bookId, int studentPK ) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseUtil.getConnection();
            // 트랜잭션 시작
            conn.setAutoCommit(false);

            // 이 쿼리에 결과집합에서 필요한 것은 borrows 의 pk(id) 값 이다.
            int borrowId = 0;
            String checkSql = "SELECT id FROM borrows " +
                    "               WHERE book_id = ? " +
                    "                   AND student_id = ? " +
                    "                   AND return_date IS NULL";

            try(PreparedStatement checkPstmt = conn.prepareStatement(checkSql)) {
                checkPstmt.setInt(1, bookId);
                checkPstmt.setInt(2, studentPK);
                ResultSet rs = checkPstmt.executeQuery();
                if(!rs.next()) {
                    throw new SQLException("해당 대출 기록이 존재하지 않거나 이미 반납되었습니다.");
                }
                borrowId = rs.getInt("id");
            }

            String updateBorrowSql = "UPDATE borrows SET return_date = CURRENT_DATE WHERE id = ? ";
            String updateBookSql = "UPDATE books SET available = true WHERE id = ? ";

            try(PreparedStatement borrowPstmt = conn.prepareStatement(updateBorrowSql);
                PreparedStatement bookPstmt = conn.prepareStatement(updateBookSql)) {
                // borrows 설정
                borrowPstmt.setInt(1, borrowId);
                borrowPstmt.executeUpdate(); // 쿼리 실행

                // books 설정
                bookPstmt.setInt(1, bookId);
                bookPstmt.executeUpdate(); // 쿼리 실행
            }
            conn.commit(); // 트랙처리 완료
        } catch (SQLException e) {
            if(conn != null) {
                conn.rollback(); // 오류 발생시 롤백 처리
            }
            System.err.println("rollback 처리를 하였습니다");
        } finally {
            if(conn != null) {
                conn.setAutoCommit(true); // 다시 오토커밋 설정
                conn.close(); // 자원을 닫아야 메모리 누수가 발생하지 않는다.
            }
        }
    }*/

    public List<Borrow> getBorrowedBooks() throws SQLException {

        List<Borrow> borrowslist = new ArrayList<>();
        String sql = "select * from borrow where return_date is null;";
        try (Connection connection = DataBaseUtil.getConnection();
             PreparedStatement checkPstmt = connection.prepareStatement(sql)) {
            ResultSet set = checkPstmt.executeQuery();
            while(set.next()){
                Borrow borrow = new Borrow();
                borrow.setId(set.getInt("id"));
                borrow.setBookId(set.getInt("book_id"));
                borrow.setStudentId(set.getInt("student_id"));
                // JAVA DTO 에서 데이터 타입은 LocalDate 이다.
                borrow.setBorrowDate(set.getDate("borrow_date").toLocalDate());
                borrowslist.add(borrow);
            }
            return  borrowslist;
        }
    }



    public static void main(String[] args) {
        BorrowDAO borrowDAO = new BorrowDAO();
        try{
            borrowDAO.borrowReturnBook(2,4);
//            borrowDAO.borrowBook(2,4);
//            for(int i =0;i<borrowDAO.getBorrowedBooks().size();i++){
//                System.out.println(borrowDAO.getBorrowedBooks().get(i));
//            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
