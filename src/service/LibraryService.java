package service;

/*
* 비즈니스 로직을 처리하는 서비스 클래스
* */

import dao.BookDAO;
import dao.BorrowDAO;
import dao.StudentDAO;
import dto.Book;
import dto.Borrow;
import dto.Student;

import java.sql.SQLException;
import java.util.List;

public class LibraryService {

    private final BookDAO bookDAO = new BookDAO();
    private final StudentDAO studentDAO = new StudentDAO();
    private final BorrowDAO borrowDAO = new BorrowDAO();

    // 책을 추가하는 서비스
    public void addBook(Book book) throws SQLException {
        // 입력 값 유효성 검사
        if(book.getTitle() == null || book.getAuthor() == null ||
                book.getTitle().trim().isEmpty() || book.getAuthor().trim().isEmpty()){
            throw new SQLException("도서 제목과 저자는 필수 입력 항목입니다");
        }
        // 유효성 검사 통과후 BookDAO 에게 일을 협력 요청 한다
        bookDAO.addBook(book);
    }
    public List<Book> getAllBooks() throws SQLException{
        return bookDAO.getAllbBooks();
    }

    // 책 이름으로 조회하는 서비스
    public List<Book> searchBooksByTitle(String title) throws SQLException {
        // 입력값 유효성 검사
        if(title == null || title.trim().isEmpty()){
            throw new SQLException("검색 제목을 입력해 주세요");
        }
        return bookDAO.searchBooksTitle(title);
    }

    // 학생 추가하는 서비스
    public void addStudent(Student student) throws SQLException {
        // 유효성 검사 ... 생략
        studentDAO.addStudent(student);
    }

    // 전체 학생 목록을 조회 하는 서비스
    public List<Student> getAllStudents() throws SQLException{
        return studentDAO.getAllStudents();
    }

    // 도서 대출하기
    public void borrowBook(int bookid, int studentPK) throws SQLException {
        // 유효성 검사(벨리테이션 처리)
        if(bookid <= 0 || studentPK <= 0){
            throw new SQLException("학생아이디 및 책 이름을 입력해주세요");
        }
        // borrowDAO 객체에게 협력 요청하고
        // borrows 테이블에 insert 처리에 책임은 BorrowDAO 객체가 가진다
        borrowDAO.borrowBook(bookid,studentPK);
    }
    // 현재 대출중인 도서 목록 보여 주기
    public List<Borrow> getBorrowedBooks() throws SQLException {
        return borrowDAO.getBorrowedBooks();
    }

    // 도서 대출을 하는 서비스
    public void borrowReturnBook(int bookid , int studentPk) throws SQLException {
        if(bookid <= 0 || studentPk <= 0){
            throw new SQLException("잘못된 입력입니다 다시 입력해주세요");
        }
        borrowDAO.borrowReturnBook(bookid,studentPk);
    }

    // 학생 ID로 학생 인증하는 서비스
    public Student authenticateStudent(String studentId) throws SQLException{
        if(studentId == null || studentId.trim().isEmpty()){
            throw new SQLException("학번을 정상적으로 입력하시오");
        }
        return studentDAO.authenticateStudent(studentId);
    }
}
