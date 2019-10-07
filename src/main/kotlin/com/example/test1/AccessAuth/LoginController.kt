package com.example.test1.AccessAuth

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam




@Controller
class LoginController {
    @RequestMapping("/","login-page")
    fun transHomePage(model: Model, @RequestParam("error",defaultValue = "false") errorflag : String?):String{
        val errorEcho =
                if (errorflag=="true") "ユーザIDまたはパスワードが違います!管理者に問い合わせてください!"
                else ""
        model.addAttribute("errorAlertFlag",errorEcho)
        return "index"
    }
}