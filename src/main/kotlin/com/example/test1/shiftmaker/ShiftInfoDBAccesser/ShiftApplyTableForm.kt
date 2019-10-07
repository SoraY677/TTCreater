package com.example.test1.shiftmaker.ShiftInfoDBAccesser

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

@Data
@AllArgsConstructor
@NoArgsConstructor
class ShiftApplyTableForm {

    var userId =""
    var date = 0
    var timeElement = 0
    var startTime = 0
    var endTime = 0

}