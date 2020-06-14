package com.epam.lab.service;

import com.epam.lab.dto.UserDTO;
import com.epam.lab.repository.UserRepository;
import com.epam.lab.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MapperUtil mapperUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, MapperUtil mapperUtil) {
        this.userRepository = userRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public UserDTO selectUser(long id) {
        return mapperUtil.convertUserToUserDTO(userRepository.findById(id));
    }

    @Override
    public List<UserDTO> selectUsers(int from, int howMany) {
        return userRepository.findAll(from, howMany).stream().map(mapperUtil::convertUserToUserDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteUser(long id) {
        userRepository.delete(id);
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        userRepository.update(mapperUtil.convertUserDTOToUser(userDTO));
        return selectUser(userDTO.getId());
    }

    @Override
    public UserDTO addUser(UserDTO userDTO) {
        long userId = userRepository.insert(mapperUtil.convertUserDTOToUser(userDTO));
        return mapperUtil.convertUserToUserDTO(userRepository.findById(userId));
    }

    @Override
    public UserDTO selectUserByLogin(String login) {
        return mapperUtil.convertUserToUserDTO(userRepository.findByLogin(login));
    }
}
