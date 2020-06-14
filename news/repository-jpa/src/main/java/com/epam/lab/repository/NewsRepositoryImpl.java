package com.epam.lab.repository;

import com.epam.lab.exception.NewsNotFoundException;
import com.epam.lab.model.News;
import com.epam.lab.specification.SearchSpecification;
import com.epam.lab.specification.SortSpecification;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class NewsRepositoryImpl implements NewsRepository {

    private static final String NEWS_NOT_FOUND = "News with ID = {0} is not found.";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public long insert(News entity) {
        entityManager.persist(entity);
        return entity.getId();
    }

    @Override
    public News findById(long id) {
        News news = entityManager.find(News.class, id);
        if (news == null) {
            throw new NewsNotFoundException(MessageFormat.format(NEWS_NOT_FOUND, id));
        }
        return news;
    }

    @Override
    public List<News> find(SearchSpecification<News> searchSpecification, List<SortSpecification<News>> sortSpecifications,
                           int from, int howMany) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<News> criteriaQuery = criteriaBuilder.createQuery(News.class);
        Root<News> newsRoot = criteriaQuery.from(News.class);
        Predicate predicate = searchSpecification.toPredicate(newsRoot, criteriaQuery, criteriaBuilder);
        criteriaQuery.where(predicate);
        criteriaQuery.select(newsRoot);
        if (sortSpecsNotEmpty(sortSpecifications)) {
            criteriaQuery.orderBy(sortSpecifications
                    .stream()
                    .map(ss -> ss.toOrder(newsRoot, criteriaQuery, criteriaBuilder))
                    .collect(Collectors.toList()));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.asc(newsRoot.get("id")));
        }

        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(from)
                .setMaxResults(howMany)
                .getResultList();
    }

    @Override
    public List<News> findAll(int from, int howMany) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<News> criteriaQuery = criteriaBuilder.createQuery(News.class);
        Root<News> newsRoot = criteriaQuery.from(News.class);
        criteriaQuery.orderBy(criteriaBuilder.asc(newsRoot.get("id")));
        criteriaQuery.select(newsRoot);
        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(from)
                .setMaxResults(howMany)
                .getResultList();
    }

    @Override
    public void update(News entity) {
        findById(entity.getId());
        entityManager.merge(entity);
    }

    @Override
    public void delete(long id) {
        entityManager.remove(findById(id));
    }

    @Override
    public long countAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<News> newsRoot = criteriaQuery.from(News.class);
        criteriaQuery.select(criteriaBuilder.count(newsRoot));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    public long countWithSpecification(SearchSpecification<News> searchSpecification) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<News> newsRoot = criteriaQuery.from(News.class);
        Predicate predicate = searchSpecification.toPredicate(newsRoot, criteriaQuery, criteriaBuilder);
        criteriaQuery.where(predicate);
        criteriaQuery.select(criteriaBuilder.count(newsRoot));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

}
