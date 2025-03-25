package epam.com.task_rest.service;


import epam.com.task_rest.dto.training_type.TrainingTypeDto;
import epam.com.task_rest.entity.TrainingType;
import epam.com.task_rest.exception.ResourceNotFoundException;
import epam.com.task_rest.mapper.TrainingTypeMapper;
import epam.com.task_rest.repository.TrainingTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingTypeService {
    private final TrainingTypeRepository trainingTypeRepository;
    private final TrainingTypeMapper trainingTypeMapper;

    public TrainingTypeService(TrainingTypeRepository trainingTypeRepository, TrainingTypeMapper trainingTypeMapper) {
        this.trainingTypeRepository = trainingTypeRepository;
        this.trainingTypeMapper = trainingTypeMapper;
    }

    public TrainingType getTrainingType(Integer id) {
        return trainingTypeRepository.findTrainingTypeById(id).orElseThrow(
                () -> new ResourceNotFoundException("Training type not found"));
    }

    public List<TrainingTypeDto> getAllTrainingTypes() {
        List<TrainingType> allTrainingTypes = trainingTypeRepository.findAll();
        return trainingTypeMapper.toDtoList(allTrainingTypes);
    }
}
