class JmData {
  String jmCode;
  String name;
  late List<Map<String, dynamic>> data;


  JmData({required this.jmCode, required this.name, required this.data});

  Map<String, dynamic> getDataForCompany(int day) {

    var lastData = data.last["currentprice"]; //오늘 금액

    //시가총액(거래량)
    double totalStock = data.last["marketcapitalization"];
    double totalStockInBillions = totalStock / 1e8;
    var todaymarket = double.parse(totalStockInBillions.toStringAsFixed(2));

    //하기 부터는 종목검색에서 필요한 데이터 구하는 것.

    //n일전
    var nData = data[data.length - day - 1]["currentprice"] as num;

    //등락가
    var diff = lastData - nData;
    //등락률
    var diffPercent = (diff/ lastData) * 100;


    Map<String, dynamic> companyData = {
      "code" : jmCode,
      "name": name,
      "PriceChange": diff,
      "PriceChangePercent": diffPercent,
      "currentPrice": data.last["currentprice"],
      "TotalStock": todaymarket,
    };

    return companyData;
  }

  factory JmData.fromJson(Map<String, dynamic> json) {
    return JmData(
      jmCode: json['jmCode'] as String,
      name : json['name'] as String,
      data: (json['data'] as List<dynamic>).cast<Map<String, dynamic>>(),
    );
  }
}
