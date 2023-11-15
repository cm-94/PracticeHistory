import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:window_app/base/BasePage.dart';
import 'package:window_app/base/BasePageController.dart';
import 'package:window_app/constants/SampleColor.dart';
import '../../../manage/GlobalController.dart';
import 'PaperPageController.dart';

/// 제반문서
//ignore: must_be_immutable
class PaperPage extends BasePage<BasePageController>{
  GlobalController globalCtrl = Get.find();

  PaperPage({super.key});

  @override
  BasePageController getController() {
    return PaperPageController();
  }

  @override
  Widget getContent() {
    PaperPageController controller = ctrl as PaperPageController;
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
          Text("제반문서 컨텐츠 정의 필요", style: TextStyle(fontSize: 24),)
        ],
      ),
    );
  }

}
