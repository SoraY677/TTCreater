package com.example.test1.shiftapply

import com.example.test1.shiftmaker.ShiftInfoDBAccesser.ShiftConfigRepositry
import com.example.test1.shiftmaker.ShiftInfoDBAccesser.ShiftTimeelConfigRepositry
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class ShiftApplyProcess {

    @Autowired
    lateinit var shiftConfigRepositry : ShiftConfigRepositry
    @Autowired
    lateinit var shiftConfigTimeelRepositry : ShiftTimeelConfigRepositry

    var tableraw = 0

    fun CreateShiftboardTitleColum(): ArrayList<String>{

        var shiftRet:ArrayList<String> = ArrayList<String>()

        val shiftTimeelConfig = shiftConfigTimeelRepositry.findAllbyId()
        val shiftConfig = shiftConfigRepositry.findAllbyId()

        var timeElement = 0
        var startTime = 0
        var endTime = 0

        if(shiftTimeelConfig != null) {
            timeElement = shiftTimeelConfig.timeElement
        }
        if(shiftConfig != null) {
            startTime = shiftConfig.startTime
            endTime  = shiftConfig.endTime
        }

        //一段目の生成
        shiftRet.add("")
        for(shift_i in startTime..(endTime-timeElement) step timeElement){
            tableraw++
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

    //テーブルの中身を生成
    //引数は年月(yyyy:mm)の文字列
    public fun CreateShiftboardContent(yearandmonth : String):ArrayList<ArrayList<String>>{
        val date = addYearandMonth(yearandmonth , 0)
        var dayNum = calcDayNum(date[0],date[1])

        var ret = arrayListOf<ArrayList<String>>()

        for(content_i in 1..dayNum){
            var retline = arrayListOf<String>()
            retline.add(content_i.toString())
            for(shiftc_i in 0..tableraw){
                retline.add("")
            }
            ret.add(retline)
        }

        return ret

    }

    //年月指定数を受け取り，年月に加算したものを返す
    //例 (2019.12,1)を渡すと([2020,1])が返ってくる
    public fun addYearandMonth(yearandmonth : String, addNum:Int):Array<Int>{
        //"."で分ける．0に年，1に月が入る
        var inputdatetmp = yearandmonth.split(".")
        var year  = inputdatetmp[0].toInt()
        var month = inputdatetmp[1].toInt()

        var tmp = month + addNum

        if(tmp > 12) {
            month = 1
            year += 1
        }
        else if(tmp < 1){
            month = 12
            year -= 1
        }

        else{
            month += addNum
        }

        val ret = arrayOf(year,month)
        return ret
    }
    //月の日数を計算する
    private fun calcDayNum(year:Int,month:Int):Int{
        //うるう年を除いた日数定義
        var daynum = arrayOf(31,28,31,30,31,30,31,31,30,31,30,31)
        var ret = 0;
        //二月以外はうるう年関係なし
        if((month-1)!=2)ret = daynum[month-1]
        //二月はうるう年の可能性考慮
        else{
            //うるう年計算
            if((year%100 == 0) && (year%400!= 0)){
                ret = daynum[month-1]
            }
            else if(year % 4 == 0){
                ret = daynum[month-1] + 1
            }
            else{
                ret = daynum[month-1]
            }
        }
        return ret
    }



}