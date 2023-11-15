import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:window_app/constants/SampleColor.dart';
import 'package:window_app/utils/CommonUtils.dart';
import '../../control/button/SampleButton.dart';
import 'LoginPageController.dart';


class LoginPage extends StatelessWidget {
  LoginPageController controller = Get.put(LoginPageController());
  TextEditingController idCtrl = TextEditingController();
  TextEditingController pwdCtrl = TextEditingController();
  FocusNode secondFocusNode = FocusNode();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: SampleColor.bgWhite,
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          SizedBox(
            width: double.infinity,
            height: 200,
            child: Image.asset("assets/images/banner.webp"),
          ),
          SizedBox(
            width: 400,
            child: Column(
              children: [
                /// "아이디"
                Container(
                  width: 400,
                  height: 28,
                  padding: const EdgeInsets.only(left: 4),
                  margin: const EdgeInsets.only(top: 40,bottom: 8),
                  child: const Text(
                      "아이디",
                      textAlign: TextAlign.left,
                      style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)
                  ),
                ),
                /// ID Input
                Container(
                  width: 400,
                  height: 52,
                  margin: const EdgeInsets.only(bottom: 20),
                  decoration: BoxDecoration(
                      color: SampleColor.bgGrey,
                      borderRadius: const BorderRadius.all(Radius.circular(8))
                  ),
                  child: TextField(
                    controller: idCtrl,
                    autofocus:true,
                    style: const TextStyle(fontSize: 16),
                    decoration: const InputDecoration(
                      hintText: '그룹웨어 아이디를 입력하세요',
                      border: InputBorder.none,
                      isDense: true,
                      contentPadding: EdgeInsets.only(left: 12, top: 20, bottom: 20),
                    ),
                    textInputAction: TextInputAction.next,
                    onSubmitted: (value) async {
                      secondFocusNode.requestFocus();
                    },
                  ),
                ),
                /// "비밀번호"
                Container(
                  width: 400,
                  height: 28,
                  padding: const EdgeInsets.only(left: 4),
                  margin: const EdgeInsets.only(bottom: 8),
                  child: const Text(
                      "비밀번호",
                      textAlign: TextAlign.left,
                      style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)
                  ),
                ),
                /// PWD Input
                Container(
                  width: 400,
                  height: 52,
                  margin: const EdgeInsets.only(bottom: 44),
                  decoration: BoxDecoration(
                      color: SampleColor.bgGrey,
                      borderRadius: const BorderRadius.all(Radius.circular(8))
                  ),
                  child: TextField(
                    controller: pwdCtrl,
                    focusNode: secondFocusNode,
                    style: const TextStyle(fontSize: 16),
                    obscureText: true,
                    decoration: const InputDecoration(
                      hintText: '그룹웨어 비밀번호를 입력하세요',
                      border: InputBorder.none,
                      isDense: true,
                      contentPadding: EdgeInsets.only(left: 12, top: 20, bottom: 20),
                    ),
                    textInputAction: TextInputAction.go,
                    onSubmitted: (value) async {
                      controller.rqLogin(idCtrl.text, pwdCtrl.text);
                    },

                  ),
                ),
                SampleButton(
                  text: "로그인",
                  height: 56,
                  textSize: 20,
                  textWeight: FontWeight.bold,
                  isRipple: false,
                  radius: 12,
                  backgroundColor: SampleColor.bgPrimary,
                  textColor: SampleColor.txtWhite,
                  onTap: () {
                    if(idCtrl.text.isEmpty){
                      CommonUtils.showToast(msg: "아이디를 입력해주세요");
                    }
                    else if(pwdCtrl.text.isEmpty){
                      CommonUtils.showToast(msg: "비밀번호를 입력해주세요");
                    }
                    else{
                      controller.rqLogin(idCtrl.text, pwdCtrl.text);
                    }

                  },
                ),

              ],
            ),
          )
        ],
      )
    );
  }
}


