import 'dart:convert';

import 'package:flutter/services.dart';
import 'package:get/get.dart';

import '../constants/Constants.dart';
import '../model/CompanyItem.dart';
import '../page/contents/ContentPageController.dart';

class GlobalController extends GetxController {
  static const TAG = 'GlobalController';

  var isFloatShow = true.obs;                                 /// 현재 페이지 초기화

  var currPageCd = Constants.PAGE_HOME_CD.obs;                                 /// 현재 페이지 초기화
  var currComp = CompanyItem(companyCode : "0000", companyName : "전체").obs; /// 회사 데이터 초기화
  var _userId = "";  /// 사용자 ID
  var _userPwd = ""; /// 사용자 PWD

  List<CompanyItem> arrCompany = [];

  @override
  void onInit() {
    super.onInit();
  }

  initData() async {
    if(arrCompany.isEmpty){
      final files = await rootBundle.loadString("assets/data/company_info.json");

      //json to Map
      List<Map<String, dynamic>> items = (jsonDecode(files) as List<dynamic>).cast<Map<String, dynamic>>();

      for(var item in items){
        final company = CompanyItem();

        company.companyCode = item["compCd"] ?? "";
        company.companyName = item["compNm"] ?? "";
        arrCompany.add(company);
      }
    }
    currPageCd = Constants.PAGE_HOME_CD.obs;
    currComp = CompanyItem(companyCode : "0000", companyName : "전체").obs;
  }



  setUserInfo(String id, String pwd){
    _userId = id;
    _userPwd = pwd;
  }

  String getUserId(){
    return _userId;
  }
}