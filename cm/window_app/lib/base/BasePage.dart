import 'package:flutter/material.dart';
import 'package:get/get.dart';
import '../page/contents/ContentPageController.dart';
import 'BasePageController.dart';
import 'package:window_app/constants/Constants.dart';
import 'package:window_app/constants/SampleColor.dart';

// ignore: must_be_immutable
abstract class BasePage<T> extends StatelessWidget {
  Key pageKey = GlobalKey();

  late BasePageController ctrl;

  BasePage({super.key, T? controller}) {
    if(controller != null){
      // ctrl = controller as BasePageController;
      ctrl = Get.put(controller as BasePageController, tag: pageKey.toString());
    }
    else{
      ctrl = Get.put(getController(), tag: pageKey.toString());
    }
  }

  Key getPageKey(){
    return pageKey;
  }
  BasePageController getController();

  @override
  Widget build(BuildContext context) {
    print("::::::::::::::::: ${ctrl.className} Page Build :::::::::::::::::");
    return Expanded(
      child: Container(
        width: double.infinity,
        padding: EdgeInsets.all(20),
        child: getContent(),
      )
    );
  }
  ///윈도우창의 상단위젯 반환
  _getHeader() {
    // return GestureDetector(
    //   behavior: HitTestBehavior.translucent,
    //   child: Container(
    //     width: double.infinity,
    //     height: ctrl.headerSize,
    //     color: SampleColor.bgHeader,
    //     child: Stack(
    //       children: [
    //         Positioned(
    //           right: 10,
    //           top: 0,
    //           bottom: 0,
    //           child: GestureDetector(
    //               onTap: (){
    //                 ctrl.onCloseButtonClicked();
    //               },
    //               child: const Icon(Icons.close,color: Colors.red,)
    //           ),
    //         ),
    //         Positioned(
    //           left:5, top:0, bottom:0, right:0,
    //           child:
    //             Align(
    //                 alignment:Alignment.centerLeft,
    //                 child: Text(" ${ctrl.screenItem.title}", style:TextStyle(color: Colors.white, fontWeight: FontWeight.w400)))),
    //       ],
    //     ),
    //   ),
    // );
  }

  ///body영역의 Container의 child 구현하는 메서드
  ///함수에서 반환되는 위젯은 Container로 감싸짐
  Widget getContent();
}
