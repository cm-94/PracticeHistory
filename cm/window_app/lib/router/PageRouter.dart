
import 'package:get/get.dart';
import 'package:window_app/page/DummyPage.dart';
import 'package:window_app/page/main/MainPage.dart';
import '../constants/ToastWidget.dart';
import '../page/contents/business/BusinessPage.dart';
import '../page/contents/currect/ResolutePage.dart';
import '../page/contents/home/HomePage.dart';
import '../page/contents/month/MonthPage.dart';
import '../page/contents/paper/PaperPage.dart';
import '../page/login/LoginPage.dart';
import '../page/splash/SplashPage.dart';

/// PageRouter
/// 화면 ( page ) 및 이름 ( ex. /splashPage ), 전환 효과 ( transition ) 등 정의
class PageRouter {
  static final route = [
    // 스플레시 페이지
    GetPage(
        name: '/splashPage',
        page: () => SplashPage(),
        transition: Transition.fadeIn
    ),
    // 로그인페이지
    GetPage(
        name: '/loginPage',
        page: () => LoginPage(),
        transition: Transition.fadeIn
    ),
    // 메인페이지
    GetPage(
        name: '/mainPage',
        page: () => MainPage(),
        transition: Transition.fadeIn
    ),
    // 홈페이지
    GetPage(
        name: '/HomePage',
        page: () => HomePage(),
    ),
    // 사업현황 페이지
    GetPage(
        name: '/BusinessPage',
        page: () => BusinessPage(),
    ),
    // 월 보고 페이지
    GetPage(
        name: '/MonthPage',
        page: () => MonthPage(),
    ),
    // 결의사항 페이지
    GetPage(
        name: '/ResolutePage',
        page: () => ResolutePage(),
    ),
    // 제반문서 페이지
    GetPage(
        name: '/PaperPage',
        page: () => PaperPage(),
    ),
    // 더미 페이지
    GetPage(
        name: '/dummyPage',
        page: () => DummyPage(),
        transition: Transition.upToDown
    ),
  ];

  static GetPageBuilder getPage(String screenKey){
    //route[0].name;
    var page;
    route.forEach((item) {
      if( item.name == "/$screenKey"){
        page = item;
      }
    });
    return page.page;
  }

}

