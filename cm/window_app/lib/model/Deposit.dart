/// 예수금
class Deposit {
  String depositAmount; //예탁금액,
  String profit; //추정익일손익
  String evaluationAmount; //평가예탁금액
  String orderAmount; //주문가능 금액
  String withdrawalAmount; //인출가능 금액
  String entrustmentMargin; //위탁증거금
  String maintenanceMargin; //유지증거금
  String fees; //수수료
  List<RepeatData> items = [];

  Deposit({
    this.depositAmount = '',
    this.profit = '',
    this.evaluationAmount = '',
    this.orderAmount = '',
    this.withdrawalAmount = '',
    this.entrustmentMargin = '',
    this.maintenanceMargin = '',
    this.fees = '',
    List<RepeatData>? items,
  }) : items = items == null ? [] : items;
}

/// 예수금 Item 반복데이터
class RepeatData {
  String title; // 타이틀
  String value; // 값

  RepeatData({this.title = '', this.value = ''});
}
