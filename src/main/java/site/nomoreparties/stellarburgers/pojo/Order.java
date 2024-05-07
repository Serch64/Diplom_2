package site.nomoreparties.stellarburgers.pojo;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private String _id;
    private String ingredients;
    private String status;
    private String name;
    private String createdAt;
    private String updatedAt;
    private int number;
}