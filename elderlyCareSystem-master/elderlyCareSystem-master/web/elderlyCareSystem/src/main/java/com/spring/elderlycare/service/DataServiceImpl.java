package com.spring.elderlycare.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.elderlycare.dao.DatasDAO;
import com.spring.elderlycare.dto.Datas2DTO;
import com.spring.elderlycare.dto.DatasDTO;

@Service
public class DataServiceImpl implements DataService{
	@Autowired DatasDAO dao;

	@Override
	public List<DatasDTO> getHumTemp(int num) {
		//Map<String, Object> map = new HashMap<String, Object>();
		//map.put("humid", dao.selectHumids(num));
		//map.put("temp", dao.selectTemps(num));
		return dao.selectHTs(num);
	}

	@Override
	public void insertBandDatas(Datas2DTO dto) {
		dao.insertBandDatas(dto);
		
	}

	@Override
	public Datas2DTO selectCurData(int num) {
		return dao.selectCurData(num);
	}

	@Override
	public List<Datas2DTO> selectHealths(int num) {
		
		return dao.selectHealths(num);
	}
	
}
