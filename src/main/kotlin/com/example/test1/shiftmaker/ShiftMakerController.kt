package com.example.test1.shiftmaker

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.text.SimpleDateFormat
import java.util.*

@Controller
class ShiftMakerController {

    @Autowired
    lateinit var shiftProcess : ShiftProcess

    @RequestMapping("/login" )
    fun transShiftPage(model: Model) : String{
        var shiftboardTitles = shiftProcess.CreateShiftboardTitleColum()
        //今日の日付で行う
        var date= SimpleDateFormat("yyyy-MM-dd").format(Date())
        shiftProcess.CreateShiftboardbodyColum(date)
        model.addAttribute("shifttitle",shiftboardTitles)
        return "/shiftmaker"
    }

    @RequestMapping("/changeShiftPage")
    fun changeShiftPage(model:Model, @RequestParam("date") date: String ) :String{

        var shiftboardTitles = shiftProcess.CreateShiftboardTitleColum()
        var shiftcolum = shiftProcess.CreateShiftboardbodyColum(date)
        //
        if(shiftcolum==null) {
            println("こらこら")
            var nowdate= SimpleDateFormat("yyyy-MM-dd").format(Date())
            shiftcolum = shiftProcess.CreateShiftboardbodyColum(nowdate)
        }

        model.addAttribute("shifttitle",shiftboardTitles)
        model.addAttribute("shiftmemberline",shiftcolum)

        return "/shiftmaker"
    }


}