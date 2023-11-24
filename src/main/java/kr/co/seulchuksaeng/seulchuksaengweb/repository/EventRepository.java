package kr.co.seulchuksaeng.seulchuksaengweb.repository;

import jakarta.persistence.EntityManager;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.Event;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.Gender;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.event.NoEventException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class EventRepository {

    private final EntityManager entityManager;

    public void save(Event event) { entityManager.persist(event);}

    public void delete(Event event) { entityManager.remove(event);}

    public List<Event> findEventList() {
        return entityManager.createQuery("select e from Event e order by e.startTime desc", Event.class)
                .setMaxResults(4)
                .getResultList();
    }

    public List<Event> findEventList(Gender gender) {
        return entityManager.createQuery("select e from Event e where e.gender = :gender order by e.startTime desc", Event.class)
                .setParameter("gender", gender)
                .getResultList();
    }

    public Event findEventById(Long eventId) {
        Event event = entityManager.find(Event.class, eventId);
        if (event == null) {
            throw new NoEventException();
        }
        return event;
    }

}
