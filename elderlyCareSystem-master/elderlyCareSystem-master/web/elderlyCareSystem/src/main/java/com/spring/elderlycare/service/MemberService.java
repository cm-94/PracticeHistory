package com.spring.elderlycare.service;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.spring.elderlycare.dto.CalendarDTO;
import com.spring.elderlycare.dto.MemberDTO;

@Component
public interface MemberService {
	
	public int join(Map<String, Object> map);
	public int loginCheck(Map<String, Object>m);//MemberDTO mdto);
	public int modify(MemberDTO mdto);
	public int delet(String id);
	public MemberDTO myPage(String id);
	public List<String> approvalList(String uid);
	public int approvalProcess(String aid);
	public int calendarPost(Map<String, Object> map);
	public List<CalendarDTO> calendarGet(String uid);
}
