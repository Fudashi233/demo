package cn.edu.jxau.lang;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component
public class BeanSelfProxyAwareMounter implements SystemBootPlugin, ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

	@Override
	public void onReady() {

		Map<String, BeanSelfProxyAware> map = applicationContext.getBeansOfType(BeanSelfProxyAware.class);
		if (map != null) {
			for (BeanSelfProxyAware item : map.values()) {
				item.setSelfProxy(item);
			}
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public String toString() {
		return String.format("BeanSelfProxyAwareMounter:" + getOrder());
	}
}
