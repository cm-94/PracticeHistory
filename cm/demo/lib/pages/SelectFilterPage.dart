import 'package:animated_widgets/animated_widgets.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:scroll_snap_list/scroll_snap_list.dart';

import '../assets/constants.dart';
import '../comm/CameraOption.dart';
import 'CameraMainController.dart';
import 'SelectFilterController.dart';

class SelectFilterPage extends StatelessWidget {
  /// 카메라 뷰 컨트롤러
  final CameraMainController _cameraMainController = Get.find<CameraMainController>();
  /// 필터 뷰 컨트롤러
  final SelectFilterController _selectFilterController = Get.find<SelectFilterController>();
  ScrollController listController = ScrollController();

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
      return Container(width: size.width,height: 120,color: Colors.red);
    }
    else if(type == CameraOption.FILTER_COVER){
      return Container(width: size.width,height: 120,color: Colors.green);
    }
    else if(type == CameraOption.FILTER_TEXT){
      return Container(width: size.width,height: 120,color: Colors.blue);
    }
    else{
      return const SizedBox(width: 0,height: 0);
    }
  }

  Widget _getFilterList(BuildContext context, int index) {
    return TextButton(
        style: TextButton.styleFrom(
          minimumSize: Size(60,32), // Set this
          padding: EdgeInsets.zero, // and this
        ),
        onPressed: () {
          if(_cameraMainController.getFilter(index) == _selectFilterController.getFilterType() && _selectFilterController.getFilterType() != CameraOption.FILTER_DEFAULT){
            _selectFilterController.setFilterType(0);
          }
          else{
            _cameraMainController.setFilter(index);
            _selectFilterController.setFilterType(index);
            listController.animateTo((index * 60), duration: Duration(milliseconds: 200), curve: Curves.linear);
          }
        },
        child: Container(
          height: 32,
          width: 60,
          alignment: Alignment.center,
          padding: EdgeInsets.zero,
          child: Text(filterList[index].title, style: TextStyle(color: mColorWhite, fontWeight: FontWeight.bold, fontSize: 14), ),
        )
    );
  }

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    _cameraMainController.init();

    return Container(
      width: size.width,
      height: 152,
      child: Stack(
        children: [
          /// TODO : 카메라 필터 세팅 메뉴
          Positioned(
              bottom: 0,
              left: 0,
              child: Container(
                  width: size.width,
                  height: 28,
                  color: mColorTransparent,
                  child: Container(
                      width: size.width,
                      child:  Column(
                        children: <Widget>[
                          Expanded(
                            child: ScrollSnapList(
                              listController: listController,
                              onItemFocus: (value) {
                                _cameraMainController.setFilter(value);
                                _selectFilterController.setFilterType(value);
                              },
                              itemSize: 60,
                              itemBuilder: _getFilterList,
                              itemCount: filterList.length,
                              dynamicItemSize: true,
                              // dynamicSizeEquation: customEquation, //optional
                            ),
                          ),
                        ],
                      )
                  )
              )
          ),
          Positioned(
            child: Obx(() {
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
            })
          )
        ]
      ),
    );
  }
}