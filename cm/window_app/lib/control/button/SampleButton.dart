import 'package:flutter/material.dart';

import '../../constants/SampleColor.dart';
import '../../constants/SampleWidget.dart';

/// 공통 버튼
class SampleButton extends StatelessWidget {
  final double? height;
  final double? width;
  final EdgeInsetsGeometry? margin;
  final EdgeInsetsGeometry? padding;
  final Decoration? decoration;
  final bool isDecoration;
  final Color backgroundColor; // 배경 색상
  final double radius;
  final String text;
  final Color textColor; // 텍스트 색상
  final double textSize; // 텍스트 사이즈
  final FontWeight textWeight; // 텍스트 두께
  final GestureTapCallback? onTap;
  final Color borderColor;
  final double borderWidth;
  final bool isRipple; // 배경 색상
  final Widget? child;

  SampleButton({
    Key? key,
    this.height = 50,
    this.width,
    this.margin,
    this.padding,
    Decoration? decoration,
    this.isDecoration = true,
    this.backgroundColor = Colors.transparent,
    this.radius = 0,
    this.text = '',
    this.textColor = Colors.black,
    this.textSize = 16,
    this.textWeight = FontWeight.normal,
    this.onTap,
    this.borderColor = Colors.transparent,
    this.borderWidth = 1,
    this.isRipple = true,
    this.child,
  })  : assert(margin == null || margin.isNonNegative),
        assert(padding == null || padding.isNonNegative),
        this.decoration = decoration == null
            ? !isDecoration
                ? null
                : BoxDecoration(
                    color: backgroundColor,
                    borderRadius: BorderRadius.circular(radius),
                    border: Border.all(color: borderColor, width: borderWidth))
            : decoration,
        super(key: key);

  _getButton() {
    return InkWell(
      borderRadius: BorderRadius.circular(radius),
      splashColor: isRipple ? null : SampleColor.transparent,
      highlightColor: isRipple ? null : SampleColor.transparent,
      child: Container(
        padding: padding,
        child: Center(
          child: child != null
              ? child
              : addTextWidget(
                  text,
                  color: textColor,
                  fontSize: textSize,
                  fontWeight: textWeight,
                  align: TextAlign.center,
                ),
        ),
      ),
      onTap: () {
        onTap?.call();
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      height: height,
      width: width,
      margin: margin,
      decoration: decoration,
      child: Material(
        color: SampleColor.transparent,
        child: _getButton(),
      ),
    );
  }
}
