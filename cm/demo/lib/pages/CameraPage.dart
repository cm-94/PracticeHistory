import 'package:camerademo/pages/CameraWidget.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:get/get.dart';
import 'package:permission_handler/permission_handler.dart';
import '../assets/constants.dart';
import 'CameraMainController.dart';
import 'ControlWidget.dart';
import 'TopWidget.dart';

/// //////////////////
/// 카메라 메인 화면 ///
/// //////////////////
class CameraPage extends StatefulWidget {
  @override
  _CameraPageState createState() => _CameraPageState();
}

class _CameraPageState extends State<CameraPage> {
  final CameraMainController _cameraMainController = Get.find<CameraMainController>();

  /// 상단 메뉴바
  TopWidget topWidget = TopWidget();
  CameraWidget cameraWidget = CameraWidget();
  late ControlWidget controlWidget;

  /// 촬영버튼 클릭 시 이벤트 함수
  void cameraCaptureListener(String type){
    switch(type){
      case "CAPTURE":
        cameraWidget.cameraCapture();
        break;
      case "RESET":
        cameraWidget.cameraCapture();
        break;
    }
  }

  @override
  void initState() {
    super.initState();
    controlWidget = ControlWidget(cameraCaptureListener);
    SystemChrome.setEnabledSystemUIMode(SystemUiMode.manual, overlays: [
      SystemUiOverlay.bottom
    ]);
  }

  @override
  void dispose() {
    super.dispose();
    SystemChrome.setEnabledSystemUIMode(SystemUiMode.manual, overlays: SystemUiOverlay.values);  // to re-show bars
  }

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;

    if(_cameraMainController.camMaxHeight.value == 0.0) _cameraMainController.camMaxHeight.value = size.height;

    return WillPopScope(    // <-  WillPopScope로 감싼다.
      onWillPop: () {
        setState(() {
          if(topWidget.getMenuState()){
            topWidget.closeMenu();
          }
          else if(topWidget.getCaptureState()){
            topWidget.setCaptureState(false);
          }
          else {
            // TODO : 종료 팝업 뒤
            showToast(context,"종료하시겠습니까?", size: 16);
          }
        });
        return Future(() => false);
      },
      child: Scaffold(
        backgroundColor: mCamColor,
        body: Stack(
            children: [
              /// 카메라
              Positioned(
                  top: 50, /// TODO : 카메라 상단 위치조정 필요 _cameraMainController.camMarginTop.value
                  child: cameraWidget
              ),
              /// 하단 버튼
              Positioned(
                  bottom: 0,
                  child: controlWidget
              ),
              /// 상단 및 메뉴
              Positioned(
                left: 0,
                top: 0,
                child: topWidget
              ),
            ],
          )
      ),
    );
  }

  Future<bool> checkPermission() async {
    Map<Permission, PermissionStatus> statuses = await [
      Permission.camera,
      Permission.photos,
      Permission.photosAddOnly,
      Permission.sms,
      Permission.notification
    ].request(); //여러가지 퍼미션을하고싶으면 []안에 추가하면된다. (팝업창이뜬다)

    bool per = true;

    statuses.forEach((permission, permissionStatus) {
      if (!permissionStatus.isGranted) {
        per = false; //하나라도 허용이안됐으면 false
      }
    });

    return per;
  }
}
