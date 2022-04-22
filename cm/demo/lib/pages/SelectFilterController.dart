import 'package:flutter/material.dart';
import 'package:get/get.dart';
import '../assets/constants.dart';
import '../comm/CameraOption.dart';

class SelectFilterController extends GetxController {
  var isDisplay = false.obs;
  var filterType = CameraOption.convertOptionString(FILTER_TYPE.DEFUALT).obs;

  String getFilterType(){
    return filterType.value;
  }

  void setFilterType(int index){
    filterType.value = filterList[index].filterCode;
  }

  void init() {

  }
}