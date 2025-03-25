package epam.com.task_rest.repository;

import epam.com.task_rest.entity.Training;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public class TrainingRepository {
    private final EntityManager entityManager;

    public TrainingRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Training save(Training training) {
        entityManager.persist(training);
        return training;
    }

    public List<Training> getTraineeTrainingsListByTraineeUsernameAndCriteria(String traineeUsername, Date fromDate, Date toDate, String trainerName, Integer trainingTypeId) {
        String sql = "select t from Training t where t.trainee.user.userName = :traineeUsername ";
        if (fromDate != null) {
            sql = sql + " and t.trainingDate >= :fromDate ";
        }
        if (toDate != null) {
            sql = sql + " and t.trainingDate <= :toDate";
        }
        if (trainerName != null) {
            sql = sql + " and t.trainer.user.firstName = :trainerName";
        }
        if (trainingTypeId != null) {
            sql = sql + " and t.trainingType.id = :trainingTypeId";
        }
        TypedQuery<Training> query = entityManager.createQuery(sql, Training.class);
        query.setParameter("traineeUsername", traineeUsername);
        if (fromDate != null) {
            query.setParameter("fromDate", fromDate);
        }
        if (toDate != null) {
            query.setParameter("toDate", toDate);
        }
        if (trainerName != null) {
            query.setParameter("trainerName", trainerName);
        }
        if (trainingTypeId != null) {
            query.setParameter("trainingTypeId", trainingTypeId);
        }
        List<Training> trainings = query.getResultList();
        return trainings;
    }

    public List<Training> getTrainerTrainingsListByTrainerUsernameAndCriteria(String trainerUsername, Date fromDate, Date toDate, String traineeName) {
        String sql = "select t from Training t where t.trainer.user.userName = :trainerUsername";
        if (fromDate != null) {
            sql = sql + " and t.trainingDate >= :fromDate ";
        }
        if (toDate != null) {
            sql = sql + " and t.trainingDate <= :toDate ";
        }
        if (traineeName != null) {
            sql = sql + " and t.trainee.user.firstName = :traineeName";
        }
        TypedQuery<Training> query = entityManager.createQuery(sql, Training.class);
        query.setParameter("trainerUsername", trainerUsername);
        if (fromDate != null) {
            query.setParameter("fromDate", fromDate);
        }
        if (toDate != null) {
            query.setParameter("toDate", toDate);
        }
        if (traineeName != null) {
            query.setParameter("traineeName", traineeName);
        }
        List<Training> trainings = query.getResultList();
        return trainings;
    }
}
