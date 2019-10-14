package com.example.test1.shiftmaker

import com.example.test1.ShiftInfoDBAccesser.ShiftConfigRepositry
import com.example.test1.ShiftInfoDBAccesser.ShiftTimeControlRepositry
import com.example.test1.ShiftInfoDBAccesser.ShiftTimeelConfigRepositry
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.servlet.http.HttpSession
import kotlin.system.exitProcess

@Service
class ShiftProcess {

    @Autowired
    lateinit var httpSession : HttpSession

    @Autowired
    lateinit var shiftConfigTimeelRepositry : ShiftTimeelConfigRepositry

    @Autowired
    lateinit var shiftConfigRepositry : ShiftConfigRepositry

    @Autowired
    lateinit var shiftTimeControlRepositry: ShiftTimeControlRepositry

    //シフトボードの生成用に列のタイトル（シフトの時間）である配列を生成し返す
    fun CreateShiftboardTitleColum(): ArrayList<String> {

        var shiftRet:ArrayList<String> = ArrayList<String>()

        val shiftTimeelConfig = shiftConfigTimeelRepositry.findAllbyId()
        val shiftConfig = shiftConfigRepositry.findAllbyId()

        var timeElement = 0
        var startTime = 0
        var endTime = 0
        var timeperPerson = 0
        var job = ""


        if(shiftTimeelConfig != null) {
            timeElement = shiftTimeelConfig.timeElement
        }
        else{
            println("shiftTimeelConfig == null")
            exitProcess(1)
        }
        if(shiftConfig != null) {
            startTime = shiftConfig.startTime
            endTime = shiftConfig.endTime
        }else{
            println("shiftConfig == null")
            exitProcess(1)
        }

        for(shift_i in startTime..(endTime-timeElement) step timeElement){
            shiftRet.add(calcMinutetoDate(shift_i))
        }

        return shiftRet
    }

    //分で表現された時間をyy:mmに修正
    fun calcMinutetoDate(minute:Int) : String{
        var hour  = Math.floor(minute.toDouble()/ 60).toInt();
        var min = minute % 60
        var rethour = hour.toString()
        if(hour<10) rethour = "0" + rethour
        var retminute = min.toString()
        if(min < 10) retminute = "0"+ retminute

        return rethour + ":"+ retminute;
    }


    //シフト生成
    fun CreateShiftboardbodyColum(date:String): ArrayList<ArrayList<String>>? {

        var shiftRetLine = ArrayList<ArrayList<String>>()
        var shiftRet:ArrayList<ArrayList<String>> = ArrayList()

        //店の番号を取得
        var shopnum = httpSession.getAttribute("user_id").toString().toDouble() / 1000000 * 1000000
        //番号のオーバフローを避けるよう，番号を戻す
        var adminnumstr = (shopnum / 1000000).toInt().toString() + "000000"

        var memberInfo = shiftTimeControlRepositry.findAllbyIdandDate(adminnumstr,date)



        //日付の指定がない場合
        if(date=="")return null

        if(memberInfo != null) {

            shiftRet = formatStr(memberInfo.shiftDetails)
            return shiftRet
        } else{
                return null
        }
    }

    //Json形式のデータを配列に入れて返す
    private fun formatStr(str :String):ArrayList<ArrayList<String>>{
        var ret = ArrayList<ArrayList<String>>()

        var retlinetmp = str.split("/")

        retlinetmp.forEach{
            var retlinetmp2 = ArrayList<String>()
            var tmp = it.split(",")
            tmp.forEach{
                retlinetmp2.add(it)
            }
            ret.add(retlinetmp2)
        }

        return ret
    }

    //管理者かどうか
    public fun isAdmin():Boolean{
        //店の番号を取得
        var shopnum = httpSession.getAttribute("user_id").toString().toDouble() / 1000000 * 1000000
        var adminnumstr = (shopnum / 1000000).toInt().toString() + "000000"
        if(httpSession.getAttribute("user_id").toString() == adminnumstr){
            return true
        }
        else{
            return false
        }
    }

}
