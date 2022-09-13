package com.ead.authuser.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    public interface UserView {
        public static interface RegistrationPost{}
        public static interface UserPut{}
        public static interface PasswordPut{}
        public static interface ImagePut{}
    }


    private UUID userId;

    @NotBlank(groups = UserView.RegistrationPost.class)
    @Size(max = 50, min = 4)
    @JsonView(UserView.RegistrationPost.class)
    private String username;

    @Email(groups = UserView.RegistrationPost.class)
    @NotBlank(groups = UserView.RegistrationPost.class)
    @JsonView(UserView.RegistrationPost.class)
    private String email;

    @NotBlank(groups = {UserView.RegistrationPost.class,UserView.PasswordPut.class})
    @Size(max = 20, min = 6)
    @JsonView({UserView.RegistrationPost.class,UserView.PasswordPut.class})
    private String password;

    @NotBlank(groups = UserView.PasswordPut.class)
    @Size(max = 20, min = 6)
    @JsonView({UserView.PasswordPut.class})
    private String oldPassword;

    @JsonView({UserView.RegistrationPost.class,UserView.UserPut.class})
    private String fullName;

    @JsonView({UserView.RegistrationPost.class,UserView.UserPut.class})
    private String phoneNumber;

    @CPF(groups = {UserView.RegistrationPost.class,UserView.PasswordPut.class})
    @JsonView({UserView.RegistrationPost.class,UserView.UserPut.class})
    private String cpf;

    @NotBlank(groups = {UserView.ImagePut.class})
    @JsonView({UserView.ImagePut.class})
    private String imageUrl;
}
