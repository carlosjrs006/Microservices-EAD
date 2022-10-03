package com.ead.authuser.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) //mostra somente campos com valores
@Entity
@Table(name = "TB_USUERS_COURSES")
public class UserCourserModel  implements Serializable {

    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private UserModel user;

    @Column(nullable = false)
    private UUID courseId;


}
