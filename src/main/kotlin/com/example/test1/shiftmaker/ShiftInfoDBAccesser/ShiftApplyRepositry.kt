package com.example.test1.shiftmaker.ShiftInfoDBAccesser

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import javax.servlet.http.HttpSession

class ShiftApplyRepositry {
    @Autowired
    private lateinit var jdbcTamplate : NamedParameterJdbcTemplate

    @Autowired
    private lateinit var httpSession : HttpSession

    val tableName = "shift_apply_table"

    //user_idからuser_passを取得
    //取得できない場合はnullになる
    fun findPassbyId(): ShiftApplyTableForm?{
        val param = MapSqlParameterSource().addValue("id", httpSession.getAttribute("user_id"))
        try {
            return jdbcTamplate.queryForObject(
                    "SELECT * FROM `$tableName` WHERE user_id = :id",
                    param,
                    BeanPropertyRowMapper<ShiftApplyTableForm>(ShiftApplyTableForm::class.java))
        }catch(e: EmptyResultDataAccessException){
            return null
        }

    }
}