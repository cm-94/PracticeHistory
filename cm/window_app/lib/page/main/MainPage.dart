import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:get/get_state_manager/src/rx_flutter/rx_obx_widget.dart';
import 'package:window_app/constants/SampleColor.dart';
import 'package:window_app/page/main/BottomBar.dart';
import 'package:window_app/page/main/TopMenuBar.dart';
import 'package:window_app/utils/CommonUtils.dart';

import '../../manage/GlobalController.dart';
import '../contents/ContentPage.dart';
import '../navi/NaviBar.dart';

class MainPage extends StatelessWidget {
  GlobalController globalCtrl = Get.find();
  GlobalKey<ScaffoldState> _scaffoldKey = new GlobalKey<ScaffoldState>();
  //late MdiController mdiController = MdiController();

  FloatingActionButton extendButton() {
    return FloatingActionButton.extended(
      onPressed: () {
        /// TODO : 콘텐츠 파이프라인 화면 이동
        CommonUtils.showToast(msg: "콘텐츠 파이프라인 화면은 준비중입니다.");
      },
      label: const Text("콘텐츠 파이프라인", style: TextStyle(fontSize: 20),),
      icon: const Icon(
        Icons.check,
        size: 30,
      ),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),

      /// 텍스트 컬러
      foregroundColor: Colors.white,
      backgroundColor: Colors.red,
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      key: _scaffoldKey,
      drawer: NaviBar(scaffoldKey: _scaffoldKey),
      appBar: AppBar(
        backgroundColor: SampleColor.bgPrimary,
        toolbarHeight:44,
        title: TopMenuBar(),
        toolbarOpacity: 1.0,
        leading: IconButton(
          icon: const Icon(Icons.menu),
          onPressed: () => _scaffoldKey.currentState?.openDrawer(),
        ),
      ),
      backgroundColor: SampleColor.bgWhite,
      body: SafeArea(
        child: Column(
          children: [
            ContentPage(),
            BottomBar()
          ],
        ),
      ),
      floatingActionButton: Obx((){
        if(globalCtrl.isFloatShow.value){
          return SizedBox(
            height: 54,
            width: 240,
            child: extendButton(),
          );
        }
        else return SizedBox();
      }),
      floatingActionButtonLocation: FloatingActionButtonLocation.endFloat,
    );
  }
}