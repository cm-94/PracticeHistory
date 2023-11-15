import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:window_app/constants/SampleColor.dart';

enum ToastType{
  LONG,
  SHORT
}

class ToastWidget extends StatelessWidget{
  var msg = "";
  var type = ToastType.SHORT;
  var count = 0;
  var maxCount = 50;
  late Timer _timer; // late 는 변수의 초기화를 미룰 수 있게 해준다. _ 는 private
  ToastWidget({super.key, required this.msg, ToastType? type}) : type = type?? ToastType.SHORT, maxCount = type == ToastType.LONG? 50 : 30;

  void startTimer() {
    _timer = Timer.periodic(const Duration(milliseconds: 100), (timer) async {
      if (count >= maxCount) {
        _timer.cancel();
        Get.back();
      } else {
        // 상태 변경에 대해 알린다. 화면을 다시 그린다

        count++; // 카운트 1 차감
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    startTimer();
    return Scaffold(
      backgroundColor: SampleColor.transparent,
      body: SafeArea(
        child: GestureDetector(
          onTapDown: (details){
            _timer.cancel();
            Get.back();
          },
          child: Container(
            width: double.infinity,
            height: double.infinity,
            padding: EdgeInsets.only(bottom: 120),
            color: SampleColor.transparent,
            alignment: Alignment.bottomCenter,
            child: Container(
              padding: EdgeInsets.only(left: 20, top: 16, right: 20, bottom: 16),
              decoration: BoxDecoration(
                  color: SampleColor.bgToastGrey,
                  borderRadius: BorderRadius.all(Radius.circular(12))
              ),
              child: Text(
                msg,
                textAlign: TextAlign.center,
                style: TextStyle(
                    fontSize: 16,
                    fontWeight: FontWeight.bold
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}