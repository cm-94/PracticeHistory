import 'package:get/get.dart';
import '../comm/CameraOption.dart';

class CameraMainController extends GetxController {
  var cameraMode = "".obs;
  var camWidth = 1.obs;
  var camHeight = 1.obs;
  var isCapture = false.obs;
  var isMenu = false.obs;

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

  void init() {
    cameraMode.value = CameraOption.convertOptionString(CAMERA_TYPE.CAMERA); // TODO : 기본 설정값 호출 후 사용
    isCapture.value = false;

  }
}


