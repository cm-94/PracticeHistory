package com.spring.elderlycare.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.spring.elderlycare.dto.DevicesDTO;
import com.spring.elderlycare.dto.Elderly2DTO;
import com.spring.elderlycare.dto.ElderlyDTO;

@Component
public interface DeviceService {
	public List<Elderly2DTO>devicesList(String id);
	public void deviceRegistration(ElderlyDTO edto, String id);
	public ElderlyDTO elderlyInfo(int dnum);
	public DevicesDTO deviceInfo(int dnum);
	public void deleteDevice(int dnum);
	public Map<String, Object> eldLogin(Map<String, Object> eldInfo);
}
