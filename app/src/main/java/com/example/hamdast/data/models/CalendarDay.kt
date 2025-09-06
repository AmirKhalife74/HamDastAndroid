package com.example.hamdast.data.models

data class CalendarDay(
    val day: Int,
    val month: Int,
    val year: Int,
    val isCurrentMonth: Boolean,
    val tasks:List<TaskModel> = mutableListOf<TaskModel>(),
    val percentageTaskHasBeenDone:Int?
)