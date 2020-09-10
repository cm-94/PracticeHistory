package com.spring.elderlycare.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.elderlycare.dao.DeviceDAO;
import com.spring.elderlycare.dto.DevicesDTO;
import com.spring.elderlycare.dto.Elderly2DTO;
import com.spring.elderlycare.dto.ElderlyDTO;

@Service
public class DeviceServiceImpl implements DeviceService{
	
	@Autowired private DeviceDAO ddao;
	
	
	@Override
	public List<Elderly2DTO> devicesList(String id) {
		List<Elderly2DTO> list = ddao.selectList(id);
		return list;
	}

	@Override
	public void deviceRegistration(ElderlyDTO edto, String id) {
		ddao.insertDevice(edto);
		ddao.insertElderly(edto);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("elderly", edto.getEkey());
		map.put("staff", id);
		ddao.insertManage(map);
	}

	@Override
	public ElderlyDTO elderlyInfo(int dnum) {
		
		return ddao.selectOne(dnum);
	}

	@Override
	public DevicesDTO deviceInfo(int dnum) {
		return ddao.selectDevice(dnum);
	}

	@Override
	public void deleteDevice(int dnum) {
		ddao.deleteDevice(dnum);
	}

	@Override
	public Map<String, Object> eldLogin(Map<String, Object> eldInfo) {
		return ddao.eldLogin(eldInfo);
	}

}
