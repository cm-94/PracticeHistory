import 'package:get/get.dart';
import 'package:window_app/manage/GlobalController.dart';

class LoginPageController extends GetxController {

  void rqLogin(String id, String pwd) async {
    if(id.isNotEmpty && pwd.isNotEmpty){

      GlobalController globalCtrl = Get.find();
      await globalCtrl.initData();
      /// TODO : RQ Login
      globalCtrl.setUserInfo(id, pwd);
      Get.offAllNamed('/mainPage');
    }
  }
}
