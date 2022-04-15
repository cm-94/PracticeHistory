import 'package:get/get.dart';
import '../comm/CameraOption.dart';

class CameraPageController extends GetxController {
  var defaultMode = "";
  var camWidth = 1.obs;
  var camHeight = 1.obs;

  void init() {
    defaultMode = CameraOption.convertOptionString(CAMERA_TYPE.CAMERA); // TODO : 기본 설정값 호출 후 사용

  }
}


