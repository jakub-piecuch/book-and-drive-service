package org.redcode.bookanddriveservice.lessons.repository;

import static java.util.Objects.nonNull;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redcode.bookanddriveservice.lessons.model.LessonEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LessonCustomSearchRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    public List<LessonEntity> findAllByCriteria(LessonSearchCriteria criteria, PageRequest pageRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<LessonEntity> criteriaQuery = criteriaBuilder.createQuery(LessonEntity.class);
        Root<LessonEntity> root = criteriaQuery.from(LessonEntity.class);

        List<Predicate> predicates = buildPredicates(criteria, criteriaBuilder, root);

        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

        TypedQuery<LessonEntity> query = entityManager.createQuery(criteriaQuery);
        if (pageRequest.getPageNumber() == 0) {
            query.setFirstResult(pageRequest.getPageNumber());
        } else {
            query.setFirstResult(pageRequest.getPageNumber() + pageRequest.getPageSize() - 1);
        }

        query.setMaxResults(pageRequest.getPageSize());

        return query.getResultList();
    }

    public long getTotalCount(LessonSearchCriteria criteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<LessonEntity> root = countQuery.from(LessonEntity.class);

        List<Predicate> predicates = buildPredicates(criteria, criteriaBuilder, root);

        countQuery.select(criteriaBuilder.count(root))
            .where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(countQuery).getSingleResult();
    }

    private List<Predicate> buildPredicates(LessonSearchCriteria criteria, CriteriaBuilder criteriaBuilder, Root<LessonEntity> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (nonNull(criteria.startTime())) {
            Predicate startTimePredicate = criteriaBuilder.greaterThan(root.get("startDateTime"), criteria.startTime());
            predicates.add(startTimePredicate);
        }

        if (nonNull(criteria.endTime())) {
            Predicate endTimePredicate = criteriaBuilder.lessThan(root.get("endDateTime"), criteria.endTime());
            predicates.add(endTimePredicate);
        }

        if (nonNull(criteria.instructorId())) {
            Predicate instuctorIdPredicate = criteriaBuilder.equal(root.get("instructorId"), criteria.instructorId());
            predicates.add(instuctorIdPredicate);
        }

        if (nonNull(criteria.traineeId())) {
            Predicate traineeIdPredicate = criteriaBuilder.equal(root.get("traineeId"), criteria.traineeId());
            predicates.add(traineeIdPredicate);
        }

        if (nonNull(criteria.carId())) {
            Predicate carIdPredicate = criteriaBuilder.equal(root.get("carId"), criteria.carId());
            predicates.add(carIdPredicate);
        }

        return predicates;
    }
}
