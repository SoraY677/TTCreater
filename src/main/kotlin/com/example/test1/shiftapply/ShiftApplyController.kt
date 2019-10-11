package com.example.test1.shiftapply

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.text.SimpleDateFormat
import java.util.*

@Controller
class ShiftApplyController {

    @Autowired
    lateinit var shiftApplyProcess : ShiftApplyProcess

    @RequestMapping("/transShiftApply")
    fun transShiftApply(model: Model,@RequestParam("monthSelect") select: String ) :String{
        model.addAttribute("shifttitle",shiftApplyProcess.CreateShiftboardTitleColum())

        var tmp: Array<Int>
        var reqdate:String
        if(select=="") reqdate = SimpleDateFormat("yyyy.MM").format(Date()).toString()
        else reqdate = select

        println(reqdate)
        //model.addAttribute("shiftcontent",shiftApplyProcess.CreateShiftboardContent(reqdate))

        tmp = shiftApplyProcess.addYearandMonth(reqdate,0)
        model.addAttribute("monthNow",tmp[0].toString() + "." + tmp[1].toString())
        tmp  = shiftApplyProcess.addYearandMonth(reqdate,-1)
        model.addAttribute("monthPrev",tmp[0].toString() + "." + tmp[1].toString())
        tmp  = shiftApplyProcess.addYearandMonth(reqdate,1)
        model.addAttribute("monthNext",tmp[0].toString() + "." + tmp[1].toString())

        return "/shiftapply";
    }

}