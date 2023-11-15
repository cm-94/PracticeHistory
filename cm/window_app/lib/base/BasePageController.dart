import 'package:flutter/material.dart';
import 'package:get/get.dart';

import '../model/CompanyItem.dart';
import '../model/ScreenItem.dart';
import '../page/contents/ContentPageController.dart';

abstract class BasePageController<T> extends GetxController {
  static const _TAG = 'BasePageController';
  late String className;
  var isForeground = false;

  var isHistory = false.obs;
  List<CompanyItem> companys = List.generate(3, (index) {
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
  var company = CompanyItem(companyCode: '005930', companyName: '삼성전자').obs;

  late ScreenItem screenItem;

  // 상태창 높이
  var headerSize = 30.0;
  // 상단 (헤더) 드래그
  late Function(double, double) onWindowDragged;
  // 상단 X 버튼 클릭이벤트
  late VoidCallback onCloseButtonClicked;


  // 앱 종료
  static const snackBarDuration = Duration(seconds: 2);
  DateTime? backButtonPressTime;

  void setItem(ScreenItem item){
    this.screenItem = item;
  }

  @override
  void onInit() {
    super.onInit();
    className = T.toString();
    print("LifeCycle[onInit] - $className");
  }

  @override
  void onReady() {
    super.onReady();
    print("LifeCycle[onReady] - $className");
  }


  @override
  void dispose() {
    super.dispose();
    print("LifeCycle[dispose] - $className");
  }


  void onResume() {
    isForeground = true;
    print("LifeCycle[onResume] - $className");
  }

  void onPause() {
    print("LifeCycle[onPause] - $className");
  }

  void onForeground() {
    print("LifeCycle[onForeground] - $className");
    isForeground = true;
  }

  void onBackground() {
    print("LifeCycle[onBackground] - $className");
    isForeground = false;
  }

  Future<bool> onBackPressed() async {
    print("LifeCycle[onBackPressed] - $className");
    return true;
  }

  String getControllerTag();


}