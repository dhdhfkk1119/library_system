package dao;

/*
 * 도서 관련 데이터베이스 작업을 처리하는 DAO 클래스
 * */

import dto.Book;
import util.DataBaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class BookDAO {

    // 새 도서를 데이터 베이스에 추가
    public void addBook(Book book) throws SQLException {
        String sql = "insert into books(title,author,publisher,publication_year,isbn) " +
                "values(?,?,?,?,?) ";

        try (Connection conn = DataBaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getPublisher());
            pstmt.setInt(4, book.getPublicationYear());
            pstmt.setString(5, book.getIsbn());
            pstmt.executeUpdate();

        }

    }

    // 모든 도서 목록을 조회 기능 추가
    public List<Book> getAllbBooks() throws SQLException {
        List<Book> bookList = new ArrayList<>();
        String sql = "select * from books";
        try (Connection conn = DataBaseUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet resultSet = stmt.executeQuery(sql);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String publisher = resultSet.getString("publisher");
                int publicationYear = resultSet.getInt("publication_year");
                String isbn = resultSet.getString("isbn");
                boolean available = resultSet.getBoolean("available");

                Book book = new Book(id, title, author, publisher, publicationYear, isbn, available);
                bookList.add(book);
            }
        }
        return bookList;
    }


    // 책 제목으로 도서를 검색 기능
    public List<Book> searchBooksTitle(String searchTitle) throws SQLException {
        List<Book> bookList = new ArrayList<>();
        String sql = "select * from books where title like ? ";
        try (Connection conn = DataBaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + searchTitle + "%");
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String publisher = resultSet.getString("publisher");
                int publicationYear = resultSet.getInt("publication_year");
                String isbn = resultSet.getString("isbn");
                boolean available = resultSet.getBoolean("available");
                Book book = new Book(id, title, author, publisher, publicationYear, isbn, available);
                bookList.add(book);
            }
        }
        return bookList;
    }


    // 수정 , 삭제
    
    ///TODO 삭제 예정 
    // 테스트 코드 작성
    public static void main(String[] args) {
        // 전체 조회 테스트
        BookDAO bookDAO = new BookDAO();

        try {
//            bookDAO.getAllbBooks();
//            for (int i = 0; i < bookDAO.getAllbBooks().size(); i++) {
//                System.out.println(bookDAO.getAllbBooks().get(i));
//            }

            //bookDAO.addBook(new Book(0,"조정우짱이다","조정우","정우네트워크",2023,"9789123123",true));

            ArrayList<Book> selectBookList = (ArrayList) bookDAO.searchBooksTitle("입문");
            for (int i = 0; i < selectBookList.size(); i++) {
                System.out.println(selectBookList.get(i));
            }
//            for (int i = 0; i < bookDAO.searchBooksTitle("웹").size(); i++) {
//                System.out.println(bookDAO.selectBookList().get(i));
//            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
