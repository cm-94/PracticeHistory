package com.example.booksearch.data

object BookLink {
    private var links : ArrayList<String> = arrayListOf()


    // 책 링크 목록 초기화
    fun clear() =links.clear()

    /**
     * 링크(String) 추가
     * @param link    추가할 링크
     */
    fun addLink(link : String) = links.add(link)

    /**
     * 링크(ArrayList) 추가
     * @param link     추가할 링크 목록
     */
    fun addLink(links : ArrayList<String>) = links.addAll(links)

    fun getLinks(): ArrayList<String> {
        return links
    }


}