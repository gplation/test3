package com.example.sloppyweather.data

import kotlinx.coroutines.delay
import kotlin.random.Random

/**
 * 날씨 정보를 가져오는 클래스
 * 실제 API 호출 대신 랜덤 데이터를 생성하여 날씨 정보를 시뮬레이션함
 */
class SourceFetcher {

    /**
     * 특정 도시의 날씨 정보를 가져오는 함수
     * 
     * @param city 날씨 정보를 조회할 도시 이름
     * @return InfoPacket 형태의 날씨 정보
     * 
     * 주요 동작:
     * 1. 네트워크 지연을 시뮬레이션 (1.5초)
     * 2. 랜덤한 날씨 데이터 생성
     *   - 온도: 15.0°C ~ 30.0°C 사이의 랜덤 값
     *   - 날씨 상태: "Sunny", "Cloudy", "Rainy" 중 랜덤 (가끔 null)
     *   - 습도: 30% ~ 80% 사이의 랜덤 값 (가끔 null)
     */
    suspend fun retrieveInfo(city: String): InfoPacket {
        println("Fetching info for $city...") // 데이터 요청 시작 로깅
        delay(1500) // API 호출 지연시간 시뮬레이션

        // 습도값 생성 - 50% 확률로 null 반환하여 오류 상황 시뮬레이션
        val sometimesNullMetricB = if (Random.nextBoolean()) Random.nextInt(30, 80) else null

        // 날씨 정보 패킷 생성
        val packet = InfoPacket(
            valueA = Random.nextDouble(15.0, 30.0), // 현재 기온 생성
            condition = listOf("Sunny", "Cloudy", "Rainy", null).random(), // 날씨 상태 생성
            metricB = sometimesNullMetricB // 습도값 설정
        )
        println("Info fetched: $packet") // 생성된 데이터 로깅
        return packet
    }
} 