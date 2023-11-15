
import 'package:flutter/material.dart';
import 'package:window_app/base/BasePage.dart';
import 'package:window_app/constants/SampleColor.dart';

import 'DummyPageController.dart';

class DummyPage extends BasePage<DummyPageController>{

  @override
  getController() {
    return DummyPageController();
  }

  //DummyPage();

  @override
  Widget getContent() {
    return Container(
        color: Colors.white,
        child: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Icon(Icons.warning, size: 100.0, color: SampleColor.primary),
              Text("서비스 준비중 입니다.", style: TextStyle(fontSize: 20),)
            ],
          ),
        )
      //color: SampleColor.primary
    );
  }

}