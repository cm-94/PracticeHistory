import 'package:get/get.dart';
import '../comm/CameraOption.dart';

class TopWidgetController extends GetxController {
  var menuState = CameraOption.convertOptionString(MENU_TYPE.START).obs; // TODO : 기본 설정값 호출 후 사용


}
