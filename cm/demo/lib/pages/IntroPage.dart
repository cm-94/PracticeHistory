import 'dart:async';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';

import '../assets/constants.dart';
import 'CameraPage.dart';

/// //////////////
/// 인트로 화면 ///
/// //////////////
class IntroPage extends StatefulWidget {
  @override
  IntroPageState createState() => IntroPageState();
}

class IntroPageState extends State<IntroPage> {
  late CameraPage cameraPage;

  @override
  void initState() {
    super.initState();

  }

  @override
  Widget build(BuildContext context) {
    double width = (MediaQuery.of(context).size.width);

    return Align(
      alignment: Alignment.center,
      child: Center(
        child: SizedBox(
          width: width,
          child: Image.asset('lib/assets/images/app_icon.png', width: 240,height: 240,),
        ),
      ),
    );
  }

  /// 퍼미션 체크
  permissionCheck() async {
    // Get.to(() => CameraPage()); //페이지 이동
    // Get.to(CameraPage());
    // Get.offAll(CameraPage()); //모든 정보를 지우고 페이지를 이동할 때 사용
  }
}