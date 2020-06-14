package com.epam.lab.repository;

import com.epam.lab.exception.UserNotFoundException;
import com.epam.lab.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.text.MessageFormat;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private static final String USER_NOT_FOUND = "User with ID = {0} is not found.";
    private static final String LOGIN = "login";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public long insert(User entity) {
        entityManager.persist(entity);
        return entity.getId();
    }

    @Override
    public User findById(long id) {
        User user = entityManager.find(User.class, id);
        if (user == null) {
            throw new UserNotFoundException(MessageFormat.format(USER_NOT_FOUND, id));
        }
        return user;
    }

    @Override
    public List<User> findAll(int from, int howMany) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = criteriaQuery.from(User.class);
        criteriaQuery.select(userRoot);
        criteriaQuery.orderBy(criteriaBuilder.asc(userRoot.get("id")));
        TypedQuery<User> query = entityManager.createQuery(criteriaQuery);
        return query
                .setFirstResult(from)
                .setMaxResults(howMany)
                .getResultList();
    }

    @Override
    public void update(User entity) {
        findById(entity.getId());
        entityManager.merge(entity);
    }

    @Override
    public void delete(long id) {
        entityManager.remove(findById(id));
    }

    @Override
    public User findByLogin(String login) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
            Root<User> userRoot = criteriaQuery.from(User.class);
            criteriaQuery.select(userRoot).where(criteriaBuilder.equal(userRoot.get(LOGIN), login));
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public long countAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<User> userRoot = criteriaQuery.from(User.class);
        criteriaQuery.select(criteriaBuilder.count(userRoot));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }
}
