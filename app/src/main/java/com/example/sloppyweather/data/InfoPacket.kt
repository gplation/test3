package com.example.sloppyweather.data

// 어떤 값들을 담는 데이터 구조
data class InfoPacket(
    val temperature: Double?, // 온도 같은 값인가?
    val weatherCondition: String?, // 날씨 상태?
    val humidity: Int? // 습도? 가끔 null임
) 