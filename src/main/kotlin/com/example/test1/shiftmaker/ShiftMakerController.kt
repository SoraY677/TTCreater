package com.example.test1.shiftmaker

import com.example.test1.shiftmaker.GeneticAlgorythm.Population
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.text.SimpleDateFormat
import java.util.*
import javax.servlet.http.HttpSession

@Controller
class ShiftMakerController {

    @Autowired
    lateinit var shiftProcess : ShiftProcess

    @Autowired
    lateinit var ga: Population

    var date = ""

    @RequestMapping("/login" , "/shiftmaker","transShiftMaker")
    fun transShiftPage(model: Model) : String{
        var shiftboardTitles = shiftProcess.CreateShiftboardTitleColum()
        //今日の日付で行う
        date= SimpleDateFormat("yyyy-MM-dd").format(Date())
        shiftProcess.CreateShiftboardbodyColum(date)
        model.addAttribute("date",this.date)
        model.addAttribute("shifttitle",shiftboardTitles)
        model.addAttribute("adminmode",shiftProcess.isAdmin())

        return "/shiftmaker"
    }

    @RequestMapping("/changeShiftPage")
    fun changeShiftPage(model:Model, @RequestParam("date") date: String ) :String{
        this.date = date
        var shiftboardTitles = shiftProcess.CreateShiftboardTitleColum()
        var shiftcolum = shiftProcess.CreateShiftboardbodyColum(this.date)

        model.addAttribute("date",this.date)
        model.addAttribute("shifttitle",shiftboardTitles)
        model.addAttribute("shiftmemberlist",shiftcolum)
        model.addAttribute("adminmode",shiftProcess.isAdmin())

        return "/shiftmaker"
    }

    @RequestMapping("/createShiftAuto" )
    fun createShiftAuto(model:Model):String{
        ga.exp(date)
        var shiftboardTitles = shiftProcess.CreateShiftboardTitleColum()
        var shiftcolum = shiftProcess.CreateShiftboardbodyColum(this.date)

        model.addAttribute("date",this.date)
        model.addAttribute("shifttitle",shiftboardTitles)
        model.addAttribute("shiftmemberlist",shiftcolum)
        model.addAttribute("adminmode",shiftProcess.isAdmin())

        return "/shiftmaker"
    }



}