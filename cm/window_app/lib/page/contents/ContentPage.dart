import 'package:flutter/material.dart';
import 'package:get/get.dart';

import 'ContentPageController.dart';

class ContentPage extends StatelessWidget {

  //final MdiController mdiController;
  //const MdiManager({required this.mdiController}) : super();

  @override
  Widget build(BuildContext context) {
    return Obx(() => ContentPageController.instance.currPage.value);
  }

}
