package lizzy.springcloud.message.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lizzy.springcloud.message.dto.SmsSendRequest;
import lizzy.springcloud.message.dto.SmsSendResponse;

public class SmsControllerTest {
	private static final String SEND_URL = "http://localhost:8080/message/sms/send";
	private static final String SEND2_URL = "http://localhost:8080/message/sms/send2";
	private RestTemplate restTemplate = new RestTemplate();
	@Test
	public void testSend() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("mobile", "18562875992");
		map.add("templateId", "CHECK_CODE");
		map.add("params['code']", "123456");

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		ResponseEntity<SmsSendResponse> response = restTemplate.postForEntity(SEND_URL, request, SmsSendResponse.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
		assertThat(response.getBody(), notNullValue());
		SmsSendResponse sendResponse = response.getBody();
		assertThat(sendResponse.getCode(), equalTo("200"));
		assertThat(sendResponse.getMessage(), equalTo("发送成功"));
	}
	
	//这种方式客户端直接传递SmsSendRequest参数，RestTemplate内部会将其转换成json传传输
	@Test
	public void testSend2() {
		SmsSendRequest request = new SmsSendRequest();
		request.setMobile("18562875992");
		request.setTemplateId("CHECK_CODE");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("code", "123456");
		request.setParams(params);
		ResponseEntity<SmsSendResponse> response = restTemplate.postForEntity(SEND2_URL, request, SmsSendResponse.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
		assertThat(response.getBody(), notNullValue());
		SmsSendResponse sendResponse = response.getBody();
		assertThat(sendResponse.getCode(), equalTo("200"));
		assertThat(sendResponse.getMessage(), equalTo("发送成功"));
	}

}
