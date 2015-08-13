package com.se.RelatedFeaturesNew;

import com.se.Exceptions.FeatureNameException;
import com.se.Exceptions.NotValidRelatedFeatureException;

import static com.se.RelatedFeaturesNew.RelatedFeatures.*;

public class MultiExporterData extends DirectExporterData{
	
	@Override
	public void setFetName(String fetId, String fetValId)
			throws FeatureNameException, NotValidRelatedFeatureException {
		
		String finalFetName = "";
		String finalFetVal = "";
		//split fets
		String[] fetIds = fetId.split(CONCATENATE_FET_CHAR_SEP);
		String[] fetValIds = null;
		if(fetValId!=null){
			fetValIds = fetValId.split(CONCATENATE_FET_CHAR_SEP);
		}
		
		for (int i = 0; i < fetIds.length; i++) {
			String fet = fetIds[i];
			String val = null;
			if(fetValId!=null){
				val = fetValIds[i];
			}				
			super.setFetName(fet, val);
			finalFetName += getFetName()+RelatedFeatures.CONCATENATE_FET_CHAR;
			finalFetVal += getFetVal()+RelatedFeatures.CONCATENATE_FET_CHAR;
		}
		fetName = finalFetName.substring(0, finalFetName.lastIndexOf(CONCATENATE_FET_CHAR));
		fetVal = finalFetVal.substring(0, finalFetVal.lastIndexOf(CONCATENATE_FET_CHAR));				
	}	

}
