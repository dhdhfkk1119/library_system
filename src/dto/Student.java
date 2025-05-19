package dto;

import lombok.*;

// @Data (Getter,Setter) 합쳐줌
@Getter
@Setter
@NoArgsConstructor // 기본 생성자 만들어주기 (매개변수)
@AllArgsConstructor // 기본 생성자 에 매개변수를 만들어줌
@ToString
public class Student {
    private int id;
    private String name;
    private String studentId;
}
