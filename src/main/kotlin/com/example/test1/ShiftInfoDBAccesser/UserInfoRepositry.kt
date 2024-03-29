package com.example.test1.ShiftInfoDBAccesser

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import javax.servlet.http.HttpSession
import kotlin.system.exitProcess

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

    fun findAllbyId(id:String): UserInfoTableForm? {
        val param = MapSqlParameterSource().addValue("id",id)
        try {
            return jdbcTamplate.queryForObject(
                    "SELECT * FROM `$tableName` WHERE user_id = :id",
                    param,
                    BeanPropertyRowMapper<UserInfoTableForm>(UserInfoTableForm::class.java))
        }catch(e: EmptyResultDataAccessException){
            return null
        }

    }

    fun countAllUserofOrg():Int?{
        //店の番号を取得
        var shopnum = httpSession.getAttribute("user_id").toString().toDouble() / 1000000 * 1000000
        //番号のオーバフローを避けるよう，番号を戻す
        var adminnumstr = (shopnum / 1000000).toInt().toString()+"%"

        val param = MapSqlParameterSource()
                .addValue("id", adminnumstr)
        try {
            val ret = jdbcTamplate.queryForObject(
                    "SELECT COUNT(*) FROM `$tableName` WHERE `user_id` LIKE :id",
                    param,
                    Int::class.java)
            return ret
        }catch(e: EmptyResultDataAccessException){
            println(e)
            exitProcess(1)
        }
    }


}