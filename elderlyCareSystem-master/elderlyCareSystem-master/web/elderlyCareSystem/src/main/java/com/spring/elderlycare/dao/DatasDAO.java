package com.spring.elderlycare.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.spring.elderlycare.dto.Datas2DTO;
import com.spring.elderlycare.dto.DatasDTO;

@Component
public interface DatasDAO  {
	//public void insertDataEvent(DatasDTO datasdto);
	public List<DatasDTO> selectHumids(int num);
	public List<DatasDTO> selectTemps(int num);
	public void insertBandDatas(Datas2DTO dto);
	//public Datas2DTO selectCurHealthData(int num);
	//public DatasDTO selectCurHTData(int num);
	public Datas2DTO selectCurData(int num);
	public List<Datas2DTO> selectHealths(int num);
	public List<DatasDTO> selectHTs(int num);
	public void insertHomeDatas(DatasDTO dto);
	public List<Map<String, Object>> getIPs();
	
}
