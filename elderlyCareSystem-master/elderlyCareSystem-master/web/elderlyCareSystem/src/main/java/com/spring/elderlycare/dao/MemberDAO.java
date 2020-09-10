package com.spring.elderlycare.dao;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.spring.elderlycare.dto.CalendarDTO;
import com.spring.elderlycare.dto.MemberDTO;

@Component
public interface MemberDAO {
	//public int insertMember(Map<String, Object> map);
	public int updateMember(MemberDTO mdto);
	public int deleteMember(String id);
	public MemberDTO selectOne(String id);
	public int exist(MemberDTO mdto);
	public int insertMember(MemberDTO mdto);
	public int insertRelation(Map<String, Object> map);//throws Exception;
	public int updateRegId(Map<String, Object> map);
	public int updateRole(String aid);
	public List<String> selectApvList(String uid);
	public int insertCalendar(Map<String, Object>map);
	public List<CalendarDTO>selectCalendars(String uid);
}
