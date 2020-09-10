package com.spring.elderlycare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.spring.elderlycare.dto.Datas2DTO;
import com.spring.elderlycare.service.DataService;

@RestController
@RequestMapping("/datas")
public class ReceiveController {
	@Autowired private DataService service;
	
	@RequestMapping(method = RequestMethod.POST,
			headers= {"Content-type=application/json"})
	@Async
	public String receiveData(@RequestBody Datas2DTO dto){
		if(dto.getStat() == 0) {
			//이상 상태. 빨간색 표시.
		}
		service.insertBandDatas(dto);
		return "receive test";
	}
}
