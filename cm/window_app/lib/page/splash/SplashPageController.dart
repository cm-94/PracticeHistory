import 'dart:async';

import 'package:get/get.dart';


class SplashPageController extends GetxController {
  //int count = 10 ->
  var count =
      0.obs; //obs를 붙여서 int count = 10 이 이렇게 변했음 -> 뒤에 변수 값으로 얘네가 알아서 타입 값을 정함,
  String type = ""; // null 값 허용 안함
  late Timer _timer; // late 는 변수의 초기화를 미룰 수 있게 해준다. _ 는 private
  var isStart = false.obs; // 타이머 시작 여부

  @override
  void onInit() {
    super.onInit();
    startTimer();
  }

  void startTimer() {
    if (!isStart.value) {
      isStart.value = true;
      _timer = Timer.periodic(const Duration(milliseconds: 100), (timer) async {
        if (count >= 50) {
          _timer.cancel();

          Get.offAllNamed('/loginPage');
        } else {
          // 상태 변경에 대해 알린다. 화면을 다시 그린다

          count.value++; // 카운트 1 차감
        }
      });
    }
  }
}
