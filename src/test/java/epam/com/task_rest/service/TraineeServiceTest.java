package epam.com.task_rest.service;

import epam.com.task_rest.dto.LoginRequestDto;
import epam.com.task_rest.dto.trainee.TraineeCreateDto;
import epam.com.task_rest.dto.trainee.TraineeDto;
import epam.com.task_rest.entity.Trainee;
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

import java.util.Date;
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
    void changePassword() {
    }

    @Test
    void update() {
    }

    @Test
    void activateOrDeactivateTrainee() {
    }

    @Test
    void deleteTraineeByUsername() {
    }

    @Test
    void getTraineeTrainingsList() {
    }

    @Test
    void getTrainersListNotAssignedOnTrainee() {
    }

    @Test
    void updateTraineeTrainerList() {
    }
}