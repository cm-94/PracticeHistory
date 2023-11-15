
//
// /// 전역 페이지 컨트롤러(앱 기동중 계속 생존)
// class GlobalController extends BasePageController<GlobalController> {
//   static const TAG ='GlobalController';
//
//   var selectedIndex = 0.obs;
//   var selectedTabIndex = 0.obs;
//
//   var currentAppbar = Rx<AppBar?>(null);
//   //var currentWidget = Rx<BasePage>(HomePage());
//
//   /// AppBar 설정
//   setAppBar(AppBar? appBar){
//     currentAppbar.value = appBar;
//   }
//
//   /// Body Widget 설정
//   setCurrWidget(BasePage widget){
//     //currentWidget.value = widget;
//   }
//
//   @override
//   String getControllerTag() {
//     return TAG;
//   }
// }