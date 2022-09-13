package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserModel>> getAllUsers(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUsersById(@PathVariable("id") UUID userID){
        Optional<UserModel> userModelOptional = userService.findById(userID);
        if (userModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(userModelOptional.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") UUID userId){
        Optional<UserModel> userModelOptional = userService.findById(userId);
        if (userModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } else{
            userService.delete(userModelOptional.get());
        }
        return ResponseEntity.status(HttpStatus.OK).body("Usuario deletado com sucesso!!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") UUID userId,
                                        @RequestBody @JsonView(UserDto.UserView.UserPut.class)
                                        @Validated(UserDto.UserView.UserPut.class) UserDto userDto ){

        Optional<UserModel> userModelOptional = userService.findById(userId);
        if (userModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } else{
            var userModel = userModelOptional.get();
            userModel.setFullName(userDto.getFullName());
            userModel.setPhoneNumber(userDto.getPhoneNumber());
            userModel.setCpf(userDto.getCpf());
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userService.save(userModel);
        }
        return ResponseEntity.status(HttpStatus.OK).body("User updated successfully!!");
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<Object> updatePassword(@PathVariable("id") UUID userId,
                                        @RequestBody @JsonView(UserDto.UserView.PasswordPut.class)
                                        @Validated(UserDto.UserView.PasswordPut.class) UserDto userDto ){

        Optional<UserModel> userModelOptional = userService.findById(userId);
        if (userModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }if(!userModelOptional.get().getPassword().equals(userDto.getOldPassword())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Mismatched old password!");
        }else{
            var userModel = userModelOptional.get();
            userModel.setPassword(userDto.getPassword());
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userService.save(userModel);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Password updated successfully!!");
    }

    @PutMapping("/{id}/image")
    public ResponseEntity<Object> updateImage(@PathVariable("id") UUID userId,
                                                 @RequestBody @JsonView(UserDto.UserView.ImagePut.class)
                                                 @Validated(UserDto.UserView.ImagePut.class) UserDto userDto ){

        Optional<UserModel> userModelOptional = userService.findById(userId);
        if (userModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }else{
            var userModel = userModelOptional.get();
            userModel.setImageUrl(userDto.getImageUrl());
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userService.save(userModel);
            return ResponseEntity.status(HttpStatus.OK).body(userModel);
        }
    }
}
