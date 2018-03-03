package cn.edu.jxau.lang;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.OrderComparator;
import org.springframework.stereotype.Component;

@Component
public class SystemBootManager implements ApplicationListener<ContextRefreshedEvent> {

	private List<SystemBootPlugin> pluginList;

	public SystemBootManager() {
		pluginList = new ArrayList<>();
	}

	@Autowired
	public void setPluginList(List<SystemBootPlugin> pluginList) {

		this.pluginList = pluginList;
		OrderComparator.sort(pluginList);
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {

		for (SystemBootPlugin plugin : pluginList) {
			plugin.onReady();
		}
	}
}
