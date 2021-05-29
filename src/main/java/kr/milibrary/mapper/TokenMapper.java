package kr.milibrary.mapper;

import kr.milibrary.domain.Token;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

@Repository("tokenMapper")
public interface TokenMapper {
    @Insert("INSERT INTO milibrary.tokens (narasarang_id, token, token_type) " +
            "VALUES (#{narasarangId}, #{token}, #{tokenType})")
    void createToken(Token token) throws SQLException;

    @Select("SELECT * FROM milibrary.tokens WHERE token = #{token}")
    Token getToken(@Param("token") String token);
}
