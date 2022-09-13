import 'package:camerademo/pages/CameraWidget.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:get/get.dart';
import 'package:permission_handler/permission_handler.dart';
import '../assets/constants.dart';
import '../comm/CameraOption.dart';
import 'CameraMainController.dart';
import 'ControlWidget.dart';
import 'SelectFilterController.dart';
import 'SelectFilterPage.dart';
import 'TopWidget.dart';

/// //////////////////
/// 카메라 메인 화면 ///
/// //////////////////
class CameraPage extends StatefulWidget {
  @override
  _CameraPageState createState() => _CameraPageState();
}

class _CameraPageState extends State<CameraPage> {
  /// 카메라 뷰 컨트롤러
  final CameraMainController _cameraMainController = Get.find<CameraMainController>();
  /// 필터 뷰 컨트롤러
  final SelectFilterController _selectFilterController = Get.find<SelectFilterController>();

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
      case "SELECT":
        cameraWidget.saveImage();
        break;
      case "FILP":
        cameraWidget.flipCamera();
        break;
    }
  }

  @override
  void initState() {
    super.initState();
    controlWidget = ControlWidget(cameraCaptureListener);
    // SystemChrome.setEnabledSystemUIMode(SystemUiMode.manual, overlays: [
    //   SystemUiOverlay.bottom
    // ]);
  }



  @override
  void dispose() {
    super.dispose();
    // SystemChrome.setEnabledSystemUIMode(SystemUiMode.manual, overlays: SystemUiOverlay.values);  // to re-show bars
  }

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;

    if(_cameraMainController.camMaxHeight.value == 0.0) {
      _cameraMainController.camMaxHeight.value = size.height;
      _cameraMainController.setRatio(CameraOption().getSizeOption(CameraOption.SIZE_DEFAULT), size);
    }

    return WillPopScope(
      onWillPop: () {
        setState(() {
          /// 1. 메뉴 닫기
          if(topWidget.getMenuState()){
            topWidget.closeMenu();
          }
          /// 2. 필터 닫기
          else if(_selectFilterController.getFilterType() != CameraOption.FILTER_DEFAULT){
            _selectFilterController.setFilterType(0);
          }
          /// TODO : 3. 팝업 닫기
          /// 4. 편집모드 -> 촬영모드
          else if(topWidget.getCaptureState()){
            _selectFilterController.setFilterType(0);
          }
          /// 앱 종료 팝업
          else {
            // TODO : 종료 팝업 뒤
            showMessageBox(context, "종료하시겠습니까?", MSG_TYPE.EXIT);
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
              Positioned(
                left: 0,
                bottom: 152,
                child: SelectFilterPage(),
              )
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

  // @override
  // Widget build(BuildContext context) {
  //   return Scaffold(
  //     body: AnimatedSwitcher(
  //       duration: const Duration(milliseconds: 300),
  //       child: loading ? Container(
  //         key: Key("loading"),
  //         color: Theme.of(context).scaffoldBackgroundColor,
  //         child: Center(
  //           child: SizedBox(
  //             width: 24,
  //             height: 24,
  //             child: GestureDetector(
  //               onTap: _toggle,
  //               child: const CircularProgressIndicator(),
  //             ),
  //           ),
  //         ),
  //       ) : Container(
  //         key: Key("normal"),
  //         child: Center(
  //           child: GestureDetector(
  //             onTap: _toggle,
  //             child: const Text("WELCOME"),
  //           ),
  //         ),
  //       ),
  //     ),
  //   );
  // }
  //
  // _toggle() {
  //   setState(() {
  //     loading = !loading;
  //   });
  // }
}
