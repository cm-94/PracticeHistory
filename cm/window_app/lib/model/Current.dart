/// 현재가 Item
class Current {
  String code; // 코드
  String name; // 종목명
  String price; // 현재가
  String jsprice; // 전일종가
  String open; // 시가
  String high; // 고가
  String low; // 저가
  String close; // 종가
  String vol; // 거래량
  String change; // 전일대비
  String diff; // 등락률
  String sign; // 부호
  int fixed;

  Current({
    this.code = '',
    this.name = '',
    this.price = '',
    this.jsprice = '',
    this.open = '',
    this.high = '',
    this.low = '',
    this.close = '',
    this.vol = '',
    this.change = '',
    this.diff = '',
    this.sign = '',
    this.fixed = 0,
  });
}
