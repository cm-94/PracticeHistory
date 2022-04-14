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
  late Timer _timer;
  var _time = 0;

  @override
  void initState() {
    super.initState();
    _start();
  }

  @override
  Widget build(BuildContext context) {
    double width = (MediaQuery.of(context).size.width) / 2;

    return Scaffold(
      body: SafeArea(
        child: Container(
          padding: EdgeInsets.only(bottom: width),
          color: mIntroColor,
          child: Center(
            child: SizedBox(
              width: width,
              child: const Text("인트로 화면"),
            ),
          ),
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

  void _start() {
    _timer = Timer.periodic(Duration(seconds: 1), (timer) {
      setState(() {
        _time++;
        if(_time == 3){
          _timer?.cancel();
          Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) {
            return CameraPage();
          }));
        }
      });
    });
  }
}