package com.example.test1.shiftmaker.GeneticAlgorythm

import com.example.test1.ShiftInfoDBAccesser.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import javax.servlet.http.HttpSession
import kotlin.collections.ArrayList
import kotlin.system.exitProcess

@Service
class Population {
    val CHROMNUM = 10 //染色体数
    val MUTATEPROB = 0.95 //突然変異率

    val CROSSOVERNUM  = 100

    @Autowired
    lateinit var httpSession: HttpSession

    @Autowired
    lateinit var shiftTimeelConfigRepositry : ShiftTimeelConfigRepositry

    @Autowired
    lateinit var shiftConfigRepositry: ShiftConfigRepositry

    @Autowired
    lateinit var shiftApplyRepositry: ShiftApplyRepositry

    @Autowired
    lateinit var userInfoRepositry : UserInfoRepositry

    @Autowired
    lateinit var shiftTimeControlRepositry: ShiftTimeControlRepositry

    //母集団
    var popSet = arrayListOf<ArrayList<ArrayList<Int>>>()
    //各染色体の評価値
    var popSetEval = arrayListOf<Int>()

    //店の番号を取得
    var SHOPNUM =  0.0

    var memberNum = 0
    lateinit var userInfo:UserInfoTableForm
    var job = listOf<String>()
    var jobnum = 0
    var timeel = 0
    var starttime = 0
    var endtime = 0
    var timenum = 0
    var jobname = arrayListOf<String>()
    var memberName = arrayListOf<String>()

    var date = ""



    public fun Init(date:String){
        SHOPNUM = httpSession.getAttribute("user_id").toString().toDouble()
        var datetmp = date.split("-")
        this.date = datetmp[0]+datetmp[1]+datetmp[2]
    }


    //初期集団生成
    public fun createPoplation(){
        val userNum = userInfoRepositry.countAllUserofOrg()
        val userInfo =userInfoRepositry.findAllbyId()
        val shiftconfig = shiftConfigRepositry.findAllbyId()
        val shifttimeelconfig = shiftTimeelConfigRepositry.findAllbyId()



        if(shiftconfig!=null) {
            this.job = shiftconfig.job.split(",")
            job.forEach{
                jobname.add(it)
            }
            this.jobnum = this.job.size

        }
        else {
            println("存在しないテーブルアクセスを行いました")
            exitProcess(0)
        }
        if(shifttimeelconfig != null)this.timeel = shifttimeelconfig.timeElement
        if(userNum != null) this.memberNum = userNum
        if(userInfo != null)this.userInfo = userInfo

        for(member_i in 0..memberNum-1){
            var userid = (SHOPNUM / 1000000).toInt().toString() + String.format("%06d", member_i)
            var userinfotmp = userInfoRepositry.findAllbyId(userid)
            if(userinfotmp != null)memberName.add(userinfotmp.userName)
        }


        starttime = shiftconfig.startTime
        endtime = shiftconfig.endTime

        timenum = (this.endtime - this.starttime) / this.timeel
        //初期集団生成
        for(popset_i in 0 .. this.CHROMNUM-1) {
            //評価を0に
            popSetEval.add(0)
            var chrom  = arrayListOf<ArrayList<Int>>()
            for (chrom_i in 0..this.jobnum - 1) {
                var tmp = ArrayList<Int>()

                for (onejob_i in 0..timenum- 1) {
                    tmp.add(Random().nextInt(this.memberNum))
                }
                chrom.add(tmp)
            }
            popSet.add(chrom)
        }


        //評価
        evaluation()

    }

