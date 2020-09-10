package com.spring.elderlycare.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.spring.elderlycare.dto.Datas2DTO;
import com.spring.elderlycare.dto.DatasDTO;
import com.spring.elderlycare.dto.Elderly2DTO;
import com.spring.elderlycare.dto.ElderlyDTO;
import com.spring.elderlycare.service.DataService;
import com.spring.elderlycare.service.DeviceService;

@SessionAttributes("uid")
@RestController
@RequestMapping("/devices")
public class DeviceController {
	@Autowired private DeviceService service;
	@Autowired private DataService dataservice;
	//@Autowired private MqttTaskService mqtt;
	//@Autowired private ElderlyDTO edto;
	@Autowired private Datas2DTO datadto;
	private final Logger logger = LoggerFactory.getLogger(DeviceController.class);

	
	@RequestMapping(method = RequestMethod.GET)
	public List<Elderly2DTO> deviceList(HttpSession httpSession) {
		List<Elderly2DTO> list = service.devicesList((String)httpSession.getAttribute("uid"));
		
		return list;
	}
	/*********************************/
	/*********************************/
	
	@RequestMapping(value = "/login", method = RequestMethod.POST,
			headers= {"Content-type=application/json"})
	public Map<String, Object> loginForEApp(@RequestBody Map<String, Object> eldInfo){
		return service.eldLogin(eldInfo);
	}
	
	@RequestMapping(value = "/form", method = RequestMethod.POST)
			//,headers= {"Content-type=application/json"})
	public ModelAndView registrationProcess(HttpSession httpSession,ModelAndView mav, ElderlyDTO edto) {
		
		service.deviceRegistration(edto, (String)httpSession.getAttribute("uid"));
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("/elderlycare/");
		redirectView.setExposeModelAttributes(false);
		mav.setView(redirectView);
		
		return mav;
	}
	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public ModelAndView registrationForm(ModelAndView mav) {
		mav.setViewName("regForm");
		
		return mav;
	}

	
	 @RequestMapping(value = "/{num}", method = RequestMethod.GET) 
	 public ElderlyDTO deviceInfo(HttpSession session, @PathVariable("num") int dnum) {
	  
	  return service.elderlyInfo(dnum);
	   
	 }
	 /*미완*/
	@RequestMapping(value = "/{num}", method = RequestMethod.PUT)
	public ElderlyDTO deviceModify(Model model) {
		
		return null;
	}
	/*미완*/
	@RequestMapping(value = "/{num}", method = RequestMethod.DELETE)
	public ModelAndView deviceDelete(ModelAndView mav, @PathVariable("num") int num) {
		service.deleteDevice(num);
		return null;
	}
	/*dksTma*/
	@RequestMapping("/{num}/daydatas")
	public Map<String, Object> viewDataLog(Model model, @PathVariable("num") int num) {
		Map<String, Object> map = new HashMap<String, Object>();
		//List<Datas2DTO> li = dataservice.selectHealths(num);
		
		map.put("band", dataservice.selectHealths(num));
		map.put("ht", dataservice.getHumTemp(num));
		
		
		return map;
	}
	@RequestMapping("/{num}/banddatas")
	public List<Datas2DTO> viewBandData(Model model, @PathVariable("num") int num) {
		List<Datas2DTO> list = dataservice.selectHealths(num);
		
		return list;
	}
	@RequestMapping("/{num}/htdatas")
	public List<DatasDTO> viewHTdatas(Model model, @PathVariable("num") int num) {
		List<DatasDTO> list = dataservice.getHumTemp(num);
		return list;
	}
	@RequestMapping(value = "/{num}/curdata", method = RequestMethod.GET)
	public Datas2DTO viewCurData(Model model, @PathVariable("num") int num){
		//Map<String, Object> map = new HashMap<String, Object>();
		
		datadto = dataservice.selectCurData(num);
		// 현재 온습도까지
		return datadto;
	}
	@RequestMapping("/datas")
	public ModelAndView datasPage(ModelAndView mav) {
		
		mav.setViewName("charts");
		return mav;
	}
	@RequestMapping("/monitoring")
	public ModelAndView monitoringPage(ModelAndView mav) {
		
		mav.setViewName("monitoring");
		return mav;
	}
}
