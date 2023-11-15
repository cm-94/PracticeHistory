/// 마스터
class Master {
  /// '0':'KOSPI',
  /// '1':'KOSDAQ',
  /// 'n':'ELW',
  /// '2':'지수선물',
  /// 'd':'지수선물 스프레드',
  /// '3':'지수옵션',
  /// 'U':'지수업종',
  /// 'g':'K-OTC',
  /// 'f':'미니지수선물',
  /// 'o':'미니지수옵션',
  /// 'L':'변동성지수선물',
  /// 'K':'tprxjwltntjsanf',
  /// 'S':'주식선물',
  /// 'C':'상품선물',
  /// 'N':'KONEX'
  String marketType; // 마켓구분코드
  String code; // 종목코드
  String korName; // 한글명

  Master(this.marketType, this.code, this.korName);
}
