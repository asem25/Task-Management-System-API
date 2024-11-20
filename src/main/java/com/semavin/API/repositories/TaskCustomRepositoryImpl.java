package com.semavin.API.repositories;

import com.semavin.API.models.Task;
import com.semavin.API.models.TaskPriority;
import com.semavin.API.models.TaskStatus;
import com.semavin.API.models.User;
import com.semavin.API.repositories.TaskCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.*;
@Repository
public class TaskCustomRepositoryImpl implements TaskCustomRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Task> findWithFilters(String status, String priority, Long authorId, Long assigneeId, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> query = cb.createQuery(Task.class);
        Root<Task> task = query.from(Task.class);

        List<Predicate> predicates = new ArrayList<>();

        // Используем Join для связи с TaskStatus
        if (status != null) {
            Join<Task, TaskStatus> taskStatusJoin = task.join("taskStatus");
            predicates.add(cb.equal(taskStatusJoin.get("name"), status));
        }

        // Используем Join для связи с TaskPriority
        if (priority != null) {
            Join<Task, TaskPriority> taskPriorityJoin = task.join("taskPriority");
            predicates.add(cb.equal(taskPriorityJoin.get("name"), priority));
        }

        // Фильтрация по автору
        if (authorId != null) {
            Join<Task, User> authorJoin = task.join("author");
            predicates.add(cb.equal(authorJoin.get("id"), authorId));
        }

        // Фильтрация по исполнителю
        if (assigneeId != null) {
            Join<Task, User> assigneeJoin = task.join("assignee");
            predicates.add(cb.equal(assigneeJoin.get("id"), assigneeId));
        }

        query.where(cb.and(predicates.toArray(new Predicate[0])));

        // Для подсчета общего числа элементов
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Task> countRoot = countQuery.from(Task.class);
        countQuery.select(cb.count(countRoot));
        countQuery.where(cb.and(predicates.toArray(new Predicate[0])));
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        // Выполняем запрос с пагинацией
        List<Task> tasks = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(tasks, pageable, count);
    }

}
