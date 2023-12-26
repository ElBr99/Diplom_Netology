package netology.diplom.filestorage.mapper;

import netology.diplom.filestorage.dto.UserDto;
import netology.diplom.filestorage.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapperImpl implements UserMapper {

    @Override
    public User toUser(UserDto userDto) {
        User userEntity = new User();
        userEntity.setLogin(userDto.getLogin());
        userEntity.setPassword(userDto.getPassword());
        return userEntity;
    }
}
