import 'dart:io';
import 'package:auto_size_text/auto_size_text.dart';
import 'package:camera/camera.dart';
import 'package:camerademo/pages/CameraMainController.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:permission_handler/permission_handler.dart';

import '../assets/constants.dart';
import '../utils/LOGCAT.dart';

/// //////////////////
/// 카메라 상단 위젯 ///
/// //////////////////
class MenuPopup extends StatefulWidget {
  @override
  _MenuPopupState createState() => _MenuPopupState();
}

class _MenuPopupState extends State<MenuPopup> {
  CameraMainController _controller = Get.find<CameraMainController>();

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    // double width = (MediaQuery.of(context).size.width) / 2;

    return Container(
      padding: const EdgeInsets.only(top: 5, right: 20),
      width: size.width,
      height: size.height,
      color: mBrightRedColor,
      child: Stack(
        children: [
          ScrollConfiguration(
            behavior: ScrollBehavior(),
            child: GlowingOverscrollIndicator(
              axisDirection: AxisDirection.down,
              color: mLightSkyBlueColor,
              child: ListView.builder(
                padding: EdgeInsets.only(top: 10),
                itemCount: menuList.length,
                itemBuilder: (context, index) {
                  String title = menuList[index].title;
                  String menuCode = menuList[index].menuCode;
                  return InkWell(
                    onTap: () {
                      /// TODO : 메뉴 이동
                      LOGCAT.d('Menu_Code', menuCode);
                      showToast(context, title, size: 18);
                    },
                    child: Container(
                      height: 70,
                      padding: EdgeInsets.only(left: 20),
                      child: Row(
                        children: [
                          Expanded(
                            flex: 1,
                            child: Column(
                              mainAxisAlignment: MainAxisAlignment.center,
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                AutoSizeText(
                                  title,
                                  textAlign: TextAlign.left,
                                  style: TextStyle(fontSize: 20),
                                  maxLines: 1,
                                  minFontSize: 5,
                                  overflow: TextOverflow.ellipsis,
                                ),
                                SizedBox(height: 40),
                              ],
                            ),
                          ),
                        ],
                      ),
                    ),
                  );
                },
              ),
            ),
          ),
          Positioned(
            top: 0,
            right: 0,
            child: IconButton(
              onPressed: () async {
                _controller.setMenuState(false);
                //  _cameraMainController.isCapture.value = false;
              },
              icon: Icon(
                Icons.close,
                color: Colors.white,
                size: 24.0,
              ),
            ),
          ),
        ],
      ),
    );
  }
}
