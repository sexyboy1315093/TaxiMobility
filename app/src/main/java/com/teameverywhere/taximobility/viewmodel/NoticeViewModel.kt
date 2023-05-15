package com.teameverywhere.taximobility.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teameverywhere.taximobility.model.Car
import com.teameverywhere.taximobility.model.Notice
import kotlinx.coroutines.*

class NoticeViewModel: ViewModel() {

    var job: Job? = null

    val noticeArray = MutableLiveData<ArrayList<Notice>>()
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError(throwable.localizedMessage)
    }

    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String?>()

    init {
        getNotice()
    }

    private fun getNotice(){
        loading.value = true

        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val noticeArr = arrayListOf<Notice>()

            val notice = Notice("[공지] 택시 요금인상", "2023-02-01", "이렇게 기본요금이 오른 건 2019년 2월 이후 4년 만입니다. 요금 인상과 동시에 거리에 따라 요금이 올라가는 미터기 속도도 더욱 빨라졌습니다. 기본거리가 2㎞에서 1.6㎞로 400ｍ 줄었습니다. 이에 따라 거리 요금은 132ｍ당 100원에서 131ｍ당 100원으로 조정됐습니다.")
            val notice1 = Notice("[공지] 택시 안전수칙", "2022-08-12", "하루에 12시간 이상 운전하지 않도록 하고, 야간근무를 마친 후 최소 24시간 이상 휴식을 취한다. 규칙적인 식사와 근무 중 적절한 휴식(휴식 시 수시로 스트레칭 실시)을 취한다. 운행 전·중에 약(감기약 등) 복용, 음주 등을 하지 않는다.")
            val notice2 = Notice("[공지] 택시 분실물", "2021-11-23", "택시회사 분실물센터에서 보관하고 있는 분실물은 주인이 나타나지 않을 경우 7일 이후 경찰서에 제출한다. 7일이 경과한 분실물이라면 경찰청 유실물 통합포털(www.lost112.go.kr)에서 분실물을 검색해 찾을 수 있다.")

            noticeArr.add(notice)
            noticeArr.add(notice1)
            noticeArr.add(notice2)

            withContext(Dispatchers.Main){
                noticeArray.value = noticeArr
                error.value = null
                loading.value = false
            }
        }
    }

    private fun onError(message: String){
        error.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}