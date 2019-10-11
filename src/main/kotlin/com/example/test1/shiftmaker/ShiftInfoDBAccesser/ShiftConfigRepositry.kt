package com.example.test1.shiftmaker.ShiftInfoDBAccesser

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import javax.servlet.http.HttpSession

@Repository
class ShiftConfigRepositry {
    @Autowired
    private lateinit var jdbcTamplate : NamedParameterJdbcTemplate

    @Autowired
    private lateinit var httpSession : HttpSession

    val tableName = "shift_config_table"

    //user_idからuser_passを取得
    //取得できない場合はnullになる
    fun findAllbyId(): ShiftConfigTableForm?{
        //店の番号を取得
        var shopnum = httpSession.getAttribute("user_id").toString().toDouble() / 1000000 * 1000000
        //番号のオーバフローを避けるよう，番号を戻す
        val adminnumstr = (shopnum / 1000000).toInt().toString() + "000000"

        val param = MapSqlParameterSource().addValue("id", adminnumstr)

        try {
            return jdbcTamplate.queryForObject(
                    "SELECT * FROM `$tableName` WHERE user_id = :id",
                    param,
                    BeanPropertyRowMapper<ShiftConfigTableForm>(ShiftConfigTableForm::class.java))
        }catch(e: EmptyResultDataAccessException){
            return null
        }

    }
}