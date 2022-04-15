import 'dart:io';

import 'package:animated_splash_screen/animated_splash_screen.dart';
import 'package:camerademo/pages/CameraPage.dart';
import 'package:camerademo/pages/IntroPage.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:splashscreen/splashscreen.dart';
import 'assets/constants.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    // checkPermission().then((val) {
    //   if(!val){ /// 카메라 권한 X
    //     exit(0); /// TODO : 앱 종료
    //   }
    // }).catchError((error) {
    //   exit(0); /// 앱 종료
    // });
    return GetMaterialApp(
      title: 'One Shot',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: AnimatedSplashScreen(
        splash: Image.asset('lib/assets/images/app_icon.png'),
        duration: 3000,
        backgroundColor: mCamColor,
        splashTransition: SplashTransition.scaleTransition,
        nextScreen: CameraPage(),
      ),
      debugShowCheckedModeBanner: false,
    );
  }

  Future<bool> checkPermission() async{
    Map<Permission, PermissionStatus> statuses =
    await[Permission.camera, Permission.photos, Permission.photosAddOnly, Permission.sms, Permission.notification].request(); //여러가지 퍼미션을하고싶으면 []안에 추가하면된다. (팝업창이뜬다)

    bool per= true;

    statuses.forEach((permission, permissionStatus){
      if(!permissionStatus.isGranted){
        per = false; //하나라도 허용이안됐으면 false
      }
    });

    return per;
  }
}