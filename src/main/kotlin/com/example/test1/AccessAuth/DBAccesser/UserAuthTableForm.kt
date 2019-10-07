package com.example.test1.AccessAuth.DBAccesser

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

@Data
@AllArgsConstructor
@NoArgsConstructor
class UserAuthTableForm {
    var userId:String = ""
    var userPassword:String = ""
}