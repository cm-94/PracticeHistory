import 'package:flutter/material.dart';

import '../assets/constants.dart';

/// //////////////////
/// 카메라 메인 화면 ///
/// //////////////////
class CameraPage extends StatefulWidget {
  @override
  CameraPageState createState() => CameraPageState();
}

class CameraPageState extends State<CameraPage> {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    double width = (MediaQuery.of(context).size.width) / 2;

    return Scaffold(
      body: SafeArea(
        child: Container(
          padding: EdgeInsets.only(bottom: width),
          color: mIntroColor,
          child: Center(
            child: SizedBox(
              width: width,
              child: const Text("카메라 메인"),
            ),
          ),
        ),
      ),
    );
  }
}