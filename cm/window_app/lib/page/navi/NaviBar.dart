import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:hover_menu/hover_menu.dart';
import 'package:window_app/manage/GlobalController.dart';
import 'package:window_app/model/CompanyItem.dart';
import 'package:window_app/page/navi/NaviBarController.dart';
import 'package:window_app/utils/CommonUtils.dart';

import '../../constants/Constants.dart';
import '../../constants/SampleColor.dart';

class NaviBar extends StatelessWidget{
  NaviBarController naviCtrl = Get.put(NaviBarController());
  GlobalKey<ScaffoldState> scaffoldKey;
  NaviBar({super.key, required this.scaffoldKey});

  GlobalController globalCtrl = Get.find();

  List<ExpansionTileController> menuCtrls = [];

  _getMenuTile(IconData icon, CompanyItem companyItem){
    ExpansionTileController vCtrl = ExpansionTileController();
    menuCtrls.add(vCtrl);

    return ExpansionTile(
      controller: vCtrl,
      leading: Icon(icon),
      title: Text(companyItem.companyName),
      onExpansionChanged: (value){
        // print("Company :: $compCode / $compName");
        for(var inCtrl in menuCtrls){
          if(naviCtrl.currNavi.value.isEmpty){
            naviCtrl.currNavi.value = companyItem.companyCode;
            return;
          }
          if(naviCtrl.currNavi.value == companyItem.companyCode){
            return;
          }
          if(inCtrl != vCtrl){
            inCtrl.collapse();
          }

        }
        naviCtrl.currNavi.value = companyItem.companyCode;
      },
      maintainState: true,
      children: [
        ListTile(title: const Text('사업 현황'),onTap: () {
          naviCtrl.movePage(companyItem, Constants.PAGE_BUSINESS_CD);
          if(scaffoldKey.currentState!.isDrawerOpen){
            scaffoldKey.currentState!.closeDrawer();
          }
        }),
        ListTile(title: const Text('월 보고'),onTap: () {
          naviCtrl.movePage(companyItem, Constants.PAGE_MONTH_CD);
          if(scaffoldKey.currentState!.isDrawerOpen){
            scaffoldKey.currentState!.closeDrawer();
          }
        }),
        ListTile(title: const Text('결의 사항'),onTap: () {
          naviCtrl.movePage(companyItem, Constants.PAGE_RESOLUTE_CD);
          if(scaffoldKey.currentState!.isDrawerOpen){
            scaffoldKey.currentState!.closeDrawer();
          }
        }),
        ListTile(title: const Text('제반 문서'),onTap: () {
          naviCtrl.movePage(companyItem, Constants.PAGE_PAGER_CD);
          if(scaffoldKey.currentState!.isDrawerOpen){
            scaffoldKey.currentState!.closeDrawer();
          }
        }),
      ],
    );
    // return ListTile(
    //   contentPadding: EdgeInsets.only(left: 12),
    //   minVerticalPadding: 0.0,
    //   leading: Icon(icon),
    //   horizontalTitleGap: 0,
    //   title: HoverMenu(
    //     title: Text(compName),
    //     items: [
    //       ListTile(title: Text('사업 현황')),
    //       ListTile(title: Text('월 보고')),
    //       ListTile(title: Text('결의 사항')),
    //       ListTile(title: Text('제반 문서')),
    //     ],
    //   ),
    //   onTap: (){
    //     /// TODO: 회사 변경 => compCode;
    //     CommonUtils.showToast(msg: "$compName 페이지는 추가 예정입니다.");
    //     if(scaffoldKey.currentState!.isDrawerOpen){
    //       scaffoldKey.currentState!.closeDrawer();
    //     }
    //   },
    // );
  }

