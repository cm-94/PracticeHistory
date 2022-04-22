import 'dart:async';
import 'dart:convert';
import 'dart:io';
import 'package:auto_size_text/auto_size_text.dart';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:camerademo/comm/CameraOption.dart';
import 'package:camerademo/comm/MenuItem.dart';
import 'package:encrypt/encrypt.dart' as Encrypt;
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:intl/intl.dart';
import '../comm/FliterItem.dart';

/// 공통색상
const Color mColorWhite = Color.fromRGBO(255, 255, 255, 1.0);
const Color mLightBlueColor = Color.fromRGBO(173, 216, 230, 1.0);
const Color mLightSkyBlueColor = Color.fromRGBO(135, 206, 250, 1.0);
const Color mBrightRedColor = Color.fromRGBO(232, 226, 202, 1.0);
const Color mBGColor = Color.fromRGBO(255, 255, 255, 1.0);
const Color mLightGray = Color.fromRGBO(230, 230, 230, 1.0);
const Color mColorYellow = Color.fromRGBO(242, 242, 25, 1.0);
const Color mMediumGray = Color.fromRGBO(176, 176, 176, 1.0);
const Color mIntroColor = Color.fromRGBO(232, 231, 210, 1.0);
const Color mColorTransparent = Colors.transparent;
const Color mCamColor = Color.fromRGBO(210, 223, 205, 1.0);
const Color mControlColor = Color.fromRGBO(242,255,255, 0.2);
const Color textColor = Color.fromRGBO(255, 255, 255, 1.0);

typedef PickerCallback = void Function(int index);
typedef MsgBoxCallback = void Function(MSG_TYPE response);

enum MSG_TYPE { OK, CANCEL, OK_CANCEL, EXIT}

showToast(BuildContext context, String msg,{double size = 12}) {
  FToast().init(context).showToast(
    child: Container(
      padding: const EdgeInsets.symmetric(horizontal: 24.0, vertical: 15),
      decoration: BoxDecoration(
      borderRadius: BorderRadius.circular(30.0),
      color: mLightBlueColor,
    ),
      child: AutoSizeText(msg, style: TextStyle(fontWeight: FontWeight.bold, fontSize: size, color: textColor)),
    )
  );
}

showPicker(BuildContext context, List<Widget> list, PickerCallback callback) {
  Size size = MediaQuery.of(context).size;
  showCupertinoModalPopup(
    context: context,
    builder: (_) => Container(
      width: size.width,
      height: size.height / 3 * 2,
      child: CupertinoPicker(
        backgroundColor: mColorWhite,
        itemExtent: 60,
        scrollController: FixedExtentScrollController(initialItem: 1),
        children: list,
        onSelectedItemChanged: (value) {
          callback(value);
        },
      ),
    ));
}

void showMessageBox(BuildContext context, String msg, MSG_TYPE type) {
  List<Widget> btnList = [];
  if(type == MSG_TYPE.EXIT){
    btnList = [
      TextButton(
        onPressed: () => Navigator.pop(context, 'Cancel'),
        child: const Text('취소',style: TextStyle(fontSize: 16),),
      ),
      TextButton(
        onPressed: () => exit(0),
        child: const Text('종료',style: TextStyle(fontSize: 16),),
      ),
    ];
  }
  else if(type == MSG_TYPE.OK){
    btnList = [
      TextButton(
        onPressed: () => Navigator.pop(context, 'Ok'),
        child: const Text('확인',style: TextStyle(fontSize: 16),),
      )
    ];
  }
  else if(type == MSG_TYPE.CANCEL){
    btnList = [
      TextButton(
        onPressed: () => Navigator.pop(context, 'Cancel'),
        child: const Text('취소',style: TextStyle(fontSize: 16),),
      )
    ];
  }
  else if(type == MSG_TYPE.OK_CANCEL){
    btnList = [
      TextButton(
        onPressed: () => Navigator.pop(context, 'Cancel'),
        child: const Text('취소',style: TextStyle(fontSize: 16),),
      ),
      TextButton(
        onPressed: () => Navigator.pop(context, 'Ok'),
        child: const Text('확인',style: TextStyle(fontSize: 16),),
      )
    ];
  }

  showDialog<String>(
    context: context,
    builder: (BuildContext context) => AlertDialog(
      title: Text("알림"),
      content: Text(msg),
      actions: btnList,
    ),
  );
}

List<MenuItem> menuList = [
  MenuItem("메뉴1", '0001'),
  MenuItem("메뉴2", '0002'),
  MenuItem("메뉴3", '0003'),
  MenuItem("메뉴4", '0004'),
  MenuItem("메뉴5", '0005')
];

List<FilterItem> filterList = [
  FilterItem("기본", CameraOption.FILTER_DEFAULT),
  FilterItem("배경", CameraOption.FILTER_BACKGROUND),
  FilterItem("필터", CameraOption.FILTER_COVER),
  FilterItem("텍스트", CameraOption.FILTER_TEXT),
];
