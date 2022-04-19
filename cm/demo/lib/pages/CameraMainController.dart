import 'dart:io';
import 'dart:ui';

import 'package:get/get.dart';
import '../assets/constants.dart';
import '../comm/CameraOption.dart';

class CameraMainController extends GetxController {
  var cameraMode = "".obs; // 촬영 , 편집 모드

  var camWidth = 0.0.obs;
  var camHeight = 0.0.obs;
  var camMaxHeight = 0.0.obs;
  var camMarginTop = 0.0.obs;
  var camRatio = "".obs;

  var isCapture = false.obs; /// true : 촬영완료, false : 촬영대기
  var isMenu = false.obs;    /// 메뉴 활성화 여부
  var filterType = "".obs;   /// 필터 카테고리 타입
  File? captureImage;        /// 촬영 이미지

  bool getMenuState() {
    return isMenu.value;
  }

  void setMenuState(bool state) {
    isMenu.value = state;
  }

  void setCaptureState(bool isCap){
    isCapture.value = isCap;
    if(isCap){
      cameraMode.value = CameraOption.convertOptionString(MENU_TYPE.CAPTURE);
    }else{
      cameraMode.value = CameraOption.convertOptionString(MENU_TYPE.START);
    }
  }

  bool getCaptureState(){
    return isCapture.value;
  }

  void setFilter(int idx){
    filterType.value = filterList[idx].filterCode;
  }

  void setRatio(SIZE_TYPE ratio, Size size){
    camRatio.value = CameraOption.convertOptionString(ratio);

    camWidth.value = size.width;
    switch(camRatio.value){
      case CameraOption.SIZE_DEFAULT:
        camHeight.value = size.width;
        camMarginTop.value = 50;
        break;
      case CameraOption.SIZE_THREETOFOUR:
        camHeight.value = size.width / 3 * 4;
        camMarginTop.value = 50;
        break;
      case CameraOption.SIZE_NINETOSIXTEEN:
        camHeight.value = size.width / 9 * 16;
        camMarginTop.value = 50;
        break;
      case CameraOption.SIZE_FULL:
        camHeight.value = camMaxHeight.value;
        camMarginTop.value = 50;
        break;
    }
    print("new_camHeight ==> " + camHeight.value.toString());
  }

  String getRatio(){
    return camRatio.value;
  }

  void init() {
    cameraMode.value = CameraOption.convertOptionString(CAMERA_TYPE.CAMERA); // TODO : 기본 설정값 호출 후 사용
    camMarginTop.value = 0.0;
    isCapture.value = false;
    filterType.value = filterList[0].filterCode;
    camRatio.value = CameraOption.convertOptionString(SIZE_TYPE.NINETOSIXTEEN);
  }
}


