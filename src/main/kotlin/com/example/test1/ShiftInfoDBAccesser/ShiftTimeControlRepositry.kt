package com.example.test1.ShiftInfoDBAccesser

import com.mysql.cj.x.protobuf.MysqlxExpr
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.util.ArrayList
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

    fun countShift(date:String): Int?{
        val param = MapSqlParameterSource()
                .addValue("id", httpSession.getAttribute("user_id").toString())
                .addValue("date", date)
        try {
            val ret = jdbcTamplate.queryForObject(
                    "SELECT COUNT(*) FROM `$tableName` WHERE `user_id`= :id AND `date`= :date;",
                    param,
                    Int::class.java)
            return ret
        }catch(e: EmptyResultDataAccessException){
            return null
        }
    }

    fun updateShift(date:String,shiftDetails:String):Boolean?{{}
        val param = MapSqlParameterSource()
                .addValue("id", httpSession.getAttribute("user_id"))
                .addValue("date", date)
                .addValue("shiftDetails",shiftDetails)
        try {
            jdbcTamplate.update(
                    "UPDATE `$tableName` SET `shift_details`=:shiftDetails WHERE `user_id`=:id AND `date`=:date;",
                    param)
            return true
        }catch(e: EmptyResultDataAccessException) {
            return null
        }
    }

    fun insertShiftDetails(date:String,shiftDetails:String):Boolean?{
        val param = MapSqlParameterSource()
                .addValue("id", httpSession.getAttribute("user_id"))
                .addValue("date", date)
                .addValue("shiftdetails",shiftDetails)
        try {
            lateinit var objects: ArrayList<MysqlxExpr.Object>
            jdbcTamplate.update(
                    "INSERT INTO `$tableName`(`user_id`, `date`, `shift_details`) VALUES (:id, :date ,:shiftdetails);",
                    param)
            return true
        }catch(e: EmptyResultDataAccessException){
            return null
        }
    }

}