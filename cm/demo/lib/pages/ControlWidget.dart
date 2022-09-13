import 'dart:io';

import 'package:camerademo/comm/CameraOption.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:image_picker/image_picker.dart';
import 'package:intl/intl.dart';
import '../assets/constants.dart';
import '../utils/LOGCAT.dart';
import 'CameraMainController.dart';
import 'package:scroll_snap_list/scroll_snap_list.dart';

import 'SelectFilterController.dart';

typedef CameraControlCallback = void Function(String type);

class ControlWidget extends StatelessWidget {
  CameraControlCallback captureCallback; // 버튼 클릭 시 이벤트 콜백
  /// 카메라 메인 컨트롤러
  final CameraMainController _cameraMainController = Get.find<CameraMainController>();
  /// 필터 뷰 컨트롤러
  final SelectFilterController _selectFilterController = Get.find<SelectFilterController>();


  ControlWidget(this.captureCallback);

  _getFromGallery() async {
    PickedFile? pickedFile = await ImagePicker().getImage(
      source: ImageSource.gallery,
      maxWidth: 1800,
      maxHeight: 1800,
    );
    if (pickedFile != null) {
      _cameraMainController.captureImage = File(pickedFile.path);
      _cameraMainController.setCaptureState(true);
    }
  }

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;

    return Visibility(
      visible: !_cameraMainController.getMenuState(),
      child: Container(
        color: mControlColor,
        width: size.width,
        height: 152,
        child: Stack(
          children: [
            /// 하단 버튼 영역
            Container(
              alignment: Alignment.center,
              padding: const EdgeInsets.fromLTRB(48.0, 40, 48.0, 0.0),
              child: Stack(
                alignment: Alignment.center,
                children: [
                  Align(
                      alignment: Alignment.centerLeft,
                      child: Obx((){
                        /// 편집 모드
                        if(_cameraMainController.getCaptureState()){
                          return IconButton(
                            onPressed: () async {
                              /// TODO : 저장 로직 추가 필요
                              captureCallback('SELECT');
                            },
                            icon: const Icon(
                              Icons.file_download,
                              color: mColorWhite,
                              size: 34.0,
                            ),
                          );
                        }
                        /// 촬영 모드
                        else {
                          return IconButton(
                            onPressed: () async {
                              /// TODO : 로컬 이미지 호출 필요
                              // showToast(context, '이미지 불러오기', size: 18);
                              _getFromGallery();
                            },
                            icon: const Icon(
                              Icons.file_copy,
                              color: mColorWhite,
                              size: 34.0,
                            ),
                          );
                        }
                      })
                  ),
                  GestureDetector(
                    onTap: () {
                      /// TODO 카메라 클릭
                      if(!_cameraMainController.getCaptureState()) captureCallback("CAPTURE");
                    },
                    child: Container(
                      height: 80.0,
                      width: 80.0,
                      padding: const EdgeInsets.all(1.0),
                      decoration: BoxDecoration(
                        shape: BoxShape.circle,
                        border:
                        Border.all(color: Colors.black, width: 1.0),
                        color: mColorWhite,
                      ),
                      child: Container(
                        decoration: BoxDecoration(
                          shape: BoxShape.circle,
                          border:
                          Border.all(color: Colors.black, width: 3.0),
                        ),
                      ),
                    ),
                  ),
                  Align(
                      alignment: Alignment.centerRight,
                      child: Obx((){
                        /// 편집 모드
                        if(_cameraMainController.getCaptureState()){
                          return IconButton(
                            onPressed: () async {
                              /// TODO : 로컬 이미지 호출 필요
                              showToast(context, '파일 공유하기', size: 18);
                            },
                            icon: const Icon(
                              Icons.share,
                              color: mColorWhite,
                              size: 34.0,
                            ),
                          );
                        }
                        /// 촬영 모드
                        else {
                          return IconButton(
                            onPressed: () async {
                              /// 후면 카메라 <-> 전면 카메라 변경
                              captureCallback("FILP");

                              //  _cameraMainController.setCaptureState(false);
                            },
                            icon: const Icon(
                              Icons.flip_camera_android,
                              color: mColorWhite,
                              size: 34.0,
                            ),
                          );
                        }
                      })
                  ),
                ],
              ),
            ),
          ]
        ),
      ),
    );
  }
}