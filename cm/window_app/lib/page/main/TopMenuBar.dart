import 'dart:ffi';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:window_app/constants/SampleColor.dart';
import 'package:window_app/manage/GlobalController.dart';

import '../../constants/Constants.dart';
import '../../constants/SampleWidget.dart';
import '../../control/button/SampleButton.dart';
import '../contents/ContentPageController.dart';


class TopMenuBar extends StatelessWidget {
  GlobalController globalCtrl = Get.find();
  // final MdiController mdiController;
  // const TopMenuBar({required this.mdiController}) : super();

  @override
  Widget build(BuildContext context) {
    return Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children : [
          /// 홈버튼
          SampleButton(margin: EdgeInsets.only(right: 20), child: const Icon(Icons.home), onTap: () {
            ContentPageController.instance.movePage(Constants.PAGE_HOME_CD);
          }),
          /// 선택 회사명
          Obx(() {
            var currComp = globalCtrl.currComp.value;
            return SizedBox(
              width: 200,
              child: Text(currComp.companyName, style: const TextStyle(color: SampleColor.txtBlack, fontSize: 20, fontWeight: FontWeight.bold)),
            );
          }),
          /// 메뉴 목록
          Expanded(
            child: Obx((){
              var currPage = globalCtrl.currPageCd.value;
              var defaultStyle = const TextStyle(color: SampleColor.txtGrey2, fontSize: 20);
              var currStyle = const TextStyle(color: SampleColor.txtWhite, fontSize: 24, fontWeight: FontWeight.bold);
              return Row(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  children:
                  [
                    // Text("사업 현황", style: (currPage == Constants.PAGE_BUSINESS)? currStyle : defaultStyle),
                    // Text("월 보고", style: (currPage == Constants.PAGE_MONTH)? currStyle : defaultStyle),
                    // Text("결의사항", style: (currPage == Constants.PAGE_CURRECT)? currStyle : defaultStyle),
                    // Text("제반문서", style: (currPage == Constants.PAGE_PAGER)? currStyle : defaultStyle),
                    TextButton(
                      onPressed:(){
                        ContentPageController.instance.movePage(Constants.PAGE_BUSINESS_CD);
                      },
                      child : Text("사업 현황", style: (currPage == Constants.PAGE_BUSINESS_CD)? currStyle : defaultStyle),
                    ),
                    TextButton(
                      onPressed:(){
                        ContentPageController.instance.movePage(Constants.PAGE_MONTH_CD);
                      },
                      child : Text("월 보고", style: (currPage == Constants.PAGE_MONTH_CD)? currStyle : defaultStyle),
                    ),
                    TextButton(
                      onPressed:(){
                        ContentPageController.instance.movePage(Constants.PAGE_RESOLUTE_CD);
                      },
                      child : Text("결의사항", style: (currPage == Constants.PAGE_RESOLUTE_CD)? currStyle : defaultStyle),
                    ),
                    TextButton(
                      onPressed:(){
                        ContentPageController.instance.movePage(Constants.PAGE_PAGER_CD);
                      },
                      child : Text("제반문서", style: (currPage == Constants.PAGE_PAGER_CD)? currStyle : defaultStyle),
                    ),

                  ]
              );
            }),
          ),
          Container(
            width: 200,
            child: Row(
              children: [
                /// 로그아웃
                addButtonWidget("로그아웃", fontSize: 16, callback: (){
                  ContentPageController.instance.movePage(Constants.PAGE_HOME_CD);
                  Get.offAllNamed("/loginPage");
                }),
                SampleButton(child: const Icon(Icons.exit_to_app), onTap: () => Get.offAllNamed("/loginPage")),
                SizedBox(width: 12,),
                /// 종료웃
                addButtonWidget("종료",fontSize: 16, callback: () => exit(0)),
                SampleButton(child: const Icon(Icons.radio_button_checked),onTap: () => exit(0)),
              ],
            ),
          )
        ]
    );
  }

  Widget popUpMenu(String name){
    return PopupMenuButton(
      itemBuilder: (context) {
        return [
          PopupMenuItem(
              value: 'edit',
              child: Text('수정')),
          PopupMenuItem(
              value: 'delete',
              child: Text('삭제')),
        ];
      },
      onSelected: (value) {
        switch (value) {
          case 'edit':
            print('edit');
            break;
          case 'delete':
            print('delete');
            break;
        }
      },
    );
  }

}