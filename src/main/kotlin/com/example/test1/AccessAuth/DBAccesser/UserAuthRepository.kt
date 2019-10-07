package com.example.test1.AccessAuth.DBAccesser

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository


@Repository
class UserAuthRepository {

    @Autowired
    lateinit var jdbcTamplate : NamedParameterJdbcTemplate

    val tableName = "user_auth_table"

    //user_idからuser_passを取得
    //取得できない場合はnullになる
    fun findPassbyId(id: String): UserAuthTableForm?{
        val param = MapSqlParameterSource().addValue("id", id)
        try {
            return jdbcTamplate.queryForObject(
                    "SELECT * FROM `$tableName` WHERE user_id = :id",
                    param,
                    BeanPropertyRowMapper<UserAuthTableForm>(UserAuthTableForm::class.java))
        }catch(e: EmptyResultDataAccessException){
            return null
        }

    }

}