import 'dart:developer';
import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:qr_code_scanner/qr_code_scanner.dart';

void main() => runApp(const MaterialApp(home: MyHome()));

class MyHome extends StatelessWidget {
  const MyHome({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return const QRViewPage();
  }
}


class QRViewPage extends StatefulWidget {
  const QRViewPage({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => QRViewPageState();
}

class QRViewPageState extends State<QRViewPage> {
  Barcode? result;
  QRViewController? controller;
  final GlobalKey qrKey = GlobalKey(debugLabel: 'QR');

  // In order to get hot reload to work we need to pause the camera if the platform
  // is android, or resume the camera if the platform is iOS.
  @override
  void reassemble() {
    super.reassemble();
    if (Platform.isAndroid) {
      controller!.pauseCamera();
    }
    controller!.resumeCamera();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Color.fromRGBO(32,29,10, 1.0),
      body: Column(
        children: <Widget>[
          Container(
            padding: const EdgeInsets.only(right: 20),
            height: 60,
            child: Row(
              children: [
                IconButton(
                  onPressed: () async {

                  },
                  icon: const Icon(
                    Icons.keyboard_arrow_left,
                    color: Color.fromRGBO(242,242,25,1.0),
                    size: 36.0,
                  ),
                ),
                Flexible(
                    flex: 1,
                    child: Text("QR SCAN",style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 16),textAlign: TextAlign.center,)
                )
              ],
            ),
          ),
          Expanded(flex: 5, child: _buildQrView(context)),
          Expanded(
            flex: 1,
            child: FittedBox(
              fit: BoxFit.contain,
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                children: [
                  Expanded(
                    flex: 1,
                    child: Column(
                      children: [
                        IconButton(
                          onPressed: () {  },
                          icon: const Icon(
                            Icons.flip_camera_android,
                            color: Color.fromRGBO(173, 216, 230, 1.0),
                            size: 34.0,
                          ),
                        ),
                        Text('QR SCAN')
                      ],
                    ),
                  ),
                  Expanded(
                    flex: 1,
                    child: Column(
                      children: [
                        IconButton(
                          onPressed: () {  },
                          icon: const Icon(
                            Icons.flip_camera_android,
                            color: Color.fromRGBO(173, 216, 230, 1.0),
                            size: 34.0,
                          ),
                        ),
                        Text('QR SCAN')
                      ],
                    ),
                  )
                ],
              ),
            ),
          )
        ],
      )
      // Column(
      //   children: <Widget>[
      //     Container(
      //       padding: const EdgeInsets.only(right: 20),
      //       height: 60,
      //       child: Row(
      //         children: [
      //           IconButton(
      //             onPressed: () async {
      //
      //             },
      //             icon: const Icon(
      //               Icons.keyboard_arrow_left,
      //               color: Color.fromRGBO(242,242,25,1.0),
      //               size: 36.0,
      //             ),
      //           ),
      //           Expanded(
      //             flex: 1,
      //             child: Text("QR SCAN",style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 16),textAlign: TextAlign.center,)
      //           )
      //         ],
      //       ),
      //     ),
      //     Expanded(flex: 5, child: _buildQrView(context)),
      //     Expanded(
      //       flex: 1,
      //       child: FittedBox(
      //         fit: BoxFit.contain,
      //         child: Row(
      //           mainAxisAlignment: MainAxisAlignment.center,
      //           crossAxisAlignment: CrossAxisAlignment.center,
      //           children: <Widget>[
      //             /// 1. Flash On, Off
      //             Container(
      //               margin: const EdgeInsets.all(8),
      //               child: ElevatedButton(
      //                   onPressed: () async {
      //                     await controller?.toggleFlash();
      //                     setState(() {});
      //                   },
      //                   child: FutureBuilder(
      //                     future: controller?.getFlashStatus(),
      //                     builder: (context, snapshot) {
      //                       return Text('Flash: ${snapshot.data}'); /// true or false
      //                     },
      //                   )),
      //             ),
      //             /// 2. 화면 전환( 앞 / 뒤 )
      //             Container(
      //               margin: const EdgeInsets.all(8),
      //               child: ElevatedButton(
      //                   onPressed: () async {
      //                     await controller?.flipCamera();
      //                     setState(() {});
      //                   },
      //                   child: FutureBuilder(
      //                     future: controller?.getCameraInfo(),
      //                     builder: (context, snapshot) { return Text(snapshot.data != null? 'Camera facing ${describeEnum(snapshot.data!)}' : "loading"); /* snapshot.data : front or back */ },
      //                   )),
      //             )
      //           ],
      //         ),
      //       ),
      //     )
      //   ],
      // ),
    );
  }

