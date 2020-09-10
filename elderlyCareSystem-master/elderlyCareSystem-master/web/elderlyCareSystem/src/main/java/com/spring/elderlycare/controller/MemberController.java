package com.spring.elderlycare.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.spring.elderlycare.dto.CalendarDTO;
import com.spring.elderlycare.dto.MemberDTO;
import com.spring.elderlycare.service.MemberService;

@SessionAttributes({"uid", "auth"})
@RestController
@RequestMapping("/users")
public class MemberController {
	@Autowired private MemberService service;
	//@Autowired private MemberDTO mdto;
	private final Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@RequestMapping(value ="login", method = RequestMethod.GET)
	public ModelAndView loginForm(ModelAndView mav) {
		mav.setViewName("login");
		return mav;
	}
	//화면에서 입력 폼 json으로 받기
	@RequestMapping(value = "/login", method = RequestMethod.POST,
			headers= {"Content-type=application/json"})
	public Map<String, Object> loginProcess(HttpSession httpSession,@RequestBody Map<String, Object>m){//MemberDTO mdto) {
		//logger.info("regId: "+m.get("regId").toString());
		int isExist = service.loginCheck(m);//dto);
		Map<String, Object> ret = new HashMap<String, Object>();
		//ret.put("result", false);
		String uid = (String) m.get("uid");
		if(isExist!=-2) {
			ret.put("result", true);
			ret.put("uid", uid);
			//model.addAttribute("uid", mdto.getUid());
			httpSession.setAttribute("uid", uid);
			httpSession.setAttribute("auth", isExist);
			logger.info(httpSession.getId());
			logger.info((String) httpSession.getAttribute("uid"));
		}else
			ret.put("result", false);
		return ret;
	}
	
	/*@RequestMapping(value = "/login", method = RequestMethod.POST,
			headers= {"Content-type=application/json"})
	public @ResponseBody Model loginCheckTest(Model model,@RequestBody MemberDTO mdto) {
		logger.info("++++++++++++login form+++++++++++++");
		logger.info("++++++++++++"+mdto.getUid()+"+++++++++++++");
		boolean isExist = service.loginCheck(mdto);
		//Map<String, Object> ret = new HashMap<String, Object>();
		//ret.put("result", false);
		if(isExist) {
			//ret.put("result", true);
			//ret.put("uid", mdto.getUid());
			model.addAttribute("result", true);
			model.addAttribute("uid", mdto.getUid());
		}else model.addAttribute("result", false); 
			//ret.put("result", false);
		return model;
	}*/
	
	@RequestMapping(value = "/join", method = RequestMethod.GET)
	public ModelAndView joinForm(ModelAndView mav){
		mav.setViewName("signup");
		return mav;
	}
	/*미완*/
	@RequestMapping(value = "/join", method = RequestMethod.POST,
			headers= {"Content-type=application/json"})
	public Map<String,Object>/*Boolean*/ joinProcess(@RequestBody Map<String, Object>map) {
		/*mdto.setUid((String)map.get("uid"));
		mdto.setUpwd((String)map.get("upwd"));
		mdto.setUname((String)map.get("uname"));
		mdto.setUtel((String)map.get("utel"));
		mdto.setUemail((String)map.get("uemail"));*/
		
		//edto.setEname((String) map.get("ename"));
		//edto.setEbirth((String) map.get("ebirth"));
		
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("result", false);
		
		if(service.join(map)>0) { //join(mdto, edto)수정 0713
			ret.put("result", true);
			return ret;
		}
		
		//return ret;
		return ret;
	}
	/*미완*/
	@RequestMapping("/info")
	public MemberDTO myPage(Model model, /*@PathVariable("uid") String m_id*/HttpSession session) {
		String uid = (String) session.getAttribute("uid");
		return service.myPage(uid);
	}
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public Boolean logoutProcess(SessionStatus sessionStatus) {
		sessionStatus.setComplete();
		return true;
	}
	/*미완*/
	@DeleteMapping("/info")
	public Boolean infoDelete(SessionStatus sessionStatus, /*@PathVariable("uid") String m_id*/HttpSession session) {
		String uid = (String) session.getAttribute("uid");
		if(service.delet(uid)>0) {
			sessionStatus.setComplete();
			return true;
		}
		return false;
	}
	/*미완*/
	@PutMapping("/info")
	public Boolean infoModify(MemberDTO mdto  /*@PathVariable("uid") String m_id*/) {
		
		if(service.modify(mdto)>0) {
			return true;
		}
		return false;
	}
	/*미완*/
	@RequestMapping("/mod-form")
	public ModelAndView modifyForm(ModelAndView mav) {
		mav.setViewName("member/modify");
		
		return mav;
	}
	@RequestMapping("/approval")
	public ModelAndView approvalForm(HttpSession httpSession, ModelAndView mav) {
		//manage 승인해야 될 것 있는지 확인후 가져오기
		mav.addObject("aids", service.approvalList(httpSession.getAttribute("uid").toString()));
		mav.setViewName("approval");
		return mav;
	}
	@RequestMapping(value = "/approval", method = RequestMethod.POST,
			headers= {"Content-type=application/json"})
	public Map<String, Object> approvalProcess(@RequestBody Map<String, Object> m) {
		int res = service.approvalProcess(m.get("id").toString());
		
		if(res<1) {
			m.put("result", true);
		}else {
			m.put("result", false);
		}
		return m;
	}
	@RequestMapping(value = "/calendar", method = RequestMethod.POST)
	public boolean calendarPost(@ModelAttribute("uid")String uid, @RequestBody CalendarDTO cdto) {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("uid", uid);
		m.put("cdto", cdto);
		CalendarDTO c = (CalendarDTO) m.get("cdto");
		System.out.println(c.getStart());
		int res = service.calendarPost(m);
		//session uid랑 cdto 이용해서 저장. calendar db.
		
		return res>0?true:false;
		//return true;
	}
	@RequestMapping(value = "/calendar", method = RequestMethod.GET)
	public List<CalendarDTO> calendarGet(@ModelAttribute("uid")String uid) {
		//session uid 이용해서 cdto 가져오기
		return service.calendarGet(uid);
	}
}
