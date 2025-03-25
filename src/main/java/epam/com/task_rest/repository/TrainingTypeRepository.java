package epam.com.task_rest.repository;

import epam.com.task_rest.entity.TrainingType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingTypeRepository extends JpaRepository<TrainingType, Integer> {
    Optional<TrainingType> findTrainingTypeById(Integer id);

}
