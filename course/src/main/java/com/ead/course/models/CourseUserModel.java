package com.ead.course.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Table(name = "TB_COURSES_USERS")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL) //mostra somente campos com valores
@Entity
public class CourseUserModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private CourseModel course;

    @Column(nullable = false)
    private UUID userId;

}
