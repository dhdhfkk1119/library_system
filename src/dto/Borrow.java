package dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자 만들어주기 (매개변수)
@AllArgsConstructor // 기본 생성자 에 매개변수를 만들어줌
@ToString
public class Borrow {
    private int id;
    private int bookId;
    private int studentId;
    private LocalDate borrowDate;
    private LocalDate returnDate;
}
