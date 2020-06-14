package com.epam.lab.repository;

import com.epam.lab.exception.AuthorNotFoundException;
import com.epam.lab.model.Author;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.text.MessageFormat;
import java.util.List;

@Repository
public class AuthorRepositoryImpl implements AuthorRepository {

    private static final String AUTHOR_NOT_FOUND = "Author with ID = {0} is not found.";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public long insert(Author entity) {
        entityManager.persist(entity);
        return entity.getId();
    }

    @Override
    public Author findById(long id) {
        Author author = entityManager.find(Author.class, id);
        if (author == null) {
            throw new AuthorNotFoundException(MessageFormat.format(AUTHOR_NOT_FOUND, id));
        }
        return author;
    }

    @Override
    public List<Author> findAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Author> criteriaQuery = criteriaBuilder.createQuery(Author.class);
        Root<Author> authorRoot = criteriaQuery.from(Author.class);
        criteriaQuery.select(authorRoot);
        TypedQuery<Author> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    @Override
    public void update(Author entity) {
        findById(entity.getId());
        entityManager.merge(entity);
    }

    @Override
    public void delete(long id) {
        entityManager.remove(findById(id));
    }

}
