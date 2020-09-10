package com.spring.elderlycare.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.elderlycare.dao.MemberDAO;
import com.spring.elderlycare.dto.CalendarDTO;
import com.spring.elderlycare.dto.MemberDTO;
import com.spring.elderlycare.util.SHAUtil;

@Service
public class MemberServiceImpl implements MemberService{
	@Autowired
	private MemberDAO mdao;
	@Autowired SHAUtil SHA;
	@Autowired MemberDTO mdto;
	
	@Override
	@Transactional
	public int join(Map<String, Object> map) {
		mdto.setUid((String)map.get("uid"));
		mdto.setUpwd(SHA.encryptSHA256((String) map.get("upwd")));
		mdto.setUname((String)map.get("uname"));
		mdto.setUtel((String)map.get("utel"));
		mdto.setUemail((String)map.get("uemail"));
		
		//map.put("upwd", SHA.encryptSHA256((String)map.get("upwd")));
		//int ret =  mdao.insertMember(map);
		int ret = mdao.insertMember(mdto);
		//int ret = 1;
		if(ret>0) 
			ret= mdao.insertRelation(map); 
		
		return ret;
	}
	@Override
	public int loginCheck(Map<String, Object> m) {//MemberDTO mdto){
		mdto.setUid((String) m.get("uid"));
		mdto.setUpwd(SHA.encryptSHA256((String) m.get("upwd")));
		int res = mdao.exist(mdto);
		if(res!=-2&&m.get("regId")!=null) {
			mdao.updateRegId(m);
		}
		return res;
	}
	@Override
	public int modify(MemberDTO mdto){
		int ret = mdao.updateMember(mdto);
		return ret;
	}
	@Override
	public int delet(String id){
		int ret = mdao.deleteMember(id);
		return ret;
	}
	@Override
	public MemberDTO myPage(String id){
		MemberDTO mdto = null;
		mdto = mdao.selectOne(id);
		return mdto;
	}
	/*@Override
	public List<DeviceUserDTO>devicesList(String id){
		return mdao.selectManageDevices(id);

	}*/
	@Override
	public List<String> approvalList(String uid) {
		return mdao.selectApvList(uid);
	}
	@Override
	public int approvalProcess(String aid) {
		return mdao.updateRole(aid);
	}
	@Override
	public int calendarPost(Map<String, Object> map) {
		return mdao.insertCalendar(map);
	}
	@Override
	public List<CalendarDTO> calendarGet(String uid) {
		return mdao.selectCalendars(uid);
	}
}
