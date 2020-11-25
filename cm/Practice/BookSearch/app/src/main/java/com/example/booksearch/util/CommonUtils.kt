package com.example.booksearch.util

object CommonUtils{
    const val DEFAULT_START = 1
    const val DISPLAY_COUNT = 20
    const val DISPLAY_MAX = 1000 // MainActivity에서만 사용하므로 옮겨도..( 데이터 요청시 사용 )
    const val EDIT_INPUT = "EDIT_INPUT"
    const val BOOK_LIST = "BOOK_INFO"
    const val BOOK_INFO_URL = "BOOK_INFO_URL"
    const val BOOK_INFO_INDEX = "BOOK_INFO_INDEX"

    const val HTML_TAG ="<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>" // BookAdapter에서만 사용하므로 옮겨도..( 태그 제거시 사용 )
}