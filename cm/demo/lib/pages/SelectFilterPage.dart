import 'package:animated_widgets/animated_widgets.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

import '../comm/CameraOption.dart';
import 'CameraMainController.dart';
import 'SelectFilterController.dart';

class SelectFilterPage extends StatelessWidget {
  /// 카메라 뷰 컨트롤러
  final CameraMainController _cameraMainController = Get.find<CameraMainController>();
  /// 필터 뷰 컨트롤러
  final SelectFilterController _selectFilterController = Get.find<SelectFilterController>();

  @override
  void initState() {

  }

  /// 스크롤 뷰 컨트롤러
  ScrollController verticalScrollController = ScrollController();
  ScrollController horizontalScrollController = ScrollController();

  bool displayMyWidget = true;

  Widget addFilterWidget(BuildContext context, String type){
    Size size = MediaQuery.of(context).size;

    if(type == CameraOption.FILTER_BACKGROUND){
      return Container(
        width: size.width,
        height: 120,
        color: Colors.red,
      );
    }
    else if(type == CameraOption.FILTER_COVER){
      return Container(
        width: size.width,
        height: 120,
        color: Colors.green,
      );
    }
    else if(type == CameraOption.FILTER_TEXT){
      return Container(
        width: size.width,
        height: 300,
        color: Colors.blue,
      );
    }
    else{
      return const SizedBox(width: 0,height: 0);
    }
  }

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    _cameraMainController.init();

    return Obx(() {
      return Container(
        width: size.width,
        child: TranslationAnimatedWidget.tween(
          enabled: _selectFilterController.getFilterType() != CameraOption.FILTER_DEFAULT && !_cameraMainController.getMenuState(),
          translationDisabled: Offset(0, 60),
          translationEnabled: Offset(0, 0),
          curve: Curves.ease,
          child:
          OpacityAnimatedWidget.tween(
            enabled: _selectFilterController.getFilterType() != CameraOption.FILTER_DEFAULT && !_cameraMainController.getMenuState(),
            opacityDisabled: 0,
            opacityEnabled: 1,
            child: addFilterWidget(context, _selectFilterController.getFilterType()),
          ),
        ),
      );
    });
  }
}