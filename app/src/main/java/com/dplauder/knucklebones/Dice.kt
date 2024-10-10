package com.dplauder.knucklebones

import kotlin.random.Random

class Dice(var value: Int){
    fun roll(){
        value = Random.nextInt(1, 7)
    }
    fun reset(){
        value = 0
    }
}