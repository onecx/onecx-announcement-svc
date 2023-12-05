package org.onecx.announcement.domain.dao;

import java.util.ArrayList;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;

import org.onecx.announcement.domain.models.Announcement;
import org.onecx.announcement.domain.models.Announcement_;
import org.tkit.quarkus.jpa.daos.AbstractDAO;
import org.tkit.quarkus.jpa.daos.Page;
import org.tkit.quarkus.jpa.daos.PageResult;
import org.tkit.quarkus.jpa.exceptions.DAOException;
import org.tkit.quarkus.jpa.models.AbstractTraceableEntity_;

import gen.io.github.onecx.announcement.v1.model.SearchAnnouncementRequestDTOV1;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class AnnouncementDAO extends AbstractDAO<Announcement> {

    public PageResult<Announcement> loadAnnouncementByCriteria(SearchAnnouncementRequestDTOV1 criteria) {
        try {
            CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Announcement> cq = cb.createQuery(Announcement.class);
            Root<Announcement> root = cq.from(Announcement.class);
            cq.select(root).distinct(true);

            List<Predicate> predicates = new ArrayList<>();
            if (criteria != null) {
                if (criteria.getStatus() != null) {
                    predicates.add(root.get(Announcement_.STATUS).in(criteria.getStatus()));
                }
                if (criteria.getAppId() != null) {
                    Predicate predicateEqualAppId = cb.equal(root.get(Announcement_.APP_ID), criteria.getAppId());
                    Predicate predicateIsNull = cb.isNull(root.get(Announcement_.APP_ID));
                    predicates.add(cb.or(predicateEqualAppId, predicateIsNull));
                }
                if (criteria.getStartDateFrom() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get(Announcement_.START_DATE),
                            criteria.getStartDateFrom().toLocalDateTime()));
                }
                if (criteria.getStartDateTo() != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get(Announcement_.START_DATE),
                            criteria.getStartDateTo().toLocalDateTime()));
                }
                if (criteria.getEndDateFrom() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get(Announcement_.END_DATE),
                            criteria.getEndDateFrom().toLocalDateTime()));
                }
                if (criteria.getEndDateTo() != null) {
                    predicates.add(
                            cb.lessThanOrEqualTo(root.get(Announcement_.END_DATE), criteria.getEndDateTo().toLocalDateTime()));
                }
                if (criteria.getPriority() != null) {
                    predicates.add(root.get(Announcement_.PRIORITY).in(criteria.getPriority()));

                }
                if (criteria.getType() != null) {
                    predicates.add(root.get(Announcement_.TYPE).in(criteria.getType()));

                }
                if (criteria.getId() != null) {
                    predicates.add(root.get(Announcement_.ID).in(criteria.getId()));
                }
            }
            if (!predicates.isEmpty()) {
                cq.where(cb.and(predicates.toArray(new Predicate[0])));
            }

            //do query and sort resultList by Priority and creation-date
            cq.orderBy(cb.desc(root.get(AbstractTraceableEntity_.CREATION_DATE)));
            return createPageQuery(cq, Page.of(criteria.getPageNumber(), criteria.getPageSize())).getPageResult();
        } catch (Exception ex) {
            throw new DAOException(ErrorKeys.ERROR_LOAD_ANNOUNCEMENT_BY_CRITERIA, ex);
        }

    }

    public List<String> findApplicationsWithAnnouncements() {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Tuple> cq = cb.createTupleQuery();
            Root<Announcement> root = cq.from(Announcement.class);
            cq.multiselect(root.get(Announcement_.APP_ID));
            cq.distinct(true);
            List<Tuple> tupleResult = em.createQuery(cq).getResultList();
            return tupleResult.stream().map(t -> (String) t.get(0)).toList();
        } catch (Exception ex) {
            throw new DAOException(ErrorKeys.ERROR_FIND_APPLICATIONS_WITH_ANNOUNCEMENTS, ex);
        }
    }

    public void deleteByAppId(String appId) {
        try {
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaDelete<Announcement> criteriaDelete = criteriaBuilder.createCriteriaDelete(Announcement.class);
            Root<Announcement> root = criteriaDelete.from(Announcement.class);
            criteriaDelete.where(criteriaBuilder.equal(root.get(Announcement_.APP_ID), appId));
            em.getTransaction().begin();
            int rowsDeleted = em.createQuery(criteriaDelete).executeUpdate();
            log.info("Number of Announcements deleted: " + rowsDeleted);
            em.getTransaction().commit();
        } catch (Exception ex) {
            throw new DAOException(ErrorKeys.ERROR_DELETE_BY_CRITERIA, ex);
        }
    }

    public enum ErrorKeys {

        ERROR_LOAD_ANNOUNCEMENT_BY_CRITERIA,
        ERROR_FIND_APPLICATIONS_WITH_ANNOUNCEMENTS,
        ERROR_DELETE_BY_CRITERIA
    }

}
