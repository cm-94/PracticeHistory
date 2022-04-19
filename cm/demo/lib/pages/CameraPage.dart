import 'dart:io';
import 'package:auto_size_text/auto_size_text.dart';
import 'package:camera/camera.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:get/get.dart';
import 'package:permission_handler/permission_handler.dart';
import '../assets/constants.dart';
import '../utils/LOGCAT.dart';
import 'CameraMainController.dart';
import 'TopWidget.dart';
import 'package:image_picker/image_picker.dart';
import 'package:scroll_snap_list/scroll_snap_list.dart';

/// //////////////////
/// 카메라 메인 화면 ///
/// //////////////////
class CameraPage extends StatefulWidget {
  @override
  _CameraPageState createState() => _CameraPageState();
}

class _CameraPageState extends State<CameraPage> {
  CameraController? _cameraController; /// 카메라 라이브러리 컨트롤러
  Future<void>? _initCameraControllerFuture;
  final CameraMainController _cameraMainController = Get.find<CameraMainController>();
  int cameraIndex = 0; /// 0 : 후면, 1: 전면
  File? captureImage;  /// 촬영 이미지
  /// 상단 메뉴바
  TopWidget topWidget = TopWidget();


  Widget _getFilterList(BuildContext context, int index) {
    //horizontal
    return Container(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: <Widget>[
          Container(
            alignment: Alignment.center,
            height: 40,
            width: 60,
            color: mColorTransparent,
            child: Text(filterList[index].title,),
          ),
        ],
      ),
    );
  }

  @override
  void initState() {
    super.initState();
    _initCamera();
    SystemChrome.setEnabledSystemUIMode(SystemUiMode.manual, overlays: [
      SystemUiOverlay.bottom
    ]);
  }

  Future<void> _initCamera() async {
    final cameras = await availableCameras();
    _cameraController =
        CameraController(cameras[cameraIndex], ResolutionPreset.veryHigh);
    _initCameraControllerFuture = _cameraController!.initialize().then((value) {
      setState(() {});
    });
  }

  @override
  void dispose() {
    super.dispose();
    _cameraController!.dispose();
    SystemChrome.setEnabledSystemUIMode(SystemUiMode.manual, overlays: SystemUiOverlay.values);  // to re-show bars
  }

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    return WillPopScope(    // <-  WillPopScope로 감싼다.
      onWillPop: () {
        setState(() {
          if(topWidget.getMenuState()){
            topWidget.closeMenu();
          }
          else if(topWidget.getCaptureState()){
            topWidget.setCaptureState(false);
          }
          else {
            // TODO : 종료 팝업 뒤
            showToast(context,"종료하시겠습니까?", size: 16);
          }
        });
        return Future(() => false);
      },
      child: Scaffold(
        backgroundColor: mCamColor,
        body: Obx(() {
          return Column(
            children: [
              topWidget,
              _cameraMainController.getCaptureState() ?
              /// 편집 모드
              SizedBox(
                width: size.width,
                height: size.width,
                child: ClipRect(
                  child: FittedBox(
                    fit: BoxFit.fitWidth,
                    child: SizedBox(
                      width: size.width,
                      child: AspectRatio(
                        aspectRatio:
                        1 / _cameraController!.value.aspectRatio,
                        child: Container(
                          width: size.width,
                          decoration: BoxDecoration(
                              image: DecorationImage(
                                image: MemoryImage(
                                    captureImage!.readAsBytesSync()),
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
                    return SizedBox(
                      width: size.width,
                      height: size.width,
                      child: ClipRect(
                        child: FittedBox(
                          fit: BoxFit.fitWidth,
                          child: SizedBox(
                            width: size.width,
                            child: AspectRatio(
                                aspectRatio: 1 /
                                    _cameraController!.value.aspectRatio,
                                child: CameraPreview(_cameraController!)),
                          ),
                        ),
                      ),
                    );
                  } else {
                    return const Center(child: CircularProgressIndicator());
                  }
                },
              ),
              Visibility(
                visible: !_cameraMainController.getMenuState(),
                child: SizedBox(
                  width: size.width,
                  height: 200,
                  child: Stack(
                      children: [
                        /// 하단 버튼 영역
                        Container(
                          alignment: Alignment.center,
                          padding: const EdgeInsets.fromLTRB(48.0, 48.0, 48.0, 0.0),
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
                                          showToast(context, '저장 완료', size: 18);
                                        },
                                        icon: const Icon(
                                          Icons.file_download,
                                          color: Colors.white,
                                          size: 34.0,
                                        ),
                                      );
                                    }
                                    /// 촬영 모드
                                    else {
                                      return IconButton(
                                        onPressed: () async {
                                          /// TODO : 로컬 이미지 호출 필요
                                          showToast(context, '이미지 불러오기', size: 18);
                                        },
                                        icon: const Icon(
                                          Icons.file_copy,
                                          color: Colors.white,
                                          size: 34.0,
                                        ),
                                      );
                                    }
                                  })
                              ),
                              GestureDetector(
                                onTap: () async {
                                  try {
                                    await _cameraController!
                                        .takePicture()
                                        .then((value) {
                                      captureImage = File(value.path);
                                    });

                                    /// 화면 상태 변경 및 이미지 저장
                                    setState(() {
                                      _cameraMainController.setCaptureState(true);
                                    });
                                  } catch (e) {
                                    LOGCAT.e("Capture_Error", "$e");
                                  }
                                },
                                child: Container(
                                  height: 80.0,
                                  width: 80.0,
                                  padding: const EdgeInsets.all(1.0),
                                  decoration: BoxDecoration(
                                    shape: BoxShape.circle,
                                    border:
                                    Border.all(color: Colors.black, width: 1.0),
                                    color: Colors.white,
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
                                          color: Colors.white,
                                          size: 34.0,
                                        ),
                                      );
                                    }
                                    /// 촬영 모드
                                    else {
                                      return IconButton(
                                        onPressed: () async {
                                          showPicker(context, (value){
                                            LOGCAT.d('callback', value.toString());
                                          });
                                          // /// 후면 카메라 <-> 전면 카메라 변경
                                          // cameraIndex = cameraIndex == 0 ? 1 : 0;
                                          // await _initCamera();
                                          // //  _cameraMainController.setCaptureState(false);
                                        },
                                        icon: const Icon(
                                          Icons.flip_camera_android,
                                          color: Colors.white,
                                          size: 34.0,
                                        ),
                                      );
                                    }
                                  })
                              ),
                            ],
                          ),
                        ),
                        /// TODO : 카메라 필터 세팅 메뉴
                        Positioned(
                            top: 0,
                            left: 0,
                            child: Container(
                                width: size.width,
                                height: 48,
                                color: mLightBlueColor,
                                child: Container(
                                  width: size.width,
                                  child: Column(
                                    children: <Widget>[
                                      Expanded(
                                        child: ScrollSnapList(
                                          onItemFocus: (value) {
                                            _cameraMainController.setFilter(value);
                                          },
                                          itemSize: 60,
                                          itemBuilder: _getFilterList,
                                          itemCount: filterList.length,
                                          dynamicItemSize: true,
                                          // dynamicSizeEquation: customEquation, //optional
                                        ),
                                      ),
                                    ],
                                  ),
                                )
                            )
                        ),
                      ]
                  ),
                ),
              )
            ],
          );
        }),
      ),
    );
  }

  Future<bool> checkPermission() async {
    Map<Permission, PermissionStatus> statuses = await [
      Permission.camera,
      Permission.photos,
      Permission.photosAddOnly,
      Permission.sms,
      Permission.notification
    ].request(); //여러가지 퍼미션을하고싶으면 []안에 추가하면된다. (팝업창이뜬다)

    bool per = true;

    statuses.forEach((permission, permissionStatus) {
      if (!permissionStatus.isGranted) {
        per = false; //하나라도 허용이안됐으면 false
      }
    });

    return per;
  }
}
