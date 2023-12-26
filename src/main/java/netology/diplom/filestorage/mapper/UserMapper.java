package netology.diplom.filestorage.mapper;

import netology.diplom.filestorage.dto.UserDto;
import netology.diplom.filestorage.entity.User;

public interface UserMapper {

    User toUser(UserDto userDto);

}