    //評価する
    public fun evaluation() {
        for (popset_i in 0..this.CHROMNUM - 1) {
            this.popSetEval[popset_i] = 0
            for (chrom_i in 0..popSet[popset_i].size - 1) {
                for (onejob_i in 0..popSet[popset_i][chrom_i].size - 1) {
                    //番号のオーバフローを避けるよう，番号を戻す
                    var userid = (SHOPNUM / 1000000).toInt().toString() + String.format("%06d", popSet[popset_i][chrom_i][onejob_i])
                    //シフトの申請状況を取得
                    var tmp = shiftApplyRepositry.findAllbyIdandDate(userid,date)

                    //ユーザが存在する場合
                    if (tmp != null) {
                        println(tmp.shiftdetails)
                        var shiftdetails = tmp.shiftdetails.split(",")
                        //申請状況と生成した染色体の値が同じであり
                        if (shiftdetails[onejob_i] == "1") {
                            var userjob = userInfo.userJob.split(",")
                            //ユーザの職種が一緒であれば
                            userjob.forEach {
                                var userjobtmp = it
                                popSetEval[popset_i]++
                                job.forEach {
                                    if (userjobtmp == it) {
                                        //シフトが重なっていた場合はダメ
                                        var flag = 0
                                        for (job_i in 0..jobnum - 1) {
                                            if (flag == 0 && popSet[popset_i][job_i][onejob_i] == 1) {
                                                flag = 1
                                            } else if (flag == 1) {
                                                popSetEval[popset_i]--
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //存在しなければ
                    else {

                    }

                }
            }
        }
    }

    //進化させる
    public fun evolveNextPop(){
        var exelparent = selectParent()
        for(pop_i in 0..this.popSet.size-1){
            //交叉
            crossOver(pop_i,exelparent)
        }
    }

    //親選択
    public fun selectParent():ArrayList<ArrayList<ArrayList<Int>>>{
        var rank = arrayOf<Int>(this.popSetEval[0],this.popSetEval[1])
        var rank_i = arrayOf(0,1)
        for(setpop_i in 2..this.popSet.size-1){
            if(rank[0] < popSetEval[setpop_i]){
                rank_i[1] = rank_i[0]
                rank_i[0] = setpop_i
                rank[1] = rank[0]
                rank[0] = popSetEval[setpop_i]
            }
            else if(rank[1] < popSetEval[setpop_i]){
                rank_i[1] = setpop_i
                rank[1] = popSetEval[setpop_i]
            }
        }
        return arrayListOf(popSet[rank[0]],popSet[rank[1]])
    }

    //二点交叉
    public fun crossOver(index_i:Int,execParent:ArrayList<ArrayList<ArrayList<Int>>>){
        //交叉点
        var point1 = Random().nextInt(timenum*jobnum)
        var point2 = Random().nextInt(timenum*jobnum - point1)

        var pointIndex = arrayOf(0,0)

        var pointi= 0
        for(pointi in 0 ..point1){
            pointIndex = arrayOf(pointi/timenum,pointi%timenum)
            popSet[index_i][pointIndex[0]][pointIndex[1]] = execParent[0][pointIndex[0]][pointIndex[1]]
        }
        for(pointi in point1+1..point2){
            pointIndex = arrayOf(pointi/timenum,pointi%timenum)
            popSet[index_i][pointIndex[0]][pointIndex[1]] = execParent[1][pointIndex[0]][pointIndex[1]]
        }
        for(pointi in point2+1..(timenum*jobnum-1)){
            pointIndex = arrayOf(pointi/timenum,pointi%timenum)
            popSet[index_i][pointIndex[0]][pointIndex[1]] = execParent[0][pointIndex[0]][pointIndex[1]]
        }
    }

    //突然変異
    public fun mutate(){
        for (popset_i in 0..this.CHROMNUM - 1) {
            for (chrom_i in 0..popSet[popset_i].size - 1) {
                for (onejob_i in 0..popSet[popset_i][chrom_i].size - 1) {
                    if(Random().nextDouble() > MUTATEPROB){
                        popSet[popset_i][chrom_i][onejob_i] = Random().nextInt(memberNum)
                    }
                }
            }
        }
    }

    public fun exp(date:String){

        //初期化
        Init(date)

        //初期集団生成
        createPoplation()

        for(crossi in 1..CROSSOVERNUM) {

            //次世代の親選択をし，交叉
            evolveNextPop()

            //突然変異
            mutate()

            //評価
            evaluation()
        }

        //DBに記録
        insertShift(date)
    }

    //シフトを記録する
    public fun insertShift(date:String) {
        var datetmp = date.split("-")
        var dateFormat = datetmp[0] + "-" + datetmp[1] + "-" + datetmp[2]

        //保存データを作成
        var savestr = ""
        var data = selectParent()[0]

        for (chrom_i in 0..data.size - 1) {
            savestr += jobname[chrom_i] + ","
            for (job_line in 0..data[chrom_i].size - 1) {
                savestr += memberName[data[chrom_i][job_line]]

                if (job_line == data[chrom_i].size - 1) break
                else savestr += ","
            }
            if (chrom_i == data.size - 1) break
            else savestr += "/"
        }

        //まだシフトが存在していない
        if(shiftTimeControlRepositry.countShift(dateFormat)==0){
            shiftTimeControlRepositry.insertShiftDetails(dateFormat,savestr)
        }
        //まだ存在していない
        else{
            shiftTimeControlRepositry.updateShift(dateFormat,savestr)
        }
    }

}

