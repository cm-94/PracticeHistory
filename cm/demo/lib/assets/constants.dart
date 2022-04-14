import 'dart:async';
import 'dart:convert';
import 'package:auto_size_text/auto_size_text.dart';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:encrypt/encrypt.dart' as Encrypt;
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:intl/intl.dart';

/// 공통색상
const Color mBaseColor = Color.fromRGBO(252, 200, 10, 1.0);
const Color mBGColor = Color.fromRGBO(255, 255, 255, 1.0);
const Color mLightGray = Color.fromRGBO(230, 230, 230, 1.0);
const Color mMediumGray = Color.fromRGBO(176, 176, 176, 1.0);
const Color mIntroColor = Color.fromRGBO(232, 231, 210, 1.0);

showToast(BuildContext context, Widget child) {
  FToast().init(context).showToast(child: child);
}

