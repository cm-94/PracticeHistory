import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:window_app/base/BasePage.dart';
import 'package:window_app/base/BasePageController.dart';
import 'package:window_app/constants/SampleColor.dart';
import 'package:window_app/manage/GlobalController.dart';
import 'package:window_app/model/CompanyItem.dart';

import '../../../constants/SampleWidget.dart';
import '../../../control/button/SampleButton.dart';
import '../../../control/dropDown/SampleDropDown.dart';
import '../../../model/Account.dart';
import '../../../model/JmCode.dart';
import 'BusinessPageController.dart';

/// 사업현황
//ignore: must_be_immutable
class BusinessPage extends BasePage<BasePageController>{
  BusinessPage({super.key});

  GlobalController globalCtrl = Get.find();

  @override
  BasePageController getController() {
    return BusinessPageController();
  }

  @override
  Widget getContent() {
    BusinessPageController busiCtrl = ctrl as BusinessPageController;
    return Column(
      children: [
        Container(
          width: double.infinity,
          height: 60,
          child: getTopMenuSearch(comboList: busiCtrl.dropdownList, selectedData: busiCtrl.selectedDropdown, onSearchClick: (value){
            /// TODO : 검색 시작
          }),
        ),
        Expanded(
          child: Container(
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
                Text("사업현황 컨텐츠 정의 필요", style: TextStyle(fontSize: 24),)
              ],
            ),
          ),
        ),
      ],
    );
  }

}
