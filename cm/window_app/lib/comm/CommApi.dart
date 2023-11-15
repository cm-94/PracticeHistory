import 'dart:convert';
import 'dart:core';

import 'package:flutter/services.dart';

class CommApi {

  static final CommApi _instance = CommApi._();
  static CommApi get instance => _instance;

  CommApi._(){
    print("MdiController is init");
  }

  Future<List<Map<String, dynamic>>?> reqData(String type) async {




    final files = await rootBundle.loadString("assets/data/$type.json");

    if (files.isEmpty) {
      return null;
    }

    List<Map<String, dynamic>> items = List<Map<String, dynamic>>.from(jsonDecode(files));

    return items;
  }


}

