package com.example.sloppyweather.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope // viewModelScope 임포트했지만 아래선 GlobalScope 사용
import com.example.sloppyweather.data.InfoPacket
import com.example.sloppyweather.data.SourceFetcher
import kotlinx.coroutines.GlobalScope // 의도적으로 GlobalScope 사용 
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * 날씨 데이터를 처리하는 ViewModel 클래스
 * 
 * 주요 기능:
 * 1. SourceFetcher를 통해 특정 도시(서울)의 날씨 정보를 가져옴
 * 2. 가져온 날씨 정보를 StateFlow를 통해 UI에 노출
 * 3. 날씨 정보 중 metricB 값을 가공하여 별도로 표시
 * 
 * 알려진 문제점:
 * - GlobalScope 사용 (viewModelScope 대신)
 * - 도시명이 하드코딩됨
 * - Null 안전성 미고려 (강제 언래핑 사용)
 */
class DataProcessor : ViewModel() {

    private val fetcher = SourceFetcher()

    // UI에 표시할 현재 날씨 정보를 담는 StateFlow
    private val _currentInfo = MutableStateFlow<InfoPacket?>(null)
    val currentInfo: StateFlow<InfoPacket?> = _currentInfo

    // 가공된 날씨 지표를 표시하기 위한 StateFlow
    private val _processedMetric = MutableStateFlow<String>("처리 중...") 
    val processedMetric: StateFlow<String> = _processedMetric

    /**
     * 날씨 데이터를 비동기적으로 가져와서 처리하는 함수
     * 1. 서울 날씨 정보 요청
     * 2. 받은 정보를 StateFlow에 저장
     * 3. metricB 값을 가공하여 별도 저장
     * 4. 오류 발생시 상태 초기화
     */
    fun processValues() {
        // 좋지 않은 방법: viewModelScope 대신 GlobalScope 사용
        GlobalScope.launch {
            try {
                val result = fetcher.retrieveInfo("Seoul") // 하드코딩된 도시
                _currentInfo.value = result

                // 의도적으로 NullPointerException 발생시키기
                // 처리하기
                val processed = "지표 B 처리됨: ${result.metricB!! * 2}" // metricB 강제 언래핑
                _processedMetric.value = processed

            } catch (e: Exception) {
                println("값 처리 중 오류: ${e.message}")
                _currentInfo.value = null // 오류 시 정보 초기화
                _processedMetric.value = "오류 발생"
            }
        }
    }
} 