package com.example.test1.shiftmaker.ShiftInfoDBAccesser

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import javax.servlet.http.HttpSession

@Repository
class ShiftTimeControlRepositry {


        @Autowired
        private lateinit var jdbcTamplate : NamedParameterJdbcTemplate

        @Autowired
        private lateinit var httpSession : HttpSession

        val tableName = "shift_time_control_table"

        //user_idからuser_passを取得
        //取得できない場合はnullになる
        fun findAllbyIdandDate(userId:String,date:String): ShiftTimeControlTableForm?{
            var param = MapSqlParameterSource()
                    .addValue("id", userId)
                    .addValue("date",date)
            try {
                return jdbcTamplate.queryForObject(
                        "SELECT * FROM `$tableName` WHERE `user_id`=:id AND `date` = :date",
                        param,
                        BeanPropertyRowMapper<ShiftTimeControlTableForm>(ShiftTimeControlTableForm::class.java))
            }catch(e: EmptyResultDataAccessException){
                return null
            }

        }

}