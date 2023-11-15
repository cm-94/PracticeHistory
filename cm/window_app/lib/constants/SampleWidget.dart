import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:get/get_rx/src/rx_types/rx_types.dart';
import 'package:get/get_state_manager/src/rx_flutter/rx_obx_widget.dart';
import 'package:window_app/constants/SampleColor.dart';
import 'package:window_app/control/button/SampleButton.dart';

Widget addTextWidget(String? str,
    {Color? color,
    double fontSize = 14,
    FontWeight fontWeight = FontWeight.normal,
    int? maxLines,
    TextAlign align = TextAlign.left,
    TextDecoration decoration = TextDecoration.none,
    double? height}) {
  color ??= SampleColor.blue;
  if (str == null || str.isEmpty) {
    return Container();
  }

  return Text(
    str,
    maxLines: maxLines,
    textScaleFactor: 1.0,
    overflow: TextOverflow.ellipsis,
    textAlign: align,
    style: TextStyle(
        color: color,
        fontSize: fontSize,
        fontWeight: fontWeight,
        decoration: decoration,
        height: height),
  );
}
Widget addButtonWidget ( String ? text,
  {
    Color? bgColor,
    Color? txtColor,
    GestureTapCallback? callback,
    String type = "confirm",
    double fontSize = 14,
    double? height = 50}) {
    bgColor ??= SampleColor.bgPrimary;
    txtColor ??= SampleColor.txtWhite;

  if (text == null || text.isEmpty) {
    return Container();
  }
  return Container(
    margin: EdgeInsets.fromLTRB(10, 0, 10, 0),
    height: height,
    decoration: BoxDecoration(
      color: bgColor,
      borderRadius: BorderRadius.circular(12),
      // border: Border.all(color: borderColor, width: borderWidth)),
    ),
    child: InkWell(
      borderRadius: BorderRadius.circular(12),
      child: Center(
        child: addTextWidget(
          text,
          color: txtColor,
          fontSize: fontSize,
          align: TextAlign.center,
        ),
      ),
      onTap: () {
        callback?.call();
      },
    )
  );
}

Widget getTopMenuSearch({required RxList<String> comboList, required RxString selectedData, required Function(String value) onSearchClick}){
  TextEditingController txtCtrl = TextEditingController();
  return Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          //계좌번호 출력
          Container(
            width:125, height: 48,
            color: SampleColor.bgWhite,
            margin: const EdgeInsets.only(right: 20),
            child: Obx(() =>  DropdownButtonHideUnderline(
              child: ButtonTheme(
                child: DropdownButton<String>(
                  isExpanded: true,
                  value: selectedData.value,
                  items: comboList.map((String value){
                    return DropdownMenuItem<String>(
                        value: value,
                        child: Text(value, textAlign: TextAlign.center,)
                    );
                  }).toList(),
                  onChanged: (value){
                    selectedData.value = value!;
                  },
                ),
              ),
            ))
          ),

          //비밀번호 필드
          Expanded(
            child: Container(
                height: 40,
                child:TextField(
                  controller: txtCtrl,
                  maxLines: 1,
                  showCursor: false,
                  //controller: textController,
                  decoration: const InputDecoration(
                    //내부컨텐츠 패딩 조절
                    contentPadding: EdgeInsets.fromLTRB(10, 0, 10, 0),
                    counterText: "",
                    //포커스 상태 일 때
                    focusedBorder: OutlineInputBorder(
                        borderSide: BorderSide(color: Colors.black, width: 0.0),
                        borderRadius: BorderRadius.all(Radius.circular(6.0))
                    ),
                    //대기 상태 일 때
                    enabledBorder: OutlineInputBorder(
                        borderSide: BorderSide(color: Colors.black54, width: 0.0),
                        borderRadius: BorderRadius.all(Radius.circular(6.0))
                    ),

                  ),
                )
            ),
          ),
          const SizedBox(width: 20,),
          SampleButton(
            text: "검색",
            width: 80,
            height: 40,
            textColor: SampleColor.txtWhite,
            decoration: const BoxDecoration(
              color: SampleColor.bgBlack,
              borderRadius: BorderRadius.all(Radius.circular(8))
            ),
            onTap: () => onSearchClick(txtCtrl.text)),
        ]
    );
}

