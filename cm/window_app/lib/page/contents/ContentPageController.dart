import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:window_app/base/BasePageController.dart';
import 'package:window_app/manage/GlobalController.dart';
import 'package:window_app/router/PageRouter.dart';
import '../../base/BasePage.dart';
import 'package:window_app/model/ScreenItem.dart';
import 'package:window_app/manager/MenuManager.dart';

import '../../constants/Constants.dart';
import 'home/HomePage.dart';

class ContentPageController{
  var currPage = (HomePage() as BasePage).obs;
  ///싱글톤 구현
  static final ContentPageController _instance = ContentPageController._();
  static ContentPageController get instance => _instance;

  ContentPageController._(){
    print("MdiController is init");
  }

  void movePage(String screenId) async{
    GlobalController globalCtrl = Get.find();
    globalCtrl.currPageCd.value = screenId;
    if(screenId == Constants.PAGE_HOME_CD){
      globalCtrl.currComp.value = globalCtrl.arrCompany[0];
    }

    //// todo 예외처리 추가하기
    ScreenItem screenItem = MenuManager.instance.mapMenus[screenId]!;
    var pageWidget = PageRouter.getPage(screenItem.screenKey);
    var page = pageWidget.call() as BasePage;
    //윈도우 창이름 지정
    page.ctrl.setItem(screenItem);

    /// 기존 페이지 삭제
    var isDeleted = await Get.delete<BasePageController>(tag: currPage.value.pageKey.toString());
    if(isDeleted){
      currPage.value = page;
    }
  }
}