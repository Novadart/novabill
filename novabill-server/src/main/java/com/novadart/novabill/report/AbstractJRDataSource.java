package com.novadart.novabill.report;

import java.util.ArrayList;
import java.util.Collection;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public abstract class AbstractJRDataSource {
	
	public JRBeanCollectionDataSource getDataSource(){
		Collection<AbstractJRDataSource> coll = new ArrayList<>();
		coll.add(this);
		return new JRBeanCollectionDataSource(coll);
	}

}
