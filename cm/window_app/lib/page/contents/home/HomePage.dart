import 'package:flutter/material.dart';
import 'package:window_app/base/BasePage.dart';
import 'package:window_app/base/BasePageController.dart';
import 'package:window_app/constants/SampleColor.dart';
import 'HomePageController.dart';

/// 사업현황
//ignore: must_be_immutable
class HomePage extends BasePage<BasePageController>{
  HomePage({super.key});

  @override
  BasePageController getController() {
    return HomePageController();
  }

  @override
  Widget getContent() {
    HomePageController controller = ctrl as HomePageController;
    return Container(
      alignment: Alignment.center,
      width: double.infinity,
      height: 500,
      decoration: BoxDecoration(
          color: SampleColor.bgGrey,
          border: Border.all(color: SampleColor.bdBlack)
      ),
      child: Text("홈페이지", style: TextStyle(fontSize: 24),),
    );
  }

}
