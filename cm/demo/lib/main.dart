import 'package:camerademo/pages/CameraPage.dart';
import 'package:camerademo/pages/IntroPage.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:splashscreen/splashscreen.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: SplashScreen(
        seconds: 3,
        navigateAfterSeconds:CameraPage(),
        title: const Text(
          'Camera Demo',
          style: TextStyle(
              fontWeight: FontWeight.bold,
              fontSize: 20.0,
              color: Colors.white),
        ),
        backgroundColor: Colors.lightBlue[200],
      )
    );
  }
}