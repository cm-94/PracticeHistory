class LOGCAT {
  static const bool isEnable = true;
  static const bool isDebugEnable = true;
  static const bool isVervoseEnable = true;
  static const bool isErrorEnable = true;

  static void d(String tag, String msg) {
    if (isEnable && isDebugEnable) {
      print("D|[$tag]$msg");
    }
  }

  static void v(String tag, String msg) {
    if (isEnable && isVervoseEnable) {
      print("V|[$tag]$msg");
    }
  }

  static void e(String tag, String msg) {
    if (isEnable && isDebugEnable) {
      print("E|[$tag]$msg");
    }
  }

  static void printStackTrace(Exception e) {
    if (isEnable && isDebugEnable) {
      print("E|[printStackTrace]${e.toString()}");
    }
  }
}
