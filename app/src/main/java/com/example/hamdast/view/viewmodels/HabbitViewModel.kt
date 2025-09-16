package com.example.hamdast.view.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hamdast.data.models.HabitModel
import com.example.hamdast.data.repos.HabitsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val habitRepository: HabitsRepository
) : ViewModel() {

    private val _habits = MutableStateFlow<List<HabitModel>>(emptyList())
    val habits: StateFlow<List<HabitModel>> = _habits

    init {
        viewModelScope.launch {
            habitRepository.habits.collect {
                _habits.value = it
            }
        }
    }

    fun addHabit(habit: HabitModel) {
        viewModelScope.launch {
            habitRepository.addHabit(habit)
        }
    }

    fun deleteHabit(id: Int) {
        viewModelScope.launch {
            habitRepository.deleteHabit(id)
        }
    }


    fun getHabitsInMonth(year: Int, month: Int): Flow<List<HabitModel>> = flow {
//        val (gyStart, gmStart, gdStart) = persianToGregorian(year, month, 1)
//        val (gyEnd, gmEnd, gdEnd) = persianToGregorian(year, month, daysInMonth[month - 1])
//
//        val startDate = "$gyStart-${twoDigitConvertor(gmStart)}-${twoDigitConvertor(gdStart)}"
//        val endDate = "$gyEnd-${twoDigitConvertor(gmEnd)}-${twoDigitConvertor(gdEnd)}"

        habitRepository.getHabitsInMonth(year, month).collect { tasks ->
            emit(tasks)
        }
    }
}
