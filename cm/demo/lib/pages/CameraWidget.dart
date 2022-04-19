import 'dart:io';
import 'package:camera/camera.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:get/get.dart';
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

  void cameraCapture(){
    _state.cameraCapture();
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
    SystemChrome.setEnabledSystemUIMode(SystemUiMode.manual, overlays: [
      SystemUiOverlay.bottom
    ]);
  }

  @override
  void dispose() {
    super.dispose();
    _cameraController!.dispose();
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

  Future<void> _initCamera() async {
    final cameras = await availableCameras();
    _cameraController =
        CameraController(cameras[cameraIndex], ResolutionPreset.veryHigh);
    _initCameraControllerFuture = _cameraController!.initialize().then((value) {
      setState(() {
      });
    });
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
    Size size = MediaQuery.of(context).size;
    _cameraMainController.setRatio(SIZE_TYPE.DEFAULT, size);

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
                _cameraController;
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
                return const Center(child: CircularProgressIndicator());
              }
            },
          );
        }),
        /// TODO : 카메라 사이즈 세팅 메뉴
        Visibility(
          visible: !_cameraMainController.getCaptureState(),
          child: Positioned(
              top: 0,
              left: 0,
              child: Container(
                  width: size.width,
                  height: 40,
                  child: Container(
                    width: size.width,
                    child: Column(
                      children: <Widget>[
                        Expanded(
                          child: ScrollSnapList(
                            onItemFocus: (value) {
                              _cameraMainController.setRatio(CameraOption().getSizeOption(sizeList[value]), size);
                            },
                            itemSize: 50,
                            itemBuilder: _getSizeList,
                            itemCount: sizeList.length,
                            dynamicItemSize: false,
                            // dynamicSizeEquation: customEquation, //optional
                          ),
                        ),
                      ],
                    ),
                  )
              )
          ),
        ),
      ],
    );
  }
}