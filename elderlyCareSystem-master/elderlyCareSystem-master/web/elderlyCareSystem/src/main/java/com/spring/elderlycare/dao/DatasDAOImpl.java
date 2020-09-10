package com.spring.elderlycare.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.elderlycare.dto.Datas2DTO;
import com.spring.elderlycare.dto.DatasDTO;

@Repository("datasDAO")
public class DatasDAOImpl implements DatasDAO{
	@Autowired
	private SqlSession sqlSession;
	
	private static final String ns = "com.spring.elderlycare.dao.DatasDAOImpl.";
	/*@Override
	public void insertDataEvent(DatasDTO datasdto) {
		sqlSession.insert(ns+"log", datasdto);
	}*/


	@Override
	public List<DatasDTO> selectHumids(int num) {
		return sqlSession.selectList(ns+"selectHumids", num);
	}

	@Override
	public List<DatasDTO> selectTemps(int num) {
		return sqlSession.selectList(ns+"selectTemps", num);
	}
	

	@Override
	public void insertBandDatas(Datas2DTO dto) {
		sqlSession.insert(ns+"log2", dto);
		
	}
	@Override
	public Datas2DTO selectCurData(int num) {
		return sqlSession.selectOne(ns+"curData", num);
	}

	

	@Override
	public List<Datas2DTO> selectHealths(int num) {
		return sqlSession.selectList(ns+"selectHealths", num);
		
	}

	@Override
	public List<DatasDTO> selectHTs(int num) {
		return sqlSession.selectList(ns+"selectHts", num);
	}

	@Override
	public void insertHomeDatas(DatasDTO dto) {
		float hum = (float)dto.getHumid(),temp = (float)dto.getTemp();
		if(hum<30||hum>100) return;
		if(temp< -20|| temp > 40) return;
		
		sqlSession.insert(ns+"log", dto);
		//sqlSession.commit(true);
		
	}

	@Override
	public List<Map<String, Object>> getIPs() {
		return sqlSession.selectList(ns+"selectDevIPs");
	}

	
	
}
