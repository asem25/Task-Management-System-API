package com.semavin.API.repositories;

import com.semavin.API.models.Comment;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(exported = false)
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByTaskId(Long id);
}
