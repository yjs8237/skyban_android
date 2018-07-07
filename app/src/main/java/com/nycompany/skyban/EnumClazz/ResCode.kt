package com.nycompany.skyban.EnumClazz

enum class ResCode(val Code:String) {
    Success("0"),
    TransmissionError("-101"),
}

fun codeToStr(Code: String?): String {
    return when (Code) {
        "-101" -> "전송 데이터오류"
        "-201" -> "회원 미존재"
        "-202" -> "이미 가입되어 있는 회원"
        "-203" -> "추천인 회원 미존재"
        "-204" -> "비밀번호 오류"
        "-301" -> "QNA 항목 미존재"
        "-302" -> "이미 삭제된 발주"
        "-303" -> "이미 매칭완료된 발주건"
        "-304" -> "발주 데이터없음"
        "-305" -> "수주자 없음"
        "-306" -> "수주자 정보 다름"
        "-307" -> "수주 매칭 진행중"
        "-308" -> "본인의 발주를 본인이 수주할 수 없음"
        "-309" -> "캐쉬부족으로 유료발주 수주불가"
        "-310" -> "유료회원이 아니므로 유료발주 수주불가"
        "-311" -> "잘못된 work_proc 데이터"
        "-312" -> "이미 작업 완료된 발주"
        "-313" -> "포인트 적립내용 없음"
        "-314" -> "캐쉬충전 요청 없음"
        "-315" -> "QNA 문의 내역 없음"
        "-316" -> "캐쉬부족으로 유료회원신청불가"
        "-401" -> "공지사항없음"
        else -> "Invalid color param value"
    //else -> throw IllegalArgumentException("Invalid color param value")
    }
}
