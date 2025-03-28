package epam.com.task_rest.service;

import epam.com.task_rest.dto.ChangeLoginDto;
import epam.com.task_rest.dto.LoginRequestDto;
import epam.com.task_rest.dto.trainee.TraineeCreateDto;
import epam.com.task_rest.dto.trainee.TraineeDto;
import epam.com.task_rest.dto.trainee.TraineeUpdateDto;
import epam.com.task_rest.dto.training.TrainingDto;
import epam.com.task_rest.entity.Trainee;
import epam.com.task_rest.entity.Training;
import epam.com.task_rest.entity.User;
import epam.com.task_rest.exception.AuthenticationException;
import epam.com.task_rest.exception.ResourceNotFoundException;
import epam.com.task_rest.mapper.TraineeMapper;
import epam.com.task_rest.mapper.TrainerMapper;
import epam.com.task_rest.mapper.TrainingMapper;
import epam.com.task_rest.repository.TraineeRepository;
import epam.com.task_rest.repository.TrainerRepository;
import epam.com.task_rest.repository.TrainingRepository;
import epam.com.task_rest.util.UsernamePasswordGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {
    @InjectMocks
    private TraineeService traineeService;
    @Mock
    private TraineeRepository traineeRepository;
    @Mock
    private TraineeMapper traineeMapper;
    @Mock
    private TrainerMapper trainerMapper;
    @Mock
    private UsernamePasswordGenerator usernamePasswordGenerator;
    @Mock
    private TrainingRepository trainingRepository;
    @Mock
    private TrainingMapper trainingMapper;
    @Mock
    private TrainerRepository trainerRepository;

    @Test
    void create() {
        TraineeCreateDto traineeCreateDto = new TraineeCreateDto("Ali", "Valiyev",
                                                    new Date(2025, 3, 27), "Toshkent");
        Trainee trainee = new Trainee();
        Mockito.when(traineeMapper.toEntity(traineeCreateDto)).thenReturn(trainee);

        Mockito.when(usernamePasswordGenerator.generateUsername(traineeCreateDto.firstName(), traineeCreateDto.lastName()))
                                                    .thenReturn("Ali.Valiyev");
        Mockito.when(usernamePasswordGenerator.generatePassword()).thenReturn("777");
       // Mockito.doNothing().when(traineeRepository).save(trainee);

        traineeService.create(traineeCreateDto);

        Mockito.verify(traineeMapper).toEntity(traineeCreateDto);
        Mockito.verify(usernamePasswordGenerator).generateUsername(traineeCreateDto.firstName(), traineeCreateDto.lastName());
        Mockito.verify(usernamePasswordGenerator).generatePassword();
     //   Mockito.verify(traineeRepository).save(trainee);
    }

    @Test
    void login_Failure() {
        LoginRequestDto loginRequestDto = new LoginRequestDto("Ali.Valiyev", "777");
        Mockito.when(traineeRepository.checkUsernameAndPasswordMatch(loginRequestDto.username(), loginRequestDto.password()))
                .thenReturn(false);

        AuthenticationException exception = Assertions.assertThrows(AuthenticationException.class,
                () -> traineeService.login(loginRequestDto));
        Assertions.assertEquals("Username or password is incorrect", exception.getMessage());
        Mockito.verify(traineeRepository, Mockito.times(1)).checkUsernameAndPasswordMatch(loginRequestDto.username(), loginRequestDto.password());
    }

    @Test
    void login_Success(){
        LoginRequestDto loginRequestDto = new LoginRequestDto("Ali.Valiyev", "777");
        Mockito.when(traineeRepository.checkUsernameAndPasswordMatch(loginRequestDto.username(), loginRequestDto.password()))
                .thenReturn(true);

        Assertions.assertDoesNotThrow(() -> traineeService.login(loginRequestDto));
        Mockito.verify(traineeRepository).checkUsernameAndPasswordMatch(loginRequestDto.username(), loginRequestDto.password());
    }

    @Test
    void getTraineeByUsername_Success() {
        String username = "Ali.Valiyev";
        Trainee trainee = new Trainee();
        User user = new User();
        user.setUserName(username);
        trainee.setUser(user);
        trainee.setId(1);

        TraineeDto traineeDto = new TraineeDto("Ali", "Valiyev", true, new Date(), "Navai", null);

        Mockito.when(traineeRepository.findTraineeByUser_UserName(username))
                .thenReturn(Optional.of(trainee));
        Mockito.when(traineeMapper.toDto(trainee)).thenReturn(traineeDto);

        TraineeDto result = traineeService.getTraineeByUsername(username);

        Assertions.assertEquals(traineeDto, result);
        Mockito.verify(traineeRepository).findTraineeByUser_UserName(username);
        Mockito.verify(traineeMapper).toDto(trainee);
    }

    @Test
    void getTraineeByUsername_ThrowsException() {
        String username = "Ali.Valiyev";
        Mockito.when(traineeRepository.findTraineeByUser_UserName(username)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> traineeService.getTraineeByUsername(username));
        Assertions.assertEquals("Trainee not found", exception.getMessage());
        Mockito.verify(traineeMapper, Mockito.never()).toDto(Mockito.any());
    }

    @Test
    void changePassword_Success() {
        ChangeLoginDto dto = new ChangeLoginDto("Ali.Valiyev", "123", "111");
        Mockito.when(traineeRepository.checkUsernameAndPasswordMatch(dto.username(), dto.oldPassword())).thenReturn(true);
        Mockito.doNothing().when(traineeRepository).changePassword(dto.username(), dto.newPassword());

        traineeService.changePassword(dto);

        Mockito.verify(traineeRepository).checkUsernameAndPasswordMatch(dto.username(), dto.oldPassword());
        Mockito.verify(traineeRepository).changePassword(dto.username(), dto.newPassword());
    }

    @Test
    void changePassword_ThrowsException() {
        ChangeLoginDto changeLoginDto = new ChangeLoginDto("Ali.Valiyev", "123", "111");
        Mockito.when(traineeRepository.checkUsernameAndPasswordMatch(changeLoginDto.username(), changeLoginDto.oldPassword())).thenReturn(false);

        Assertions.assertThrows(AuthenticationException.class, () -> traineeService.changePassword(changeLoginDto));

        Mockito.verify(traineeRepository).checkUsernameAndPasswordMatch(changeLoginDto.username(), changeLoginDto.oldPassword());
        Mockito.verify(traineeRepository, Mockito.never()).changePassword(changeLoginDto.username(), changeLoginDto.newPassword());
    }

    @Test
    void update_Success() {
        String username = "Ali";
        TraineeUpdateDto dto = new TraineeUpdateDto("Ali", "Valiyev", true,
                new Date(), "Navai");
        Trainee trainee = new Trainee();
        TraineeDto traineeDto = new TraineeDto("Ali", "Valiyev", true, new Date(), "Navai", null);

        Mockito.when(traineeRepository.findTraineeByUser_UserName(username)).thenReturn(Optional.of(trainee));
        Mockito.when(traineeMapper.partialUpdate(dto, trainee)).thenReturn(trainee);
        Mockito.when(traineeRepository.save(trainee)).thenReturn(trainee);
        Mockito.when(traineeMapper.toDto(trainee)).thenReturn(traineeDto);

        traineeService.update(username, dto);

        Mockito.verify(traineeRepository).findTraineeByUser_UserName(username);
        Mockito.verify(traineeMapper).partialUpdate(dto, trainee);
        Mockito.verify(traineeRepository).save(trainee);
        Mockito.verify(traineeMapper).toDto(trainee);
    }

    @Test
    void update_ThrowsException() {
        String username = "Ali";
        TraineeUpdateDto dto = new TraineeUpdateDto("Ali", "Valiyev", true,
                new Date(), "Navai");
        Mockito.when(traineeRepository.findTraineeByUser_UserName(username)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> traineeService.update(username, dto));
        Assertions.assertEquals("Trainee not found", exception.getMessage());

        Mockito.verify(traineeRepository).findTraineeByUser_UserName(username);
        Mockito.verify(traineeMapper, Mockito.never()).partialUpdate(Mockito.any(), Mockito.any());
        Mockito.verify(traineeRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(traineeMapper, Mockito.never()).toDto(Mockito.any());
    }

    @Test
    void activateOrDeactivateTrainee_Success() {
        String username = "Ali";
        Mockito.when(traineeRepository.existsByUser_UserName(username)).thenReturn(true);
        Mockito.doNothing().when(traineeRepository).activateOrDeactivateTrainee(username);

        traineeService.activateOrDeactivateTrainee(username);

        Mockito.verify(traineeRepository).existsByUser_UserName(username);
        Mockito.verify(traineeRepository).activateOrDeactivateTrainee(username);
    }

    @Test
    void activateOrDeactivateTrainee_throwsException() {
        String username = "Ali";
        Mockito.when(traineeRepository.existsByUser_UserName(username)).thenReturn(false);

        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> traineeService.activateOrDeactivateTrainee(username));
        Assertions.assertEquals("Trainee not found", exception.getMessage());

        Mockito.verify(traineeRepository).existsByUser_UserName(username);
        Mockito.verify(traineeRepository, Mockito.never()).activateOrDeactivateTrainee(username);
    }

    @Test
    void deleteTraineeByUsername_Success() {
        String username = "Ali";
        Mockito.when(traineeRepository.existsByUser_UserName(username)).thenReturn(true);
        Mockito.doNothing().when(traineeRepository).deleteByUser_UserName(username);

        traineeService.deleteTraineeByUsername(username);

        Mockito.verify(traineeRepository).existsByUser_UserName(username);
        Mockito.verify(traineeRepository).deleteByUser_UserName(username);
    }

    @Test
    void deleteTraineeByUsername_ThrowsException() {
        String username = "Ali";
        Mockito.when(traineeRepository.existsByUser_UserName(username)).thenReturn(false);

        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> traineeService.deleteTraineeByUsername(username));
        Assertions.assertEquals("Trainee not found", exception.getMessage());
        Mockito.verify(traineeRepository, Mockito.never()).deleteByUser_UserName(username);
    }

    @Test
    void getTraineeTrainingsList_Success() {
        String traineeUsername = "Ali";
        Date fromDate = new Date(2025, 3, 4);
        Date toDate = new Date(2025, 3, 7);
        String trainerName = "Botir";
        Integer trainingTypeId = 3;
        List<Training> trainings = new ArrayList<>();
        Mockito.when(traineeRepository.existsByUser_UserName(traineeUsername)).thenReturn(true);

        Mockito.when(trainingRepository.getTraineeTrainingsListByTraineeUsernameAndCriteria(traineeUsername, fromDate, toDate, trainerName, trainingTypeId))
                .thenReturn(trainings);
        Mockito.when(trainingMapper.toDtoList(trainings)).thenReturn(new ArrayList<>());

        traineeService.getTraineeTrainingsList(traineeUsername, fromDate, toDate, trainerName, trainingTypeId);

        Mockito.verify(traineeRepository).existsByUser_UserName(traineeUsername);
        Mockito.verify(trainingRepository).getTraineeTrainingsListByTraineeUsernameAndCriteria(traineeUsername, fromDate, toDate, trainerName, trainingTypeId);
        Mockito.verify(trainingMapper).toDtoList(trainings);
    }

    @Test
    void getTraineeTrainingsList_ThrowsException() {
        String traineeUsername = "Ali";
        Date fromDate = new Date(2025, 3, 4);
        Date toDate = new Date(2025, 3, 7);
        String trainerName = "Botir";
        Integer trainingTypeId = 3;
        Mockito.when(traineeRepository.existsByUser_UserName(traineeUsername)).thenReturn(false);

        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> traineeService.getTraineeTrainingsList(traineeUsername, fromDate, toDate, trainerName, trainingTypeId));
        Assertions.assertEquals("Trainee not found", exception.getMessage());

        Mockito.verify(trainingRepository, Mockito.never()).getTraineeTrainingsListByTraineeUsernameAndCriteria(traineeUsername, fromDate, toDate, trainerName, trainingTypeId);
        Mockito.verify(trainingMapper, Mockito.never()).toDtoList(Mockito.any());
    }

    @Test
    void getTrainersListNotAssignedOnTrainee() {
    }

    @Test
    void updateTraineeTrainerList() {
    }
}