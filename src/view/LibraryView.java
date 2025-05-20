package view;

/*
* 사용자 인터페이스를 처리하는 뷰 클래스
* */

import dto.Student;
import service.LibraryService;

import java.sql.SQLException;
import java.util.Scanner;

public class LibraryView {
    private final LibraryService service = new LibraryService();
    private final Scanner scanner = new Scanner(System.in);
    private Integer currentStudentId = null;
    private String currentStudentName = null;
    // private Student principaUser = null; 접근 주제

    public void start(){
        while (true){
            System.out.println("=====도서관리 시스템=====");
            if(currentStudentId == null){
                System.out.println("현재 로그아웃 상태 입니다");
            } else {
                System.out.println("현재 로그인 유저 : " + currentStudentName);
            }

            System.out.println("1. 도서 추가");
            System.out.println("2. 도서 목록");
            System.out.println("3. 도서 검색");
            System.out.println("4. 학생 등록");
            System.out.println("5. 학생 목록");
            System.out.println("6. 도서 대출");
            System.out.println("7. 도서 대출 목록");
            System.out.println("8. 도서 반납");
            System.out.println("9. 로그인");
            System.out.println("10. 로그아웃");
            System.out.println("11. 종료");
            System.out.print("선택 : ");
            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("정수 값을 입력해 주시기 바랍니다");
                scanner.nextLine();
                continue;
            }

            try {
                switch (choice){
                    case 1 :
                        System.out.println("도서 추가");
                        break;
                    case 2 :
                        System.out.println("도서 목록");
                        break;
                    case 3 :
                        System.out.println("도서 검색");
                        break;
                    case 4 :
                        System.out.println("학생 등록");
                        break;
                    case 5 :
                        System.out.println("학생 목록");
                        break;
                    case 6 :
                        borrowBook();
                        break;
                    case 7 :
                        System.out.println("도서 대출 목록");
                        break;
                    case 8 :
                        System.out.println("도서 반납");
                        break;
                    case 9 :
                        login();
                        break;
                    case 10 :
                        logout();
                        break;
                    case 11 :
                        System.out.println("시스템 종료");
                        scanner.close();
                        return;
                    default:
                        System.out.println("잘못된 입력입니다");
                        break;
                }
            } catch (SQLException e) {
                System.out.println("오류 발생 : " + e.getMessage());
            } catch (Exception e){
                System.out.println("전체 오류 : " + e.getMessage());
            }
        } // end of while

    }

    // 로그인 기능
    private void login() throws SQLException {
        if(currentStudentId != null){
            System.out.println("이미 로그인된 상태입니다");
            return;
        }
        System.out.println("학번 : ");
        String studentId = scanner.nextLine().trim();
        if(studentId.trim().isEmpty()){
            System.out.println("학번을 입력해주시요");
            return;
        }
        // 1. 학번을 입력 받았다면 -> 실제 학번이 맞는지 확인
        // 1.1 (데이터 접근해서 해당하는 학번(비밀번호) 맞는지 조회
        
        // 이 메서드에 결과는 두가지(객제 존재 , null)
        Student studentDTO = service.authenticateStudent(studentId);
        if (studentDTO == null){
            System.out.println("존재 하지 않는 학번입니다");
        }else {
            currentStudentId = studentDTO.getId();
            currentStudentName = studentDTO.getName();
            System.out.println("로그인 성공" + currentStudentName);
        }
    }

    // 로그아웃 기능
    private void logout(){
        if(currentStudentId == null){
            System.out.println("이미 로그인 상태가 아닙니다");
        }else {
            currentStudentId = null;
            currentStudentName = null;
            System.out.println("로그아웃 완료");
        }

    }

    public void borrowBook() throws SQLException{
        // 로그인 먼저 되어야 student_id 값을 넘겨 줄 수 있다
        if(currentStudentId == null){
            System.out.println("로그인 해주시기 바랍니다");
        }else {
            System.out.println("도서 ID : ");
            int bookId;
            try {
                bookId = scanner.nextInt();
                if (bookId <= 0) {
                    System.out.println("유효한 도서 아이디를 입력해주세요");
                }
            } catch (Exception e) {
                System.out.println("유효한 도서 ID를 입력해주시여");
                return;
            }
            scanner.nextLine();
            service.borrowBook(bookId, currentStudentId);
            System.out.println("도서 대출이 완료 되었습니다 : " + bookId + currentStudentId);
        }
    }

    public static void main(String[] args) {
        LibraryView libraryView = new LibraryView();
        libraryView.start();
    }
}
