package com.novadart.novabill.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class JasperReportDescriptor {
	
	private String key;
	
	private String path;
	
	private List<JasperReportDescriptor> subreportDescs;

	public JasperReportDescriptor(String key, String path, JasperReportDescriptor[] subreportDescs) {
		this.key = key;
		this.path = path;
		if(subreportDescs != null){
			this.subreportDescs = new ArrayList<JasperReportDescriptor>(subreportDescs.length);
			for(JasperReportDescriptor jrDesc: subreportDescs)
				this.subreportDescs.add(jrDesc);
		}else
			this.subreportDescs = Collections.emptyList();
	}

	public String getKey() {
		return key;
	}

	public String getPath() {
		return path;
	}
	
	public List<JasperReportDescriptor> getFirstLevelSubreportDescs(){
		return subreportDescs;
	}

	public Set<JasperReportDescriptor> getSubreportDescs() {
		Set<JasperReportDescriptor> set = getSubreportDescsInclusive();
		set.remove(this);
		return set;
	}

	
	private Set<JasperReportDescriptor> getSubreportDescsInclusive(){
		Set<JasperReportDescriptor> set = new HashSet<JasperReportDescriptor>();
		set.add(this);
		for(JasperReportDescriptor jrDesc: subreportDescs)
			set.addAll(jrDesc.getSubreportDescsInclusive());
		return set;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JasperReportDescriptor other = (JasperReportDescriptor) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

	
	
}
