package com.example.hamdast.data.models

data class CalendarDay(
    val day: Int,
    val month: Int,
    val year: Int,
    val isCurrentMonth: Boolean,
    val tasks:List<TaskModel>? =  emptyList<TaskModel>(),
    val habits: List<HabitModel>? = emptyList<HabitModel>(),
//    val isClicked: Boolean,
    val percentageTaskHasBeenDone:Int? = 0
)