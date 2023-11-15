import 'package:get/get.dart';
import 'package:window_app/manage/GlobalController.dart';
import 'package:window_app/model/CompanyItem.dart';

import '../contents/ContentPageController.dart';

class NaviBarController extends GetxController {
  var currNavi = "".obs;

  GlobalController globalCtrl = Get.find();

  movePage(CompanyItem item, String pageCd){
    if(globalCtrl.currComp.value.companyCode != item.companyCode){
      globalCtrl.currComp.value = item;
    }
    /// 현재 페이지 -> 새 페이지 이동
    if(globalCtrl.currPageCd.value != pageCd){
      globalCtrl.currPageCd.value = pageCd;
      ContentPageController.instance.movePage(pageCd);
    }
  }
}