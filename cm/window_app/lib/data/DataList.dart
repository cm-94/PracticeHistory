import '../model/CompanyItem.dart';

class DataList {
  static List<CompanyItem> companys = List.generate(3, (index) {
    CompanyItem item;
    if (index == 0) {
      item = CompanyItem(companyCode: '005930', companyName: '삼성전자');
    } else if (index == 1) {
      item = CompanyItem(companyCode: '005380', companyName: '현대차');
    } else {
      item = CompanyItem(companyCode: '080220', companyName: '제주반도체');
    }
    return item;
  });
}