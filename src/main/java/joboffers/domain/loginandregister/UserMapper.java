package joboffers.domain.loginandregister;

import joboffers.domain.loginandregister.dto.UserMessageDto;
import joboffers.domain.loginandregister.dto.UserRequestDto;
import joboffers.domain.loginandregister.dto.UserResponseDto;
import lombok.AllArgsConstructor;

@AllArgsConstructor
class UserMapper {
    private final IdGenerable idGenerable;

    public User toUser(UserRequestDto userRequestDto) {
        return User.builder()
                .id(idGenerable.generate())
                .username(userRequestDto.username())
                .password(userRequestDto.password())
                .isAdmin(false)
                .build();
    }

    public UserResponseDto toUserResponseDto(User user) {
        return UserResponseDto.builder()
                .username(user.username())
                .password(user.password())
                .isAdmin(user.isAdmin())
                .build();
    }

    public UserMessageDto toUserMessageDto(UserResponseDto userResponseDto, String message) {
        return UserMessageDto.builder()
                .userRequestDto(userResponseDto)
                .message(message)
                .build();
    }

}
