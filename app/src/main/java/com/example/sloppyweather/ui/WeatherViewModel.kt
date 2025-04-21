package com.example.sloppyweather.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope // viewModelScope 사용
import com.example.sloppyweather.data.InfoPacket
import com.example.sloppyweather.data.SourceFetcher
// import kotlinx.coroutines.GlobalScope // GlobalScope 사용 제거
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * 날씨 데이터를 가져오고 처리하여 UI에 제공하는 ViewModel 클래스입니다.
 *
 * 이 클래스는 다음 작업을 수행합니다:
 * 1. `SourceFetcher`를 사용하여 특정 도시의 날씨 정보를 비동기적으로 가져옵니다.
 * 2. 가져온 원시 날씨 데이터(`InfoPacket`)를 `currentInfo` StateFlow를 통해 노출합니다.
 * 3. 데이터 로딩 및 처리 상태를 UI가 관찰할 수 있도록 `StateFlow`를 사용합니다.
 *
 * @property currentInfo 현재 날씨 정보(InfoPacket)를 담는 StateFlow. 초기값은 null입니다.
 *
 * @see SourceFetcher 데이터 소스
 * @see InfoPacket 날씨 정보 데이터 클래스
 *
 * @constructor `SourceFetcher` 인스턴스를 내부적으로 생성합니다.
 */
class WeatherViewModel : ViewModel() {

    private val fetcher = SourceFetcher()

    // UI에 표시할 현재 날씨 정보를 담는 StateFlow
    private val _currentInfo = MutableStateFlow<InfoPacket?>(null)
    val currentInfo: StateFlow<InfoPacket?> = _currentInfo

    // 가공된 날씨 지표 StateFlow 제거

    /**
     * 특정 도시의 날씨 데이터를 비동기적으로 가져와서 처리하는 함수
     *
     * @param city 날씨 정보를 조회할 도시 이름 (기본값: "Seoul")
     */
    fun fetchAndProcessWeatherData(city: String = "Seoul") { // 도시 이름 파라미터 추가
        // viewModelScope 사용
        viewModelScope.launch {
            try {
                val result = fetcher.retrieveInfo(city) // 파라미터 사용
                _currentInfo.value = result
                // 습도 처리 로직 제거 (UI에서 직접 처리)
            } catch (e: Exception) {
                println("값 처리 중 오류: ${e.message}")
                _currentInfo.value = null // 오류 시 정보 초기화
                // processedMetric 업데이트 제거
            }
        }
    }
} 