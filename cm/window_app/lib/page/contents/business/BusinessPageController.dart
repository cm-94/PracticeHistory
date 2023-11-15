import 'package:get/get.dart';
import 'package:window_app/base/BasePageController.dart';

class BusinessPageController extends BasePageController<BusinessPageController>{
  static final TAG = "BusinessPageController";

  var dropdownList = ['인사', '총무', '회계'].obs; // 사업부 Combo Data
  var selectedDropdown = '인사'.obs; // 선택한 사업부

  @override
  String getControllerTag() {
    return TAG;
  }
}