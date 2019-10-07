package com.example.test1.AccessAuth.DBAccesser

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import javax.servlet.http.HttpSession

@Repository
class UserInfoRepositry {

    @Autowired
    lateinit var jdbcTamplate : NamedParameterJdbcTemplate

    @Autowired
    lateinit var httpSession : HttpSession

    val tableName = "user_info_table"

    //user_idからuser_passを取得
    //取得できない場合はnullになる
    fun findAllbyId(): UserInfoTableForm?{
        val param = MapSqlParameterSource().addValue("id", httpSession.getAttribute("user_id"))
        try {
            return jdbcTamplate.queryForObject(
                    "SELECT * FROM `$tableName` WHERE user_id = :id",
                    param,
                    BeanPropertyRowMapper<UserInfoTableForm>(UserInfoTableForm::class.java))
        }catch(e: EmptyResultDataAccessException){
            return null
        }

    }
}