  /// QR 카메라 영역
  Widget _buildQrView(BuildContext context) {
    // For this example we check how width or tall the device is and change the scanArea and overlay accordingly.
    /// var scanArea = (MediaQuery.of(context).size.width < 400 || MediaQuery.of(context).size.height < 400) ? 150.0 : 300.0;
    /// TODO : 사이즈 수정 필요
    var scanArea = MediaQuery.of(context).size.width - 40;
    // To ensure the Scanner view is properly sizes after rotation
    // we need to listen for Flutter SizeChanged notification and update controller
    return QRView(
      key: qrKey,
      onQRViewCreated: _onQRViewCreated,
      /// TODO : 디자인 커스텀 필요
      overlay: QrScannerOverlayShape(
          borderColor: Colors.red,
          borderRadius: 10,
          borderLength: 30,
          borderWidth: 10,
          cutOutSize: scanArea),
      onPermissionSet: (ctrl, p) => _onPermissionSet(context, ctrl, p),
    );
  }

  /// QR 뷰 생성
  void _onQRViewCreated(QRViewController controller) {
    setState(() {
      this.controller = controller;
    });
    /// QR 스캔 리스너
    controller.scannedDataStream.listen((scanData) {
      setState(() {
        result = scanData;
      });
    });
  }

  void _onPermissionSet(BuildContext context, QRViewController ctrl, bool p) {
    log('${DateTime.now().toIso8601String()}_onPermissionSet $p');
    if (!p) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('no Permission')),
      );
    }
  }

  @override
  void dispose() {
    controller?.dispose();
    super.dispose();
  }
}

// import 'dart:io';
//
// import 'package:animated_splash_screen/animated_splash_screen.dart';
// import 'package:camerademo/pages/CameraPage.dart';
// import 'package:camerademo/pages/IntroPage.dart';
// import 'package:flutter/material.dart';
// import 'package:flutter/services.dart';
// import 'package:get/get.dart';
// import 'package:permission_handler/permission_handler.dart';
// import 'package:splashscreen/splashscreen.dart';
// import 'assets/constants.dart';
// import 'pages/CameraMainController.dart';
// import 'pages/SelectFilterController.dart';
//
// void main() {
//   WidgetsFlutterBinding.ensureInitialized();
//   SystemChrome.setEnabledSystemUIOverlays([]);
//
//   runApp(MyApp());
// }
//
// class MyApp extends StatelessWidget {
//   @override
//   Widget build(BuildContext context) {
//     CameraMainController _cameraMainController =  Get.put(CameraMainController());
//     SelectFilterController _selectFilterController =  Get.put(SelectFilterController());
//     _cameraMainController.init();
//
//     // checkPermission().then((val) {
//     //   if(!val){ /// 카메라 권한 X
//     //     exit(0); /// TODO : 앱 종료
//     //   }
//     // }).catchError((error) {
//     //   exit(0); /// 앱 종료
//     // });
//     return GetMaterialApp(
//       title: 'One Shot',
//       theme: ThemeData(
//         primarySwatch: Colors.blue,
//         visualDensity: VisualDensity.adaptivePlatformDensity,
//       ),
//       home: AnimatedSplashScreen(
//         splash: Image.asset('lib/assets/images/app_icon.png'),
//         duration: 3000,
//         backgroundColor: mCamColor,
//         splashTransition: SplashTransition.scaleTransition,
//         nextScreen: CameraPage(),
//       ),
//       debugShowCheckedModeBanner: false,
//     );
//   }
//
//   Future<bool> checkPermission() async{
//     Map<Permission, PermissionStatus> statuses =
//     await[Permission.camera, Permission.photos, Permission.photosAddOnly, Permission.sms, Permission.notification].request(); //여러가지 퍼미션을하고싶으면 []안에 추가하면된다. (팝업창이뜬다)
//
//     bool per= true;
//
//     statuses.forEach((permission, permissionStatus){
//       if(!permissionStatus.isGranted){
//         per = false; //하나라도 허용이안됐으면 false
//       }
//     });
//
//     return per;
//   }
// }