package com.example.test1.shiftmaker

import com.example.test1.shiftmaker.ShiftInfoDBAccesser.ShiftConfigRepositry
import com.example.test1.shiftmaker.ShiftInfoDBAccesser.ShiftTimeControlRepositry
import com.example.test1.shiftmaker.ShiftInfoDBAccesser.ShiftTimeelConfigRepositry
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.servlet.http.HttpSession

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
        if(shiftConfig != null) {
            startTime = shiftConfig.startTime
            endTime  = shiftConfig.endTime
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

    fun CreateShiftboardbodyColum(date:String): ArrayList<ArrayList<String>>? {

        var shiftRetLine:ArrayList<String> = ArrayList<String>()
        var shiftRet:ArrayList<ArrayList<String>> = ArrayList()

        //店の番号を取得
        var shopnum = httpSession.getAttribute("user_id").toString().toDouble() / 1000000 * 1000000
        //番号のオーバフローを避けるよう，番号を戻す
        var adminnumstr = (shopnum / 1000000).toInt().toString() + "000000"

        var memberInfo = shiftTimeControlRepositry.findAllbyIdandDate(adminnumstr,date)

        //日付の指定がない場合
        if(date=="")return null

        if(memberInfo != null) {

                shiftRetLine = formatJson(memberInfo.shiftDetails)
                //メンバー情報を配列に入れていく

                shiftRet.add(shiftRetLine)
                return shiftRet
        } else{
                return null
        }

    }

    //Json形式のデータを配列に入れて返す
    private fun formatJson(str :String):ArrayList<String>{
        var retArray =ArrayList<String>()
        var json1 = JSONObject(str)
        var arr = json1.getJSONArray("shift_details")
        for(i in 0..(arr.length()-1)) {
            var json2 = JSONObject(arr[i].toString())
            retArray.add(json2.get("name").toString())
            var arr2 = json2.get("shift").toString().split(",")
            arr2.forEach{
                if(it=="none")retArray.add("")
                else retArray.add(it)
            }
        }
        return retArray
    }

}
