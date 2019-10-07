package com.example.test1.shiftmaker.ShiftInfoDBAccesser

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import javax.servlet.http.HttpSession

@Repository
class ShiftTimeelConfigRepositry {

    @Autowired
    private lateinit var jdbcTamplate : NamedParameterJdbcTemplate

    @Autowired
    private lateinit var httpSession : HttpSession

    val tableName = "shift_timeel_config_table"

    //user_idからuser_passを取得
    //取得できない場合はnullになる
    fun findAllbyId(): ShiftTimeelConfigTableForm?{
        val param = MapSqlParameterSource().addValue("id", httpSession.getAttribute("user_id"))
        try {
            return jdbcTamplate.queryForObject(
                    "SELECT * FROM `$tableName` WHERE user_id = :id",
                    param,
                    BeanPropertyRowMapper<ShiftTimeelConfigTableForm>(ShiftTimeelConfigTableForm::class.java))
        }catch(e: EmptyResultDataAccessException){
            return null
        }

    }
}