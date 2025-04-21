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
 * 날씨 데이터를 가져오고 처리하여 UI에 제공하는 ViewModel 클래스입니다.
 *
 * 이 클래스는 다음 작업을 수행합니다:
 * 1. `SourceFetcher`를 사용하여 특정 도시("Seoul")의 날씨 정보를 비동기적으로 가져옵니다.
 * 2. 가져온 원시 날씨 데이터(`InfoPacket`)를 `currentInfo` StateFlow를 통해 노출합니다.
 * 3. 날씨 데이터 중 `metricB` 값을 가공하여 `processedMetric` StateFlow를 통해 별도로 노출합니다.
 * 4. 데이터 로딩 및 처리 상태를 UI가 관찰할 수 있도록 `StateFlow`를 사용합니다.
 *
 * @property currentInfo 현재 날씨 정보(InfoPacket)를 담는 StateFlow. 초기값은 null입니다.
 * @property processedMetric 가공된 날씨 지표 B의 문자열 표현을 담는 StateFlow. 초기값은 "처리 중..."입니다.
 *
 * @see SourceFetcher 데이터 소스
 * @see InfoPacket 날씨 정보 데이터 클래스
 *
 * @constructor `SourceFetcher` 인스턴스를 내부적으로 생성합니다.
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