package akvelon.zuora.denysenko.persistence.hiber;

import akvelon.zuora.denysenko.persistence.Repository;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @param <T> Specific Entity Class inheritor
 * @author Denysenko Stanislav
 */
@Slf4j
public class RepositoryImpl<T> implements Repository<T> {

    protected final EntityManagerFactory entityManagerFactory;

    private final Class<T> clazz;

    public RepositoryImpl(EntityManagerFactory entityManagerFactory, Class<T> clazz) {
        this.entityManagerFactory = entityManagerFactory;
        this.clazz = clazz;
    }

    @Override
    public T create(T entity) {
        var entityManager = entityManagerFactory.createEntityManager();
        var transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(entity);
            transaction.commit();
            if (entityManager.contains(entity)) {
                log.debug("create, entity created: {}", entity);
            }
            return entity;
        } catch (EntityExistsException exception) {
            log.debug("create, entity already exist: %{}, {} ", entity, exception.getMessage());
            transaction.rollback();
            throw exception;
        } catch (IllegalArgumentException exception) {
            log.debug("create, entity: {}, {}", entity, exception.getMessage());
            transaction.rollback();
            throw  exception;
        } catch (PersistenceException exception) {
            log.debug("create, unable to create Entity: {}, {}", entity, exception.getMessage());
            transaction.rollback();
            throw  exception;
        } finally {
            entityManager.close();
        }
//        log.debug("create, entity wasn't created: {}", entity);
//        return null;
    }

    @Override
    public T update(T entity) {
        var entityManager = entityManagerFactory.createEntityManager();
        var transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            T tResponse = entityManager.merge(entity);
            transaction.commit();
            log.debug("update, Entity updated: {}", entity);
            return tResponse;
        } catch (TransactionRequiredException | IllegalArgumentException exception) {
            log.debug("update, unable to update Entity: {}, {}", entity, exception.getMessage());
            transaction.rollback();
        } finally {
            entityManager.close();
        }
        log.debug("update, nothing updated");
        return null;
    }

    @Override
    public T getById(long id) {
        var entityManager = entityManagerFactory.createEntityManager();
        var transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            T entity = entityManager.find(clazz, id);
            transaction.commit();
            log.debug("getById, get entity: {}", entity);
            return entity;
        } catch (IllegalArgumentException exception) {
            log.debug("Unable to find entity by id={}, {}", id, exception.getMessage());
            transaction.rollback();
        } finally {
            entityManager.close();
        }
        log.debug("Can't getById");
        return null;
    }

    @Override
    public T deleteById(long id) {
        var entityManager = entityManagerFactory.createEntityManager();
        T entity = entityManager.find(clazz, id);
        var transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.remove(entity);
            transaction.commit();
            log.debug("deleteById, entity deleted: {}", entity);
            return entity;
        } catch (IllegalArgumentException | TransactionRequiredException exception) {
            log.debug("deleteById, unable to delete Entity by id={}, {}", id, exception.getMessage());
            transaction.rollback();
        } finally {
            entityManager.close();
        }
        log.debug("deleteById, wasn't deleteById ");
        return null;
    }

    @Override
    public List<T> getList(String sort, String order) {
        var entityManager = entityManagerFactory.createEntityManager();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);

        Root<T> rootT = criteriaQuery.from(clazz);
        Path<String> path = rootT.get(sort);
        criteriaQuery.select(rootT);
        if ("asc".equalsIgnoreCase(order)) {
            criteriaQuery.orderBy(criteriaBuilder.asc(path));
        } else if ("desc".equalsIgnoreCase(order)) {
            criteriaQuery.orderBy(criteriaBuilder.desc(path));
        }
        TypedQuery<T> queryGetEntities = entityManager.createQuery(criteriaQuery);

        var transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            List<T> list = queryGetEntities.getResultList();
            transaction.commit();
            if (!Objects.isNull(list)) {
                log.debug("getList, response list: {} ", list);
                return list;
            }
        } catch (PersistenceException | IllegalStateException exception) {
            log.debug("getList, unable to get Entities with parameters: sort={}, order={} {}", sort, order, exception.getMessage());
            transaction.rollback();
        } finally {
            entityManager.close();
        }
        log.debug("getList, the list is null. with parameters: sort={}, order={}, entityClass={}", sort, order, clazz);
        return Collections.emptyList();
    }

    @Override
    public T getByName(String name) {
        var entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> rootT = criteriaQuery.from(clazz);

        Predicate namePredicate = criteriaBuilder.equal(rootT.get("name"), name);
        criteriaQuery.where(namePredicate);

        TypedQuery<T> queryGetEntities = entityManager.createQuery(criteriaQuery);

        var transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            T entity = queryGetEntities.getSingleResult();
            transaction.commit();
            if (!Objects.isNull(entity)) {
                log.debug("getByName, response: {} ", entity);
                return entity;
            }
        } catch (PersistenceException | IllegalStateException exception) {
            log.debug("getByName, can't get entity: {} {}", name, exception.getMessage());
            transaction.rollback();
        } finally {
            entityManager.close();
        }
        log.debug("getByName, null:");
        return null;
    }

}
