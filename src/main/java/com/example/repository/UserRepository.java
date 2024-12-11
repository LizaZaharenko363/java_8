package com.example.repository;

import com.example.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserRepository {
    @PersistenceContext(unitName = "MySqlPU")
    private EntityManager entityManager;

    @Transactional
    public User create(User user) {
        entityManager.persist(user);
        return user;
    }

    public User findById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Transactional
    public User update(User user) {
        return entityManager.merge(user);
    }

    @Transactional
    public void delete(Long id) {
        User user = findById(id);
        if (user != null) {
            entityManager.remove(entityManager.contains(user) ? user : entityManager.merge(user));
        }
    }

    public List<User> findByEmail(String email) {
        return entityManager.createNamedQuery("User.findByEmail", User.class)
                .setParameter("email", email)
                .getResultList();
    }

    public List<User> findActiveUsers() {
        return entityManager.createNamedQuery("User.findActiveUsers", User.class)
                .getResultList();
    }

    // JPQL
    public List<User> findUsersByRegistrationDateRange(LocalDate startDate, LocalDate endDate) {
        return entityManager.createQuery(
                        "SELECT u FROM User u WHERE u.registrationDate BETWEEN :startDate AND :endDate", User.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }

    // JPQL
    public long countActiveUsers() {
        return entityManager.createQuery(
                        "SELECT COUNT(u) FROM User u WHERE u.active = true", Long.class)
                .getSingleResult();
    }

    public List<User> findUsersWithNameContaining(String namePart) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.name LIKE :namePart", User.class);
        query.setParameter("namePart", "%" + namePart + "%");
        return query.getResultList();
    }

    public Optional<User> findOldestUser() {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u ORDER BY u.registrationDate ASC", User.class);
        query.setMaxResults(1);
        query.setFirstResult(0);
        List<User> result = query.getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }
}