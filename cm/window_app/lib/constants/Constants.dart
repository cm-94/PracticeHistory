import 'package:flutter/foundation.dart';

class Constants {
  Constants._();

  /// 프로젝트명
  static const String MTS_PRPJECT_NAME = 'SsinPractice';

  /// 프로젝트 타입
  static const String MTS_PRPJECT_TYPE = kDebugMode? "개발": "운영"; // 개발 & 운영

  /// 윈도우창 둥글기
  static const double WINDOW_BORDER_RADIUS = 7.0;

  /// 화면명
  static const String PAGE_BUSINESS = 'BusinessPage'; // 사업현황
  static const String PAGE_MONTH = 'MonthPage';       // 월 보고
  static const String PAGE_RESOLUTE = 'ResolutePage';   // 결의사항
  static const String PAGE_PAGER = 'PagerPage';       // 제반문서

  /// 화면 ID
  static const String PAGE_HOME_CD = '0000';     // 홈페이지
  static const String PAGE_BUSINESS_CD = '1000'; // 바이포엠 스튜디오
  static const String PAGE_MONTH_CD = '2000';    // 디엔비 아이엔씨
  static const String PAGE_RESOLUTE_CD = '3000'; // 메이저 나인
  static const String PAGE_PAGER_CD = '4000';    // 바이포엠 에프앤티

  /// 회사 목록
  static const String COMP_CODE_ALL = '0000';     // 전체
  static const String COMP_CODE_BY4M = '1000';    // 바이포엠 스튜디오
  static const String COMP_CODE_DNBINC = '2000';  // 디엔비 아이엔씨
  static const String COMP_CODE_MAJ9 = '3000';    // 메이저 나인
  static const String COMP_CODE_BY4MFNT = '4000'; // 바이포엠 에프앤티
  static const String COMP_CODE_NATURE = '5000';  // 네이처 라우드
  static const String COMP_CODE_THE = '6000';     // 더궈
  static const String COMP_CODE_MOVING = '7000';  // 무빙 픽쳐스
}