package org.smg.TwoLevelCache.ngp;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.openwave.policy.AttributeValuePair;
import com.openwave.policy.AttributeValuePair.Type;
import com.openwave.policy.Policy;
import com.openwave.policy.Rule;
import com.openwave.policy.Rule.RuleType;
import com.openwave.sessionmanager.cache.Session;
import com.openwave.sessionmanager.cache.SessionCacheImpl;
import com.openwave.sessionmanager.cache.SessionImpl;
import com.openwave.sessionmanager.cache.radius.CustomAttribute;
import com.openwave.sessionmanager.cache.radius.CustomDataType;
import com.openwave.sessionmanager.cache.radius.Identity;
import com.openwave.sessionmanager.cache.radius.IdentityImpl;

public class NGPSessionModeller {
	public static void main(String [] args) throws UnknownHostException {
		SessionCacheImpl sessionCache = new SessionCacheImpl();
		
		Random r = new Random();
		
		for(int i=0; i<100000; i++) {
			String ipAddress = "10.20." + r.nextInt(255) + "." + r.nextInt(255);
			Session session = sessionCache.createSessionWithIP(ipAddress);
			
			String identity = "id-" + r.nextInt();
			session.setIdentity(identity);
			
	        final int createTime = 0;
	        final int eventTimestamp = 0;
	        
			List<CustomAttribute> customAttributes = new ArrayList<CustomAttribute> ();
			int vendor1 =0;
			int vendorType1 = 0;
			customAttributes.add(new CustomAttribute("attribute" + r.nextInt(50), 0, CustomDataType.Integer, 10, vendor1, vendorType1 ));
			customAttributes.add(new CustomAttribute("attribute" + r.nextInt(50), 1, CustomDataType.Blob, new byte[10], vendor1, vendorType1 ));
			
			String deviceId = "07980" + r.nextInt();
			String userName = "user" + r.nextInt();
			
			Identity radiusIdentity = new IdentityImpl(ipAddress,
													   deviceId,
													   userName,
													   createTime,
													   "radClient1",
													   900,
													   customAttributes ,
													   "abcd",
													   "correlationId",
													   eventTimestamp);
			session.setRadiusIdentity(radiusIdentity);
	
			String gxSessionId = "gx-" + i;
			Policy policy = new Policy(gxSessionId);
			HashMap<String, Rule> chargingRules = new HashMap<String,Rule>();
			chargingRules.put("GOLD", new Rule("ruleName1", RuleType.INSTALL, false));
			chargingRules.put("SILVER", new Rule("ruleName2", RuleType.INSTALL, false));
			
			policy.setChargingRules(chargingRules);
			
			List<AttributeValuePair> capturedAttributes = new ArrayList<> ();
			capturedAttributes.add(new AttributeValuePair("attrName1", Type.INTEGER, 1));
			capturedAttributes.add(new AttributeValuePair("attrName2", Type.INTEGER, 2));
	
			policy.setCapturedAttributes(capturedAttributes );
	
			session.setPolicy(policy );
	
			Map<String, String> attributes = new HashMap<> ();
			attributes.put("attr1", "value1");
			attributes.put("attr2", "value2");
			attributes.put("attr3", "value3");
	
			session.setSubscriberAttributes(attributes );
			
			sessionCache.put(ipAddress, session);
		}
	}
}
