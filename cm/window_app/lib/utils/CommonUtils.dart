import 'package:decimal/decimal.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:window_app/constants/SampleColor.dart';
import 'package:window_app/manager/MenuManager.dart';

import '../constants/ToastWidget.dart';
import '../page/contents/ContentPageController.dart';

class CommonUtils {
  // /// 토스트 메시지 표시
  // static void showToast(String msg) {
  //   Fluttertoast.showToast(
  //       msg: msg,
  //       toastLength: Toast.LENGTH_SHORT,
  //       gravity: ToastGravity.CENTER,
  //       timeInSecForIosWeb: 1,
  //       backgroundColor: SampleColor.bgPrimary,
  //       textColor: SampleColor.txtBlack,
  //       fontSize: 16.0);
  // }

  /// 토스트 메시지 표시
  static void showToast({required String msg, ToastType? type}) {
    Get.dialog(
        ToastWidget(msg: msg, type : type),
        barrierDismissible: false,
        barrierColor: SampleColor.transparent
    );
  }

  ///screen_info.json데이터로 버튼위젯을 만들어 List형태로 반환
  static List<Widget> getMenuButton(){
    /// todo 파일로드 시간때문에 최초 빌드시 메뉴버튼이 생성되지 않음
    List<Widget> btnList = [];
    MenuManager.instance.mapMenus.forEach((key, value){
      print(value.title);
      btnList.add(TextButton(
          onPressed:(){
            ContentPageController.instance.movePage(key);
          },
          child: Text(value.title, style:TextStyle(color:Colors.white))
          ));
    });

    return btnList;
  }

  /// 날짜, 시간 포맷팅
  ///
  /// @param str
  /// @param type '0' : 날짜 "." 포맷팅,
  ///             '1' : 시간 ":" 포맷팅,
  ///             '2' : 날짜 "/" 포맷팅
  /// @return
  static String getDateFormat(String str, int type) {
    // 데이터 없을 경우 리턴
    if (str.length <= 0) {
      return str;
    }

    // 날짜 '.' 포맷 (사용하는 곳이 없어서 처리안함)
    if (type == 0) {
      if (str.length > 8) {
        str = str.substring(0, 8);
      }

      List<String> arrChar = [];
      str.runes.forEach((int rune) {
        var c = String.fromCharCode(rune);
        arrChar.add(c);
      });
      arrChar.insert(4, '.');
      arrChar.insert(7, '.');

      String value = '';
      arrChar.forEach((item) {
        value += item;
      });
      return value;
    }
    // 시간 ':' 포맷
    else if (type == 1) {
      if (str.contains(":")) {
        str = str.replaceAll(":", "");
      }
      if (str.length > 6) {
        str = str.substring(0, 6);
      } else if (str.length != 6) {
        return str;
      }

      List<String> arrChar = [];
      str.runes.forEach((int rune) {
        var c = String.fromCharCode(rune);
        arrChar.add(c);
      });
      arrChar.insert(2, ':');
      arrChar.insert(5, ':');

      String value = '';
      arrChar.forEach((item) {
        value += item;
      });
      return value;
    }
    // 날짜 '/' 포맷
    else if (type == 2) {
      if (str.length > 8) {
        str = str.substring(0, 8);
      } else if (str.length != 8) {
        return str;
      }

      List<String> arrChar = [];
      str.runes.forEach((int rune) {
        var c = String.fromCharCode(rune);
        arrChar.add(c);
      });
      arrChar.insert(4, '/');
      arrChar.insert(7, '/');

      String value = '';
      arrChar.forEach((item) {
        value += item;
      });
      return value;
    } else {
      return "";
    }
  }

  /// 대비 계산
  static String getChange(String price, String jsprice) {
    if (price.isEmpty || jsprice.isEmpty) {
      return '0';
    }

    try {
      final dPrice = Decimal.parse(price);
      final dJsprice = Decimal.parse(jsprice);

      final sum = dJsprice - dPrice;
      return sum.abs().toString();
    } catch (e) {
      return '0';
    }
  }

