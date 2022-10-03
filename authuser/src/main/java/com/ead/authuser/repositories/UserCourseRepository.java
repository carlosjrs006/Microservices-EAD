package com.ead.authuser.repositories;

import com.ead.authuser.models.UserCourserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourserModel, UUID>, JpaSpecificationExecutor<UserCourserModel> {
}
