package org.tkit.onecx.announcement.domain.daos;

import java.util.ArrayList;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.*;

import org.tkit.onecx.announcement.domain.criteria.AnnouncementSearchCriteria;
import org.tkit.onecx.announcement.domain.models.Announcement;
import org.tkit.onecx.announcement.domain.models.Announcement_;
import org.tkit.quarkus.jpa.daos.AbstractDAO;
import org.tkit.quarkus.jpa.daos.Page;
import org.tkit.quarkus.jpa.daos.PageResult;
import org.tkit.quarkus.jpa.exceptions.DAOException;
import org.tkit.quarkus.jpa.models.AbstractTraceableEntity_;
import org.tkit.quarkus.jpa.models.TraceableEntity_;
import org.tkit.quarkus.jpa.utils.QueryCriteriaUtil;

import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class AnnouncementDAO extends AbstractDAO<Announcement> {

    // https://hibernate.atlassian.net/browse/HHH-16830#icft=HHH-16830
    @Override
    public Announcement findById(Object id) throws DAOException {
        try {
            var cb = this.getEntityManager().getCriteriaBuilder();
            var cq = cb.createQuery(Announcement.class);
            var root = cq.from(Announcement.class);
            cq.where(cb.equal(root.get(TraceableEntity_.ID), id));
            return this.getEntityManager().createQuery(cq).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        } catch (Exception e) {
            throw new DAOException(ErrorKeys.FIND_ENTITY_BY_ID_FAILED, e, entityName, id);
        }
    }

    public PageResult<Announcement> loadAnnouncementByCriteria(AnnouncementSearchCriteria criteria) {
        try {
            CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Announcement> cq = cb.createQuery(Announcement.class);
            Root<Announcement> root = cq.from(Announcement.class);
            cq.select(root).distinct(true);

            List<Predicate> predicates = new ArrayList<>();
            if (criteria.getStatus() != null) {
                predicates.add(root.get(Announcement_.STATUS).in(criteria.getStatus()));
            }
            if (criteria.getAppId() != null) {
                predicates.add(cb.equal(root.get(Announcement_.APP_ID), criteria.getAppId()));
            }
            if (criteria.getWorkspaceName() != null) {
                predicates.add(cb.equal(root.get(Announcement_.WORKSPACE_NAME), criteria.getWorkspaceName()));
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
            QueryCriteriaUtil.addSearchStringPredicate(predicates, cb, root.get(Announcement_.TITLE), criteria.getTitle());

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
            CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<String> cq = cb.createQuery(String.class);
            Root<Announcement> root = cq.from(Announcement.class);
            cq.select(root.get(Announcement_.APP_ID)).distinct(true);
            cq.where(root.get(Announcement_.APP_ID).isNotNull());
            return getEntityManager().createQuery(cq).getResultList();
        } catch (Exception ex) {
            throw new DAOException(ErrorKeys.ERROR_FIND_APPLICATIONS_WITH_ANNOUNCEMENTS, ex);
        }
    }

    public List<String> findWorkspacesWithAnnouncements() {
        try {
            CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<String> cq = cb.createQuery(String.class);
            Root<Announcement> root = cq.from(Announcement.class);
            cq.select(root.get(Announcement_.WORKSPACE_NAME)).distinct(true);
            cq.where(root.get(Announcement_.WORKSPACE_NAME).isNotNull());
            return getEntityManager().createQuery(cq).getResultList();
        } catch (Exception ex) {
            throw new DAOException(ErrorKeys.ERROR_FIND_WORKSPACES_WITH_ANNOUNCEMENTS, ex);
        }
    }

    public enum ErrorKeys {

        FIND_ENTITY_BY_ID_FAILED,
        ERROR_LOAD_ANNOUNCEMENT_BY_CRITERIA,
        ERROR_FIND_APPLICATIONS_WITH_ANNOUNCEMENTS,
        ERROR_FIND_WORKSPACES_WITH_ANNOUNCEMENTS
    }

}
