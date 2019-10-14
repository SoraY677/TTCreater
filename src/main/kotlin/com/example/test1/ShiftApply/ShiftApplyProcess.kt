package com.example.test1.ShiftApply

import com.example.test1.ShiftInfoDBAccesser.ShiftApplyRepositry
import com.example.test1.ShiftInfoDBAccesser.ShiftConfigRepositry
import com.example.test1.ShiftInfoDBAccesser.ShiftTimeelConfigRepositry
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class ShiftApplyProcess {

    @Autowired
    lateinit var shiftConfigRepositry : ShiftConfigRepositry
    @Autowired
    lateinit var shiftConfigTimeelRepositry : ShiftTimeelConfigRepositry
    @Autowired
    lateinit var shiftApplyRepositry: ShiftApplyRepositry

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
        tableraw = 0;
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
            retline.add((content_i).toString())
            for(shiftc_i in 0..tableraw-1){
                retline.add((content_i-1).toString()+"-"+shiftc_i.toString())
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
        if(month!=2)ret = daynum[month-1]
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

    //シフト申請をデータベースに記録する
    public fun insertApplyShift(year:Int,month:Int,applyshift:List<String>){

        var arr:ArrayList<ArrayList<Int>> = arrayListOf()
        //配列初期化
        for(line_i in 0..(calcDayNum(year,month)-1)){
            var arrline:ArrayList<Int> = arrayListOf()
            for(colum_i in 0..(tableraw-1)) {
                arrline.add(0)
            }
            arr.add(arrline)
        }
        //条件に合ったものに1を追加
        applyshift.forEach {
            var index = it.split("-")
            var index0 =index[0].toInt()
            var index1 = index[1].toInt()
            arr[index0][index1] = 1
        }

        for(line_i in 0..(calcDayNum(year,month)-1)){
                //追加する日時
                val date = year.toString()+month.toString()+(line_i+1).toString()
                var shiftDetails = ""
                for(colum_i in 0..(tableraw-2)){
                    shiftDetails += arr[line_i][colum_i].toString() + ","
                }
                shiftDetails += arr[line_i][tableraw-1].toString()

                //既に存在していたら
                val flag = shiftApplyRepositry.countShiftDetails(date)
                if(flag !=null) {
                    if (flag.toString() != "0") {
                        shiftApplyRepositry.updateShiftDetails(date, shiftDetails)
                    } else {
                        shiftApplyRepositry.insertShiftDetails(date, shiftDetails)
                    }
                }

        }
    }


}