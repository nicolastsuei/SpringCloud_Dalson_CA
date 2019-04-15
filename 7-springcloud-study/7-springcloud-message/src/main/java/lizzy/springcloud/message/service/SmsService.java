package lizzy.springcloud.message.service;

import lizzy.springcloud.message.dto.SmsSendRequest;
import lizzy.springcloud.message.dto.SmsSendResponse;

public interface SmsService {
	public SmsSendResponse send(SmsSendRequest request);
}
