import 'dart:io';
import 'package:camera/camera.dart';
import 'package:flutter/material.dart';
import 'package:permission_handler/permission_handler.dart';

import '../assets/constants.dart';
import '../utils/LOGCAT.dart';

/// //////////////////
/// 카메라 메인 화면 ///
/// //////////////////
class TopWidget extends StatefulWidget {
  @override
  _TopWidgetState createState() => _TopWidgetState();
}

class _TopWidgetState extends State<TopWidget> {
  TopController _topController;

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    // double width = (MediaQuery.of(context).size.width) / 2;

    return Scaffold(
      backgroundColor: mCamColor,
      body: isCapture
          ? Column(
        children: [
          /// 촬영 된 이미지 출력
          Flexible(
            flex: 4,
            fit: FlexFit.tight,
            child: Container(
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
                            )),
                      ),
                    ),
                  ),
                ),
              ),
            ),
          ),
          Flexible(
            flex: 1,
            fit: FlexFit.tight,
            child: InkWell(
              onTap: () {
                /// 재촬영 선택시 카메라 삭제 및 상태 변경
                captureImage!.delete();
                captureImage = null;
                setState(() {
                  isCapture = false;
                });
              },
              child: Container(
                width: double.infinity,
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Icon(
                      Icons.file_download,
                      color: Colors.white,
                    ),
                    SizedBox(height: 8.0),
                    Text(
                      "저장",
                      style: TextStyle(
                          fontSize: 16.0,
                          color: Colors.white,
                          fontWeight: FontWeight.bold),
                    ),
                  ],
                ),
              ),
            ),
          ),
        ],
      )
          : Column(
        children: [
          Flexible(
            flex: 3,
            fit: FlexFit.tight,
            child: FutureBuilder<void>(
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
                  return Center(child: CircularProgressIndicator());
                }
              },
            ),
          ),
          Flexible(
            flex: 1,
            child: Container(
              alignment: Alignment.center,
              padding: const EdgeInsets.symmetric(horizontal: 48.0),
              child: Stack(
                alignment: Alignment.center,
                children: [
                  Align(
                    alignment: Alignment.centerLeft,
                    child: IconButton(
                      onPressed: () async {
                        /// 후면 카메라 <-> 전면 카메라 변경
                        cameraIndex = cameraIndex == 0 ? 1 : 0;
                        await _initCamera();
                      },
                      icon: Icon(
                        Icons.file_download,
                        color: Colors.white,
                        size: 34.0,
                      ),
                    ),
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
                          isCapture = true;
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
                    child: IconButton(
                      onPressed: () async {
                        /// 후면 카메라 <-> 전면 카메라 변경
                        cameraIndex = cameraIndex == 0 ? 1 : 0;
                        await _initCamera();
                      },
                      icon: Icon(
                        Icons.flip_camera_android,
                        color: Colors.white,
                        size: 34.0,
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ),
        ],
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
