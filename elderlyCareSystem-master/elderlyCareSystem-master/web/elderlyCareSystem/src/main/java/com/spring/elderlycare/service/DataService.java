package com.spring.elderlycare.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.spring.elderlycare.dto.Datas2DTO;
import com.spring.elderlycare.dto.DatasDTO;

@Component
public interface DataService {
	public List<DatasDTO> getHumTemp(int num);
	public List<Datas2DTO> selectHealths(int num);
	public void insertBandDatas(Datas2DTO dto);
	public Datas2DTO selectCurData(int num);
}
