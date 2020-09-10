package com.spring.elderlycare.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.elderlycare.dto.DevicesDTO;
import com.spring.elderlycare.dto.Elderly2DTO;
import com.spring.elderlycare.dto.ElderlyDTO;

@Repository("deviceDAO")
public class DeviceDAOImpl implements DeviceDAO{
	@Autowired
	private SqlSession sqlSession;
	@Autowired
	ElderlyDTO edto;
	private static final String ns = "com.spring.elderlycare.dao.DeviceDAOImpl.";
	
	@Override
	public List<Elderly2DTO> selectList(String id) {
		return sqlSession.selectList(ns+"selectDevices", id);
		//return null;
	}

	@Override
	public ElderlyDTO selectOne(int dnum) {
		return sqlSession.selectOne(ns+"selectDevice", dnum);
		//return null;
	}

	@Override
	public void insertDevice(ElderlyDTO edto) {
		
		sqlSession.insert(ns+"insertElderly", edto);
		
	}
	
	

	@Override
	public void updateDevice(ElderlyDTO dudto) {
		//sqlSession.update(ns+"", dudto);
	}

	@Override
	public void deleteDevice(int dnum) {
		sqlSession.delete(ns+"deleteDevice", dnum);
	}

	@Override
	public DevicesDTO selectDevice(int dnum) {
		
		return sqlSession.selectOne(ns+"selectDeviceOne", dnum);
	}

	@Override
	public Map<String, Object> eldLogin(Map<String, Object> eldInfo) {
		return sqlSession.selectOne(ns+"elderlyLogin", eldInfo);
	}

	@Override
	public void insertElderly(ElderlyDTO edto) {
		sqlSession.insert(ns+"insertDevice", edto);
		
	}

	@Override
	public void insertManage(Map<String, Object>map) {
		sqlSession.insert(ns+"insertManage", map);
		
	}

}
