import 'dart:convert';
import 'package:flutter/services.dart';
import 'package:window_app/utils/CommonUtils.dart';

import '../model/ScreenItem.dart';

class MenuManager{

  static const _TAG = 'ScreenManager';
  static final MenuManager _instance = MenuManager._();

  static MenuManager get instance => _instance;
  static var isInit = false;

  // 메뉴 데이터
  Map<String, ScreenItem> mapMenus = {};


  MenuManager._(){
    if(isInit){
      return;
    }
    init();
    isInit = true;
  }


  //메뉴 인스턴스 초기화
  Future<void> init() async {
    final files = await rootBundle.loadString("assets/data/menu_info.json");

    //json to Map
    Map<String, dynamic> items = jsonDecode(files);

    //Map to List<Object>
    items.forEach((key, value) {
      final menu = ScreenItem();

      menu.screenKey = value["screenKey"] ?? "";
      menu.screenId = key;
      menu.title = value['title'];

      mapMenus[key] = menu;
    });
  }

  //메뉴 가져오기
  Future<ScreenItem?> getMenu(String screenId) async{
    //메뉴 존재 여부 확인
    if(mapMenus.isEmpty){
      await init();
    }
    //요청한 화면이 존재하는지 확인
    if(mapMenus[screenId] == null){
      CommonUtils.showToast(msg: "화면 [ $screenId ]이 없습니다");
    }

    return mapMenus[screenId];
  }
}