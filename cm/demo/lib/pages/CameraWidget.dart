import 'dart:io';
import 'package:image/image.dart' as img;
import 'package:camera/camera.dart';
import 'package:dio/dio.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:get/get.dart';
import 'package:image_gallery_saver/image_gallery_saver.dart';
import 'package:image_picker/image_picker.dart';
import 'package:intl/intl.dart';
import 'package:scroll_snap_list/scroll_snap_list.dart';
import '../assets/constants.dart';
import '../comm/CameraOption.dart';
import '../utils/LOGCAT.dart';
import 'CameraMainController.dart';

class CameraWidget extends StatefulWidget {
  /// 상태
  final _CameraWidgetState _state = _CameraWidgetState();

  @override
  _CameraWidgetState createState() => _state;

  /// 사진 촬영
  void cameraCapture(){
    _state.cameraCapture();
  }

  /// 카메라 앞 - 뒤 전환
  Future<void> flipCamera() async {
    _state.cameraIndex = _state.cameraIndex == 0 ? 1 : 0;
    await _state._initCamera();
  }

  Future selectImage(ImageSource imageSource) async {
    // _save();
  }

  Future saveImage() async {
    _state._save();
  }
}

class _CameraWidgetState extends State<CameraWidget> {
  CameraController? _cameraController; /// 카메라 라이브러리 컨트롤러
  Future<void>? _initCameraControllerFuture;
  final CameraMainController _cameraMainController = Get.find<CameraMainController>();
  int cameraIndex = 0; /// 0 : 후면, 1: 전면

  List<String> sizeList = CameraOption().getSizeList();


  @override
  void initState() {
    super.initState();
    _initCamera();
    // SystemChrome.setEnabledSystemUIMode(SystemUiMode.manual, overlays: [
    //   SystemUiOverlay.bottom
    // ]);
  }

  @override
  void dispose() {
    super.dispose();
    _cameraController!.dispose();
    // SystemChrome.setEnabledSystemUIMode(SystemUiMode.manual, overlays: SystemUiOverlay.values);  // to re-show bars
  }

  void cameraCapture() async {
    try {
      await _cameraController!
          .takePicture()
          .then((value) {
        _cameraMainController.captureImage = File(value.path);

      });

      /// 화면 상태 변경 및 이미지 저장
      setState(() {
        _cameraMainController.setCaptureState(true);
      });
    } catch (e) {
      LOGCAT.e("Capture_Error", "$e");
    }
  }

  _save() async {
    if(_cameraMainController.captureImage != null){
      final img.Image? capturedImage = img.decodeImage(await File(_cameraMainController.captureImage!.path).readAsBytes());
      final img.Image? orientedImage = img.bakeOrientation(capturedImage!);
      var newImage =  await File(_cameraMainController.captureImage!.path).writeAsBytes(img.encodeJpg(orientedImage!));

      final result = await newImage.readAsBytes();
      var isSave = await ImageGallerySaver.saveImage(result, quality: 60, name: "one_shot_${DateFormat("yyyyMMdd_hhmmss").format(DateTime.now())}");
      if(isSave['isSuccess']){
        showToast(context, '저장 완료', size: 18);
      }else {
        showToast(context, '저장 실패', size: 18);
      }
    }
  }

  Future<void> _initCamera() async {
    final cameras = await availableCameras();
    _cameraController = CameraController(cameras[cameraIndex], ResolutionPreset.veryHigh);
    _initCameraControllerFuture = _cameraController!.initialize().then((value) {
      setState(() { });
    });
    _cameraController?.setFlashMode(FlashMode.off);

    Size size = MediaQuery.of(context).size;
    print('status_aaa');

  }

