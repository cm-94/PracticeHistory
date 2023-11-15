import 'package:flutter/material.dart';

import '../../constants/SampleColor.dart';

class BottomBar extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      height: 44,
      alignment: Alignment.center,
      decoration: BoxDecoration(
        color: SampleColor.bgGrey
      ),
        child: const Stack(
          children: [
            Center(
              child: Text("(주)바이포엠 스튜디오  BY4M STUDIO Co.,Ltd.", textScaleFactor: 1.2,),
            ),
            Positioned(
              top: 12,
              right: 280,
              child: Text("version 1.0.0")),
          ],
        ),
    );
  }
}