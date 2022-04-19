import 'dart:io';
import 'package:flutter/cupertino.dart';
import 'package:camerademo/pages/CameraMainController.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:permission_handler/permission_handler.dart';

import '../assets/constants.dart';
import '../utils/LOGCAT.dart';
import 'MenuPopup.dart';

/// //////////////////
/// 카메라 상단 위젯 ///
/// //////////////////
class TopWidget extends StatefulWidget {
  /// 상태
  final _TopWidgetState _state = _TopWidgetState();

  @override
  _TopWidgetState createState() => _state;

  bool getMenuState(){
    return _state._controller.getMenuState();
  }

  void closeMenu(){
    _state._controller.setMenuState(false);
  }

  bool getCaptureState(){
    return _state._controller.isCapture.value;
  }

  void setCaptureState(bool state){
    _state._controller.setCaptureState(state);
  }


}

class _TopWidgetState extends State<TopWidget> {
  CameraMainController _controller = Get.find<CameraMainController>();
  MenuPopup menuPopup = MenuPopup();

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    // double width = (MediaQuery.of(context).size.width) / 2;
    return Container(
      width: size.width,
      child: Stack(
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              /// <- 버튼
              Obx((){
                return Container(
                    child: InkWell(
                        child: ClipRRect(
                          borderRadius: BorderRadius.all(Radius.circular(5.0)),
                          child: Container(
                              child: IconButton(
                                onPressed: () async {
                                  if(_controller.isCapture.value){
                                    _controller.setCaptureState(false);
                                  }
                                },
                                icon: Icon(
                                  Icons.arrow_back,
                                  color: _controller.isCapture.value? Colors.white : Colors.transparent,
                                  size: 20.0,
                                ),
                              )
                          ),
                        ),
                        onTap: () {
                          _controller.isCapture(!_controller.isCapture.value);
                          // setChartType();
                        }
                    )
                );
              }),
              /// 타이틀
              Obx((){
                return Text((_controller.isCapture.value)? '편집' : '촬영', style: TextStyle(color: mColorWhite, fontSize: 20));
              }),
              /// 메뉴 버튼
              Obx((){
                return Container(
                    child: InkWell(
                        child: ClipRRect(
                          child: Container(
                              child: IconButton(
                                onPressed: () async {
                                  ///
                                  if(!_controller.isCapture.value){
                                    _controller.setMenuState(!_controller.isMenu.value);
                                  }
                                },
                                icon: Icon(
                                  Icons.menu,
                                  color: !_controller.isCapture.value? Colors.white : Colors.transparent,
                                  size: 20.0,
                                ),
                              )
                          ),
                        ),
                        onTap: () {
                          _controller.isCapture(!_controller.isCapture.value);
                          // setChartType();
                        }
                    )
                );
              })
            ],
          ),
          Obx((){
            if(_controller.getMenuState()){
              return menuPopup;
            }else {
              return SizedBox(width: 0, height: 0,);
            }
          })
        ],
      ),
    );
  }
}
