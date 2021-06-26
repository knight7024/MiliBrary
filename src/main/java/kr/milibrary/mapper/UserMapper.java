package kr.milibrary.mapper;

import kr.milibrary.domain.User;
import org.apache.ibatis.annotations.*;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;

@Repository("userMapper")
public interface UserMapper {
    @Select("SELECT * FROM milibrary.users WHERE narasarang_id = #{narasarangId} AND is_registered = true;")
    User getUserByNarasarangId(@Param("narasarangId") String narasarangId);

    @Insert("INSERT INTO milibrary.users (narasarang_id, password, nickname) " +
            "VALUES (#{narasarangId}, #{password}, #{nickname});")
    void createUser(User user) throws DataAccessException;

    @Select("SELECT * FROM milibrary.users u, " +
            "(SELECT * FROM milibrary.tokens WHERE token = #{token}) t " +
            "WHERE u.narasarang_id = t.narasarang_id;")
    User getUserByToken(@Param("token") String token);

    @Update("UPDATE milibrary.users SET password = #{password}, is_registered = #{registered} WHERE narasarang_id = #{narasarangId};")
    void updateUser(User user);

    @Update("UPDATE milibrary.users SET is_registered = #{registered}, registered_at = #{registeredAt} WHERE narasarang_id = #{narasarangId};")
    void updateUserRegistration(@Param("narasarangId") String narasarangId, @Param("registered") boolean registered, @Param("registeredAt") Timestamp registeredAt);

    @Delete("UPDATE milibrary.users SET is_registered = #{registered} WHERE narasarang_id = #{narasarangId};")
    void withdraw(User user);
}