  List<Widget> getCompanyList(){
    var result = <Widget>[];
    /// Drawer 상단
    result.add(DrawerHeader(
      decoration: BoxDecoration(
          border: Border.all(color: SampleColor.transparent, width:0),
          image: const DecorationImage(
            opacity: 0.3,
            image: AssetImage("assets/images/banner.webp"),
          )
      ),
      child: Container(
        width: 400,
        height: 300,
        child: Column(
          mainAxisAlignment: MainAxisAlignment.end,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text("반갑습니다. 김포엠님", style: TextStyle(color: SampleColor.txtBlack, fontSize: 20, fontWeight: FontWeight.bold)),
            Text("${globalCtrl.getUserId()}@by4m.com", style: TextStyle(color: SampleColor.txtBlack, fontSize: 20, fontWeight: FontWeight.bold))
          ],
        ),
      ),
    ));

    /// Drawer 하단
    for(var company in globalCtrl.arrCompany){
      result.add(
        _getMenuTile(
          company.companyCode == "0000"? Icons.home : const IconData(0xe11b, fontFamily: 'MaterialIcons'),
          company
        )
      );/// 아이콘 / 사명 / 회사 / 구분코드
      result.add(const Divider(height: 1));
    }
    return result;
  }

  @override
  Widget build(BuildContext context) {
    menuCtrls = [];
    return Drawer(
      clipBehavior: Clip.none,
      // backgroundColor: SampleColor.transparent,
      child: ListView(
        padding: EdgeInsets.zero,
        children: getCompanyList()
        // [
        //   DrawerHeader(
        //     decoration: BoxDecoration(
        //       border: Border.all(color: SampleColor.transparent, width:0),
        //       image: const DecorationImage(
        //         opacity: 0.3,
        //         image: AssetImage("assets/images/banner.webp"),
        //       )
        //     ),
        //     child: Container(
        //       width: 400,
        //       height: 300,
        //       child: Column(
        //         mainAxisAlignment: MainAxisAlignment.end,
        //         crossAxisAlignment: CrossAxisAlignment.start,
        //         children: [
        //           Text("반갑습니다. 김포엠님", style: TextStyle(color: SampleColor.txtBlack, fontSize: 20, fontWeight: FontWeight.bold)),
        //           Text("$userId@by4m.com", style: TextStyle(color: SampleColor.txtBlack, fontSize: 20, fontWeight: FontWeight.bold))
        //         ],
        //       ),
        //     ),
        //   ),
        //   _getMenuTile(Icons.home, "전체", Constants.COMP_CODE_ALL), /// 아이콘 / 사명 / 회사 / 구분코드
        //   const Divider(height: 1),
        //   _getMenuTile(IconData(0xe11b, fontFamily: 'MaterialIcons'), "(주)바이포엠스튜디오", Constants.COMP_CODE_BY4M),
        //   const Divider(height: 1),
        //   _getMenuTile(IconData(0xe11b, fontFamily: 'MaterialIcons'), "(주)디앤비아이앤씨", Constants.COMP_CODE_DNBINC),
        //   const Divider(height: 1),
        //   _getMenuTile(IconData(0xe11b, fontFamily: 'MaterialIcons'), "(주)메이저나인", Constants.COMP_CODE_MAJ9),
        //   const Divider(height: 1),
        //   _getMenuTile(IconData(0xe11b, fontFamily: 'MaterialIcons'), "(주)바이포엠에프앤티", Constants.COMP_CODE_BY4MFNT),
        //   const Divider(height: 1),
        //   _getMenuTile(IconData(0xe11b, fontFamily: 'MaterialIcons'), "(주)네이처라우드", Constants.COMP_CODE_NATURE),
        //   const Divider(height: 1),
        //   _getMenuTile(IconData(0xe11b, fontFamily: 'MaterialIcons'), "(주)더궈", Constants.COMP_CODE_THE),
        //   const Divider(height: 1),
        //   _getMenuTile(IconData(0xe11b, fontFamily: 'MaterialIcons'), "(주)무빙픽쳐스컴퍼니", Constants.COMP_CODE_MOVING),
        //   const Divider(height: 1),
        // ],
      ),
    );
  }
}