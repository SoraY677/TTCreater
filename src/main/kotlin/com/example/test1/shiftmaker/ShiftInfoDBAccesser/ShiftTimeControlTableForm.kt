package com.example.test1.shiftmaker.ShiftInfoDBAccesser

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

@Data
@AllArgsConstructor
@NoArgsConstructor
class ShiftTimeControlTableForm {
    var user_id = ""
    var date = ""
    var shiftDetails = ""
}