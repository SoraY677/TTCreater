package com.example.test1.shiftmaker.ShiftInfoDBAccesser

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

@Data
@AllArgsConstructor
@NoArgsConstructor
class ShiftConfigTableForm {
    var userId = ""
    var startTime = 0
    var endTime = 0
    var timePerPerson = 0
    var job = ""
}