  Widget _getSizeList(BuildContext context, int index) {
    return Container(
      alignment: Alignment.center,
      height: 20,
      width: 50,
      child: Text(sizeList[index], style: TextStyle(color: mColorWhite, fontWeight: FontWeight.bold, fontSize: 12), ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Stack(
      children: [
        Obx((){
          return _cameraMainController.getCaptureState() ?
          /// 편집 모드
          Container(
            width: _cameraMainController.camWidth.value,
            height: _cameraMainController.camHeight.value,
            child: ClipRect(
              child: FittedBox(
                fit: BoxFit.fitWidth,
                child: SizedBox(
                  width: _cameraMainController.camWidth.value,
                  child: AspectRatio(
                    aspectRatio:
                    1 / _cameraController!.value.aspectRatio,
                    child: Container(
                      width: _cameraMainController.camWidth.value,
                      decoration: BoxDecoration(
                          image: DecorationImage(
                            image: MemoryImage(
                                _cameraMainController.captureImage!.readAsBytesSync()),
                          )
                      ),
                    ),
                  ),
                ),
              ),
            ),
          ) :
          /// 카메라 모드
          FutureBuilder<void>(
            future: _initCameraControllerFuture,
            builder: (BuildContext context, AsyncSnapshot snapshot) {
              if (snapshot.connectionState == ConnectionState.done) {
                return Obx((){
                  return Container(
                    width: _cameraMainController.camWidth.value,
                    height: _cameraMainController.camHeight.value,
                    child: ClipRect(
                      child: FittedBox(
                        fit: BoxFit.fitWidth,
                        child: SizedBox(
                          width: _cameraMainController.camWidth.value,
                          child: AspectRatio(
                              aspectRatio: 1 /
                                  _cameraController!.value.aspectRatio,
                              child: CameraPreview(_cameraController!)),
                        ),
                      ),
                    ),
                  );
                });
              } else {
                return Container(
                  width: _cameraMainController.camWidth.value,
                  height: _cameraMainController.camHeight.value,
                  child: const Center(child: CircularProgressIndicator()),
                );
              }
            },
          );
        }),
        /// TODO : 카메라 사이즈 세팅 메뉴
        Obx((){
            int index = CameraOption().getSizeList().indexOf(_cameraMainController.getStrCamRatio());
            if(index == -1) index = 0;

            return Visibility(
              visible: !_cameraMainController.getCaptureState(),
              child: Positioned(
                  top: 0,
                  left: 0,
                  child: Container(
                      width: _cameraMainController.camWidth.value,
                      height: 40,
                      child: Container(
                        width: _cameraMainController.camWidth.value,
                        child: Column(
                          children: <Widget>[
                            Expanded(
                              child: ScrollSnapList(
                                scrollPhysics: ScrollPhysics(),
                                listController: ScrollController(initialScrollOffset: index * 50),
                                onItemFocus: (value) {
                                  _cameraMainController.setRatio(CameraOption().getSizeOption(sizeList[value]), MediaQuery.of(context).size);
                                },
                                itemSize: 50,
                                itemBuilder: _getSizeList,
                                itemCount: sizeList.length,
                                dynamicItemSize: true,
                                // dynamicSizeEquation: customEquation, //optional
                              ),
                            ),
                          ],
                        ),
                      )
                  )
              )
            );

        }),
        /// 카메라 플래쉬 세팅 아이콘
        Obx((){
          return Visibility(
            visible: !_cameraMainController.getCaptureState(),
            child: Positioned(
                top: 40,
                right: 10,
                child: Container(
                  width: 160,
                  height: 36,
                  alignment: Alignment.centerRight,
                  child: ToggleButtons(
                    isSelected: _cameraMainController.flashToggle.value,
                    color: mColorWhite,
                    fillColor: mColorTransparent,
                    selectedColor: mColorYellow,
                    splashColor: mColorTransparent,
                    borderColor: mColorTransparent,
                    selectedBorderColor: mColorTransparent,
                    children: const [
                      Icon(
                        Icons.flash_on,
                        size: 10.0,
                      ),
                      Icon(
                        Icons.flash_off,
                        size: 10.0,
                      ),
                      Icon(
                        Icons.flash_auto,
                        size: 10.0,
                      ),
                    ],
                    onPressed: (value){
                      if(value == 0) _cameraController?.setFlashMode(FlashMode.always);
                      else if(value == 1) _cameraController?.setFlashMode(FlashMode.off);
                      else if(value == 2) _cameraController?.setFlashMode(FlashMode.auto);
                      setState(() {
                        _cameraMainController.setFlash(value);
                      });
                    },
                  ),
                  // Row(
                  //   mainAxisAlignment: MainAxisAlignment.end,
                  //   children: <Widget>[
                  //     IconButton(
                  //       onPressed: () async {
                  //         _cameraController?.setFlashMode(FlashMode.always);
                  //       },
                  //       icon: const Icon(
                  //         Icons.flash_on,
                  //         color: mColorWhite,
                  //         size: 16.0,
                  //       ),
                  //     ),
                  //     IconButton(
                  //       onPressed: () async {
                  //         _cameraController?.setFlashMode(FlashMode.off);
                  //       },
                  //       icon: const Icon(
                  //         Icons.flash_off,
                  //         color: mColorWhite,
                  //         size: 16.0,
                  //       ),
                  //     ),
                  //     IconButton(
                  //       onPressed: () async {
                  //         _cameraController?.setFlashMode(FlashMode.auto);
                  //       },
                  //       icon: const Icon(
                  //         Icons.flash_auto,
                  //         color: mColorWhite,
                  //         size: 16.0,
                  //       ),
                  //     ),
                  //   ],
                  // )
                )
            ),
          );
        }),
      ],
    );
  }
}