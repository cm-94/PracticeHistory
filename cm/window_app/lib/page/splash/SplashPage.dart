import 'package:desktop_window/desktop_window.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:get/get_core/src/get_main.dart';
import 'package:window_app/constants/SampleColor.dart';
import 'SplashPageController.dart';


class SplashPage extends StatelessWidget {
  SplashPageController controller = Get.put(SplashPageController());

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: SampleColor.bgWhite,
      body: Center(
          child : Container(
            // padding: EdgeInsets.all(400),
            child: Image.asset("assets/images/banner.webp"),
          )
      ),
    );
  }
}


