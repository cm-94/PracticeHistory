import 'dart:async';
import 'dart:convert';
import 'package:auto_size_text/auto_size_text.dart';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:camerademo/comm/MenuItem.dart';
import 'package:encrypt/encrypt.dart' as Encrypt;
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:intl/intl.dart';

/// 공통색상
const Color mColorWhite = Color.fromRGBO(255, 255, 255, 1.0);
const Color mLightBlueColor = Color.fromRGBO(173, 216, 230, 1.0);
const Color mLightSkyBlueColor = Color.fromRGBO(135, 206, 250, 1.0);
const Color mBrightRedColor = Color.fromRGBO(232, 226, 202, 1.0);
const Color mBGColor = Color.fromRGBO(255, 255, 255, 1.0);
const Color mLightGray = Color.fromRGBO(230, 230, 230, 1.0);
const Color mMediumGray = Color.fromRGBO(176, 176, 176, 1.0);
const Color mIntroColor = Color.fromRGBO(232, 231, 210, 1.0);
const Color mColorTransparent = Colors.transparent;
const Color mCamColor = Color.fromRGBO(210, 223, 205, 1.0);
const Color textColor = Color.fromRGBO(255, 255, 255, 1.0);

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
List<MenuItem> menuList = [
  MenuItem("메뉴1", '0001'),
  MenuItem("메뉴2", '0002'),
  MenuItem("메뉴3", '0003'),
  MenuItem("메뉴4", '0004'),
  MenuItem("메뉴5", '0005')
];
