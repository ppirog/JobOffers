package joboffers.domain.loginandregister;

import joboffers.domain.loginandregister.dto.UserMessageDto;
import joboffers.domain.loginandregister.dto.UserRequestDto;
import joboffers.domain.loginandregister.dto.UserResponseDto;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LoginAndRegisterFacade {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserMessageDto register(UserRequestDto requestDto) {

        User user = userMapper.toUser(requestDto);

        if (userRepository.existsByUsername(user.username())) {
            return UserMessageDto.builder()
                    .userRequestDto(UserResponseDto.builder()
                            .username(user.username())
                            .password(user.password())
                            .build())
                    .message("User already exists")
                    .build();
        }

        userRepository.save(user);

        final UserResponseDto userResponseDto = userMapper.toUserResponseDto(user);
        return userMapper.toUserMessageDto(userResponseDto, "User registered");

    }

    public UserResponseDto findByUsername(String username) {
        final User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundInDatabaseException("User not found"));

        return userMapper.toUserResponseDto(user);
    }
}
