package com.example.test1.AccessAuth.DBAccesser

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

@Data
@AllArgsConstructor
@NoArgsConstructor
class UserInfoTableForm {
    var userId = ""
    var userName = ""
    var userStatus = 0
}