  /// 등락률 계산
  /// [price] 현재가
  /// [jsprice] 전일종가
  /// @return #.## 형태로 리턴
  static String getDiff(String price, String jsprice) {
    if (price.isEmpty || jsprice.isEmpty) {
      return '0.00';
    }

    try {
      final dPrice = Decimal.parse(price);
      final dJsprice = Decimal.parse(jsprice);

      final sum = ((dPrice - dJsprice) / dJsprice) * Decimal.parse('100');
      return sum.toStringAsFixed(2);
    } catch (e) {
      return '0.00';
    }
  }

  /// 천단위 & 소수점 표기
  /// Digit formatting and decimal display
  /// @param value
  /// @param decimal Decimal point (true decimal point, false decimal point not displayed)
  /// @return
  static String getCommaDotFormat(String value, {bool decimal = false}) {
    if (value == '' || value == null) {
      return '';
    }
    var strPrice = value;
    var strMinus = '';

    try {
      if (value.contains(',')) {
        strPrice = strPrice.replaceAll(',', '');
      }
      // '-' If present, add '-' String to strMinus and remove strPrice '-'
      if (strPrice.contains('-')) {
        strMinus = '-';
        strPrice = strPrice.replaceAll('-', '');
      }

      if (strPrice.contains('.')) {
        String strInteger;
        String strPoint;

        var strtoken = strPrice.split('.');
        strInteger = strtoken[0];
        strInteger = getThousandFormat(strInteger);

        if (decimal) {
          try {
            strPoint = strtoken[1];
            strPrice = '$strInteger.$strPoint';
          } catch (e) {
            strPrice = value;
          }
        } else {
          strPrice = strInteger;
        }
        return strMinus + strPrice;
      } else {
        strPrice = getThousandFormat(strPrice);
        return strMinus + strPrice;
      }
    } catch (e) {
      return value;
    }
  }

  /// 천단위 콤마 표기
  /// getCommaDotFormat() 함수에서만 사용 3자리 마다 ',' 넣어주기
  /// @param comma
  /// @return
  static String getThousandFormat(String? comma) {
    if (comma == null || comma.isEmpty) {
      return '';
    }

    var strComma = comma;
    // 0 일 때 true 아니면 false
    var bZero = true;
    var length = 0;

    // Removes front zeros
    for (var i = 0; i < strComma.length; i++) {
      if (comma[i] != '0') {
        bZero = false;
        break;
      } else {
        length++;
      }
    }

    strComma = (bZero == true) ? '0' : strComma.substring(length, strComma.length);

    List<String> arrComma = [];
    strComma.runes.forEach((int rune) {
      var c = String.fromCharCode(rune);
      arrComma.add(c);
    });

    bool isMinus = (arrComma[0] == '-') ? true : false;
    if (isMinus) {
      arrComma.removeAt(0);
    }
    // 소스 전체길이
    int nLength = arrComma.length;

    // 콤마 추가mod 값
    int commaIndex = -1;
    // 소스 캐릭터시작 인덱스
    int firstIndex;
    //  결과 캐릭터시작 인덱스
    int lastIndex = 0;
    List<String> result =
    List.filled((nLength + nLength / 3 + ((nLength % 3 != 0) ? 1 : 0) - 1).toInt(), '');
    commaIndex = (nLength - 1) % 3;

    for (firstIndex = 0; firstIndex < arrComma.length - 1; firstIndex++, lastIndex++) {
      result[lastIndex] = arrComma[firstIndex];
      if (firstIndex % 3 == commaIndex) {
        lastIndex++;
        result[lastIndex] = ',';
      }
    }
    result[lastIndex] = arrComma[firstIndex];
    String value = '';
    result.forEach((item) {
      value += item;
    });
    return (isMinus) ? '-' + value : value;
  }

  /// 0 체크
  /// @param s
  /// @return 0 이외에는 false (주의: 빈값은 따로 구분해줘야함)
  static bool isZero(String s) {
    if (s.isEmpty) {
      return false;
    }
    return (s is num) ? (s as num) == 0 : false;
  }

  /// 해당 Context 크기 가져옴
  /// [key] Widget Key
  static getSize(GlobalKey key) {
    if (key.currentContext != null) {
      final RenderBox renderBox = key.currentContext!.findRenderObject() as RenderBox;
      Size size = renderBox.size;
      return size;
    }
  }
}