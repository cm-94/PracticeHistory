/// 차트 확대 축소 타입
///    - right : 오른쪽 기준으로 변경
///    - hand : 이동하는 포인트 기준으로 변경
///    - middle : 두 포인트의 가운데 기준으로 변경
enum CAMERA_TYPE { CAMERA, VIDEO }
enum FILTER_TYPE { DEFUALT, BACKGROUND, TEXT, NONE }
enum MENU_TYPE { START, EDIT, MENU, CAPTURE }
enum SIZE_TYPE { DEFAULT, THREETOFOUR, NINETOSIXTEEN, FULL }

class CameraOption {
  static const String TYPE_CAMERA = "TYPE_CAMERA";
  static const String TYPE_VIDEO = "TYPE_VIDEO";

  static const String FILTER_DEFAULT = "FILTER_DEFAULT";
  static const String FILTER_BACKGROUND = "FILTER_BACKGROUND";
  static const String FILTER_COVER = "FILTER_COVER";
  static const String FILTER_TEXT = "FILTER_TEXT";
  static const String FILTER_NONE = "FILTER_NONE";

  static const String TYPE_START = "TYPE_START";
  static const String TYPE_EDIT = "TYPE_EDIT";
  static const String TYPE_MENU = "TYPE_MENU";
  static const String TYPE_CAPTURE = "TYPE_CAPTURE";

  static const String SIZE_DEFAULT = "1:1";
  static const String SIZE_THREETOFOUR = "3:4";
  static const String SIZE_NINETOSIXTEEN = "9:16";
  static const String SIZE_FULL = "FULL";

  static String convertOptionString(Object object) {
    if (object is CAMERA_TYPE) {
      switch (object) {
        case CAMERA_TYPE.CAMERA:
          return TYPE_CAMERA;
        case CAMERA_TYPE.VIDEO:
          return TYPE_VIDEO;
      }
    }

    if (object is FILTER_TYPE) {
      switch (object) {
        case FILTER_TYPE.DEFUALT:
          return FILTER_DEFAULT;
        case FILTER_TYPE.BACKGROUND:
          return FILTER_BACKGROUND;
        case FILTER_TYPE.TEXT:
          return FILTER_TEXT;
        case FILTER_TYPE.NONE:
          return FILTER_NONE;
      }
    }

    if(object is MENU_TYPE){
      switch(object){
        case MENU_TYPE.START:
          return TYPE_START;
        case MENU_TYPE.EDIT:
          return TYPE_EDIT;
        case MENU_TYPE.MENU:
          return TYPE_MENU;
        case MENU_TYPE.CAPTURE:
          return TYPE_CAPTURE;
      }
    }

    if(object is SIZE_TYPE){
      switch(object){
        case SIZE_TYPE.DEFAULT:
          return SIZE_DEFAULT;
        case SIZE_TYPE.THREETOFOUR:
          return SIZE_THREETOFOUR;
        case SIZE_TYPE.NINETOSIXTEEN:
          return SIZE_NINETOSIXTEEN;
        case SIZE_TYPE.FULL:
          return SIZE_FULL;
      }
    }
    return '';
  }

  List<String> getSizeList(){
    return [SIZE_DEFAULT, SIZE_THREETOFOUR, SIZE_NINETOSIXTEEN/*, SIZE_FULL*/];
  }

  SIZE_TYPE getSizeOption(String type){
    switch(type){
      case SIZE_DEFAULT:
        return SIZE_TYPE.DEFAULT;
      case SIZE_THREETOFOUR:
        return SIZE_TYPE.THREETOFOUR;
      case SIZE_NINETOSIXTEEN:
        return SIZE_TYPE.NINETOSIXTEEN;
      case SIZE_FULL:
        return SIZE_TYPE.FULL;

    }
    return SIZE_TYPE.DEFAULT;
  }
}