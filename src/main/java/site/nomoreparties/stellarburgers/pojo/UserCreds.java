package site.nomoreparties.stellarburgers.pojo;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreds {
    private String email;
    private String password;
    public static UserCreds from(User user) {
        return new UserCreds(user.getEmail(), user.getPassword());
    }
}
