package com.spring.elderlycare.dto;

import org.springframework.stereotype.Repository;

@Repository("BundleDTO")
public class BundleDTO {
	private DevicesDTO ddto;
	private ElderlyDTO edto;
	public BundleDTO(DevicesDTO ddto, ElderlyDTO edto) {
		this.ddto = ddto;
		this.edto = edto;
	}
	public DevicesDTO getDdto() {
		return ddto;
	}
	public void setDdto(DevicesDTO ddto) {
		this.ddto = ddto;
	}
	public ElderlyDTO getEdto() {
		return edto;
	}
	public void setEdto(ElderlyDTO edto) {
		this.edto = edto;
	}
	
}
