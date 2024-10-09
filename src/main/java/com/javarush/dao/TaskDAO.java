package com.javarush.dao;

import com.javarush.domain.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class TaskDAO {

    private final SessionFactory sessionFactory;

    public TaskDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<Task> getAllTasks(int offset, int limit) {
        Query<Task> query = getSession().createQuery("SELECT t FROM Task t", Task.class);

        query.setFirstResult(offset);
        query.setMaxResults(limit);

        return query.getResultList();
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public int getCount(){
        Query<Long> query = getSession().createQuery("SELECT COUNT(t) FROM Task t", Long.class);

        return query.getSingleResult().intValue();
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Task getTask(int id) {
        Query<Task> query = getSession().createQuery("SELECT t FROM Task t WHERE t.id = :id", Task.class);

        query.setParameter("id", id);

        return query.uniqueResult();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveOrUpdate(Task task) {
        getSession().persist(task);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteTask(Task task) {
        getSession().remove(task);
    }

    private Session getSession() {

        return sessionFactory.getCurrentSession();
    }
}
