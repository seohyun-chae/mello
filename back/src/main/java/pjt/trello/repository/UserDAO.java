package pjt.trello.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pjt.trello.model.UserVO;

@Mapper
public interface UserDAO {
    public UserVO getUser(@Param("userId")String id, @Param("password")String pwd);
}
