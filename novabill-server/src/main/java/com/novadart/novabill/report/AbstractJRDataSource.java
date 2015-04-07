package com.novadart.novabill.report;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractJRDataSource {
	
	public JRBeanCollectionDataSource getDataSource(){
		Collection<AbstractJRDataSource> coll = new ArrayList<>();
		coll.add(this);
		return new JRBeanCollectionDataSource(coll);
	}

}
