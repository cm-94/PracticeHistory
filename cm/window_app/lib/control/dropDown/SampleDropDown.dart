import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:window_app/control/button/SampleButton.dart';

import '../../constants/SampleColor.dart';

class SampleDropDown<T> extends StatelessWidget {
  late BuildContext context;
  final Widget Function(int) child;

  /// list of DropdownItems
  final List<DropdownItem<T>> items;

  /// 선택 이벤트
  final void Function(T, int) onChange;
  final void Function(bool)? isShowing;

  /// dropdownButtonStyles passes styles to OutlineButton.styleFrom()
  final DropdownButtonStyle dropdownButtonStyle;
  final DropdownStyle dropdownStyle;

  /// dropdown button icon defaults to caret
  final Icon? icon;
  final Icon? iconSec;
  final bool hideIcon;

  /// if true the dropdown icon will as a leading icon, default to false
  final bool leadingIcon;

  final LayerLink _layerLink = LayerLink();
  late OverlayEntry _overlayEntry;
  var _isOpen = false.obs;
  int _currentIndex = 0;

  SampleDropDown({
    Key? key,
    required this.child,
    required this.items,
    required this.onChange,
    this.isShowing,
    this.dropdownButtonStyle = const DropdownButtonStyle(),
    this.dropdownStyle = const DropdownStyle(),
    this.icon,
    this.iconSec,
    this.hideIcon = false,
    this.leadingIcon = false,
  }) : super(key: key);

  OverlayEntry _createOverlayEntry() {
    // find the size and position of the current widget
    RenderBox renderBox = context.findRenderObject() as RenderBox;
    var size = renderBox.size;

    var offset = renderBox.localToGlobal(Offset.zero);
    var topOffset = offset.dy + size.height + 5;
    return OverlayEntry(
      // full screen GestureDetector to register when a
      // user has clicked away from the dropdown
      builder: (context) => GestureDetector(
        onTap: () => _toggleDropdown(close: true),
        behavior: HitTestBehavior.translucent,
        // full screen container to register taps anywhere and close drop down
        child: Container(
          height: MediaQuery.of(context).size.height,
          width: MediaQuery.of(context).size.width,
          child: Stack(
            children: [
              Positioned(
                left: offset.dx,
                top: topOffset,
                width: dropdownStyle.width ?? size.width,
                child: CompositedTransformFollower(
                  offset: dropdownStyle.offset ?? Offset(0, size.height + 5),
                  link: this._layerLink,
                  showWhenUnlinked: false,
                  child: Material(
                    elevation: dropdownStyle.elevation ?? 0,
                    borderRadius: dropdownStyle.borderRadius ?? BorderRadius.zero,
                    color: SampleColor.transparent,
                    child: ConstrainedBox(
                      constraints: dropdownStyle.constraints ??
                          BoxConstraints(
                            maxHeight: MediaQuery.of(context).size.height - topOffset - 15,
                          ),
                      child: Container(
                        margin: dropdownStyle.margin,
                        color: dropdownStyle.color,
                        decoration: dropdownStyle.decoration,
                        child: ListView(
                          padding: dropdownStyle.padding ?? EdgeInsets.zero,
                          shrinkWrap: true,
                          children: items.asMap().entries.map((item) {
                            return InkWell(
                              onTap: () {
                                _currentIndex = item.key;
                                onChange(item.value.value, item.key);
                                _toggleDropdown();
                              },
                              child: item.value,
                            );
                          }).toList(),
                        ),
                      ),
                    ),
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  /// Toggle
  void _toggleDropdown({bool close = false}) async {
    if (_isOpen.value || close) {
      this._overlayEntry.remove();
      _isOpen.value = false;

      if (isShowing != null) {
        isShowing!(false);
      }
    } else {
      this._overlayEntry = this._createOverlayEntry();
      Overlay.of(context)!.insert(this._overlayEntry);
      _isOpen.value = true;

      if (isShowing != null) {
        isShowing!(true);
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    this.context = context;
    var style = dropdownButtonStyle;
    // link the overlay to the button
    return CompositedTransformTarget(
      link: this._layerLink,
      child: SampleButton(
        width: style.width,
        height: style.height,
        margin: style.margin,
        padding: style.padding,
        decoration: style.decoration,
        isRipple: style.isRipple,
        child: Stack(
          children: [
            Align(
              child: child(_currentIndex),
            ),
            if (!hideIcon)
              Align(
                alignment: leadingIcon ? Alignment.centerLeft : Alignment.centerRight,
                child: icon ??
                    Obx(() => Icon(!_isOpen.value ? Icons.arrow_drop_down : Icons.arrow_drop_up)),
              ),
          ],
        ),
        onTap: _toggleDropdown,
      ),
    );
  }
}

/// DropdownItem is just a wrapper for each child in the dropdown list.\n
/// It holds the value of the item.
class DropdownItem<T> extends StatelessWidget {
  final T value;
  final Widget child;

  const DropdownItem({
    Key? key,
    required this.value,
    required this.child,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return child;
  }
}

class DropdownButtonStyle {
  final double? width;
  final double? height;
  final EdgeInsetsGeometry? margin;
  final EdgeInsetsGeometry? padding;
  final Decoration? decoration;
  final bool isRipple; // 리플 사용 여부

  const DropdownButtonStyle({
    this.height,
    this.width,
    this.margin,
    this.padding,
    this.decoration,
    this.isRipple = true,
  });
}

class DropdownStyle {
  ///button width must be set for this to take effect
  final double? width;
  final Color? color;
  final EdgeInsets? margin;
  final EdgeInsets? padding;
  final Decoration? decoration;

  /// position of the top left of the dropdown relative to the top left of the button
  final Offset? offset;
  final BoxConstraints? constraints;
  final BorderRadius? borderRadius;
  final double? elevation;

  const DropdownStyle({
    this.width,
    this.color,
    this.margin,
    this.padding,
    this.decoration,
    this.constraints,
    this.offset,
    this.borderRadius,
    this.elevation,
  });
}
