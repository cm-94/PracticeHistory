import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:window_app/base/BasePage.dart';
import 'package:window_app/base/BasePageController.dart';
import 'package:window_app/constants/SampleColor.dart';
import 'package:window_app/model/CompanyItem.dart';

import '../../../constants/SampleWidget.dart';
import '../../../control/button/SampleButton.dart';
import '../../../control/dropDown/SampleDropDown.dart';
import '../../../manage/GlobalController.dart';
import '../../../model/Account.dart';
import 'MonthPageController.dart';

/// 월 보고
//ignore: must_be_immutable
class MonthPage extends BasePage<BasePageController>{
  GlobalController globalCtrl = Get.find();

  MonthPage({super.key});
  @override
  BasePageController getController() {
    return MonthPageController();
  }

  @override
  Widget getContent() {
    MonthPageController controller = ctrl as MonthPageController;
    return Container(
      alignment: Alignment.center,
      width: double.infinity,
      height: 500,
      decoration: BoxDecoration(
          color: SampleColor.bgGrey,
          border: Border.all(color: SampleColor.bdBlack)
      ),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Text("계열사 : ${globalCtrl.currComp.value.companyName}", style: TextStyle(fontSize: 24),),
          SizedBox(height: 12,),
          Text("월 보고 컨텐츠 정의 필요", style: TextStyle(fontSize: 24),)
        ],
      ),
    );
  }

}
