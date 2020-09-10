package com.spring.elderlycare.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.spring.elderlycare.dto.DevicesDTO;
import com.spring.elderlycare.dto.Elderly2DTO;
import com.spring.elderlycare.dto.ElderlyDTO;

@Component
public interface DeviceDAO {
	public List<Elderly2DTO> selectList(String id);
	public ElderlyDTO selectOne(int dnum);
	public DevicesDTO selectDevice(int dnum);
	public void insertDevice(ElderlyDTO edto);
	public void insertElderly(ElderlyDTO edto);
	public void insertManage(Map<String, Object>map);
	public void updateDevice(ElderlyDTO dudto);
	public void deleteDevice(int dnum);
	public Map<String, Object> eldLogin(Map<String, Object>eldInfo);
}
