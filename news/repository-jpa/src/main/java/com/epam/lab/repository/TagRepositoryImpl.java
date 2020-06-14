package com.epam.lab.repository;

import com.epam.lab.exception.TagNotFoundException;
import com.epam.lab.model.Tag;
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
public class TagRepositoryImpl implements TagRepository {

    private static final String TAG_NOT_FOUND = "Tag with ID = {0} is not found.";
    private static final String NAME = "name";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public long insert(Tag entity) {
        entityManager.persist(entity);
        return entity.getId();
    }

    @Override
    public Tag findById(long id) {
        Tag tag = entityManager.find(Tag.class, id);
        if (tag == null) {
            throw new TagNotFoundException(MessageFormat.format(TAG_NOT_FOUND, id));
        }
        return tag;
    }

    @Override
    public Tag findByName(String tagName) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
            Root<Tag> tagRoot = criteriaQuery.from(Tag.class);
            criteriaQuery.select(tagRoot).where(criteriaBuilder.equal(tagRoot.get(NAME), tagName));
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Tag> findAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> tagRoot = criteriaQuery.from(Tag.class);
        criteriaQuery.select(tagRoot);
        TypedQuery<Tag> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    @Override
    public void update(Tag entity) {
        findById(entity.getId());
        entityManager.merge(entity);
    }

    @Override
    public void delete(long id) {
        entityManager.remove(findById(id));
    }

}
