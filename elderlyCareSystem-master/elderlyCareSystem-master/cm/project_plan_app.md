# 독거노인을 위한 건강 모니터링 시스템



## App Summary
    * 관리자-보호자는 피부양자를 관찰하고 관리할 수 있다.
    * 보호자는 관리자에게 가입 및 피부양자 추가 요청을 할 수 있다.
    * 관리자 및 보호자는 자신의 피부양자를 영상으로 관찰할 수 있다.
    * 관리자 및 보호자는 자신의 피부양자의 상태를 확인할 수 있다.
    * 관리자 및 보호자는 피부양자에게 특이사항 발생 시 알람을 통해 확인할 수 있다.

## Interface
    1. Login Activity
        * Input Id & Password
        * Admin - User Radio Button
        * Sign Up(Url Link)
            * Input User Data
  
   
    2. List Activity
      2.1 Admininstrator Account
          Elder 1  Name | Statement | Button
          Elder 2  Name | Statement | Button
          ...
          Elder20  Name | Statement | Button
      2.2 User Account
        * Check Own Elder
          Elder 1  Name | Statement | Button
          Elder 2  Name | Statement | Button
          Elder 3  Name | Statement | Button


    3. Statement Activity
        * Check Statement
            - Temperature
            - Purse
            - GPS
        * PhoneCall Button
            - System PhoneCall App Float
              - TO Elder 
              - TO Administrator 
            
        * Webcam Button
            - Link Streaming Site Url

    4. Emergency
        * pop-up window
            - Call Directly Button
            - Check Statement Button